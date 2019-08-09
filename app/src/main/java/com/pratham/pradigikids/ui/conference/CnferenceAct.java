package com.pratham.pradigikids.ui.conference;

public class CnferenceAct /*extends JitsiMeetActivity*/ {
    /**
     * The request code identifying requests for the permission to draw on top
     * of other apps. The value must be 16-bit and is arbitrarily chosen here.
     */
    /*private static final int OVERLAY_PERMISSION_REQUEST_CODE
            = (int) (Math.random() * Short.MAX_VALUE);
// JitsiMeetActivity overrides

    @Override
    protected boolean extraInitialize() {
        // Setup Crashlytics and Firebase Dynamic Links
        // Here we are using reflection since it may have been disabled at compile time.
        try {
            Class<?> cls = Class.forName("org.jitsi.meet.GoogleServicesHelper");
            Method m = cls.getMethod("initialize", JitsiMeetActivity.class);
            m.invoke(null, this);
        } catch (Exception e) {
            e.printStackTrace();
            // Ignore any error, the module is not compiled when LIBRE_BUILD is enabled.
        }
        if (canRequestOverlayPermission() && !Settings.canDrawOverlays(this)) {
            Intent intent
                    = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
            return true;
        } else return false;
    }

    @Override
    protected void initialize() {
        // Set default options
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setWelcomePageEnabled(true)
                .setServerURL(buildURL("https://meet.jit.si"))
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        super.initialize();
    }

    @Override
    public void onConferenceTerminated(Map<String, Object> data) {
        Log.d(TAG, "Conference terminated: " + data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE
                && canRequestOverlayPermission()) {
            if (Settings.canDrawOverlays(this)) {
                initialize();
                return;
            }

            throw new RuntimeException("Overlay permission is required when running in Debug mode.");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private @Nullable
    URL buildURL(String urlStr) {
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private boolean canRequestOverlayPermission() {
        return
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M;
    }*/
}
