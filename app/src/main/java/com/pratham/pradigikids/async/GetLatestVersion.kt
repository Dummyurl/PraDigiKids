package com.pratham.pradigikids.async

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.pratham.pradigikids.ui.splash.SplashContract
import org.jsoup.Jsoup

class GetLatestVersion(@SuppressLint("StaticFieldLeak") var context: Context?,
                       private var splash_presenter: SplashContract.splashPresenter?) : AsyncTask<Void, Void, Void>() {
    private var latestVersion: String = ""

    override fun doInBackground(vararg params: Void): Void? {
        try {
            //It retrieves the latest version by scraping the content of current version from play store at runtime
            // Document doc = w3cDom.fromJsoup(Jsoup.connect(urlOfAppFromPlayStore).get());
            //Log.d(TAG,"playstore doc "+getStringFromDoc(doc));
            latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.pratham.pradigikids" + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
                    .ownText()
            Log.d("latest::", latestVersion)
            //latestVersion = doc.getElementsByTagName("softwareVersion").first().text();
            //latestVersion = "1.5";
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        Log.d("version::", "Latest version = $latestVersion")
        splash_presenter!!.checkVersion(latestVersion)
    }
}