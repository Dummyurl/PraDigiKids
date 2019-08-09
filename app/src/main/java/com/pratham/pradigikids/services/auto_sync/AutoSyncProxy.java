package com.pratham.pradigikids.services.auto_sync;

import android.content.Context;

import java.util.Arrays;

public final class AutoSyncProxy {
    private final Context context;
    private final String name;
    private final AutoSync listener;

    AutoSyncProxy(Context context, String name) {
        this.context = context;
        this.name = name;
        listener = AutoSyncParser.parseListeners(context).get(name);
    }

    /**
     * Syncs immediately. This is useful for a response to a user action. Use this sparingly, as
     * frequent syncs defeat the purpose of using this library.
     */
    public void sync() {
        AutoSyncService.sync(context, name);
    }

    /**
     * Syncs sometime in the near future, randomizing per device. This is useful in response to a
     * server message, using GCM for example, so that the server is not overwhelmed with all devices
     * trying to sync at once.
     */
    public void syncInexact() {
        AutoSyncService.syncInexact(context, name);
    }

    /**
     * Gets the current configuration for the {@link AutoSync}.
     *
     * @return the configuration
     * @see AutoSync.Config
     */
    public AutoSync.Config config() {
        return listener.config();
    }

    /**
     * Modifies the current configuration for the {@link AutoSync}.
     *
     * @param edits the edits
     * @see AutoSync#edit(AutoSync.Edit...)
     */
    private void edit(Iterable<AutoSync.Edit> edits) {
        listener.edit(edits);
        AutoSyncService.update(context, name);
    }

    /**
     * Modifies the current configuration for the {@link AutoSync}.
     *
     * @param edits the edits
     * @see AutoSync#edit(AutoSync.Edit...)
     */
    public void edit(AutoSync.Edit... edits) {
        edit(Arrays.asList(edits));
    }
}
