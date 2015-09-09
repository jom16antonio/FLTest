package test.freelancer.com.fltest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


public class MainActivity extends ActionBarActivity {

    private AsyncHttpClient mClient = null;
    private JSONArray mJson = null;
    private ListFragment mContainer = null;
    private int mTotalCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Async Call to get channel programming
        getAllPrograms(0);
    }

    public AsyncHttpClient getAsyncHttpClient(){
        return mClient;
    }

    public ListFragment getContainerFragment() {
        return mContainer;
    }

    private void getAllPrograms(int start) {
        // Async Loading of Channel Programming Data
        mClient = new AsyncHttpClient();
        mClient.get("http://whatsbeef.net/wabz/guide.php?start=" + start, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String jsonString = null;
                try {
                    jsonString = new String(response, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject json = new JSONObject(jsonString);
                    mTotalCount = json.getInt("count");
                    if (mJson != null && mJson.length() > 0) {
                        mJson = concatArray(mJson, json.getJSONArray("results"));
                    } else {
                        mJson = json.getJSONArray("results");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                saveDataOffline(mJson.toString());
                reloadUI(mJson.toString());


                if (mJson.length() < mTotalCount) {
                    getAllPrograms(mJson.length());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                // If Offline get stored persistence value storage
                reloadUI(loadOfflineData());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    // Load the Listview data from Async response
    private void reloadUI(String data) {
        Bundle bundle = new Bundle();
        if (data != null) {
            bundle.putString("json", data);
        }
        mContainer = new ListFragment();
        mContainer.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main, mContainer);
        transaction.commit();
    }

    private void saveDataOffline(String data) {
        SharedPreferences settings = getSharedPreferences("PREFS_JSONDATA", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("JSONDATA", data);
        // Commit the edits!
        editor.commit();
    }

    private String loadOfflineData() {
        // Restore preferences
        SharedPreferences settings = getSharedPreferences("PREFS_JSONDATA", 0);
        return settings.getString("JSONDATA", null);
    }

    private JSONArray concatArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;
    }

}
