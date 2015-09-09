package test.freelancer.com.fltest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * List that displays the TV Programmes
 */
public class ListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListView view = (ListView) inflater.inflate(R.layout.fragment_list, container, false);

        // Async Data Checker code
//        // eurgh, damn android.os.NeworkOnMainThreadException - so pesky!
//        // stackoverflow told me to do this:
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        try {
            Bundle bundle = this.getArguments();
            if (bundle!=null && !bundle.isEmpty()) {
//                JSONObject json = new JSONObject(bundle.getString("json"));
                JSONArray arr = new JSONArray(bundle.getString("json"));
                view.setAdapter(new ListAdapter(getActivity().getApplicationContext(), /*json.getJSONArray("results"*/ arr));
            } else {
                view.setAdapter(new ListAdapter(getActivity().getApplicationContext(), null));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }


}
