package fu.nnyfro.ghunt;

import android.app.Application;

import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;


import java.util.Map;
import android.os.Handler;



public class ThisApplication extends Application {

    private String AF_KEY = "";
    String name = "c";
    public static boolean hasDeeplink = false;
    public static Boolean isInstallReferrerIsReady = false;
    String TAG = "test_";


    @Override
    public void onCreate() {
        super.onCreate();


        AF_KEY = getResources().getString(R.string.AF_KEY);

        InstallRefferer instllRfr = new InstallRefferer();
        instllRfr.buildInstallReferrer(ThisApplication.this);

        final Handler handler = new Handler();


        AppsFlyerConversionListener conversionDataListener =
                new AppsFlyerConversionListener() {
                    @Override
                    public void onInstallConversionDataLoaded(final Map<String, String> conversionData) {

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (!isInstallReferrerIsReady) {
                                    handler.postDelayed(this, 400);
                                    Log.i(TAG, "waiting install refferer: ");
                                } else {
                                    Log.i(TAG, "there is deeplink in install refferer: " + hasDeeplink);
                                    if (conversionData.containsKey(name)) {
                                        String nameValue = conversionData.get(name);
                                        Log.i(TAG, "name " + nameValue);
                                        if (!hasDeeplink) {
                                            Log.i(TAG, "no utms");
                                            MainActivity.deeplink = nameValue;
                                        }
                                    }
                                }
                            }

                        }, 400);


                    }

                    @Override
                    public void onInstallConversionFailure(String errorMessage) {

                    }

                    @Override
                    public void onAppOpenAttribution(Map<String, String> attributionData) {

                    }

                    @Override
                    public void onAttributionFailure(String errorMessage) {

                    }
                };
        AppsFlyerLib.getInstance().init(AF_KEY, conversionDataListener);
        AppsFlyerLib.getInstance().startTracking(this);

    }

}




