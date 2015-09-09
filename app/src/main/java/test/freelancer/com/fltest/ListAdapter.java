package test.freelancer.com.fltest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JoM on 9/9/2015.
 */
public class ListAdapter extends BaseAdapter {

    private final class ViewHolder {
        TextView name;
        TextView time;
        TextView channel_rating;
    }

    JSONArray array;
    private ViewHolder mHolder = null;
    private LayoutInflater mInflater = null;

    public ListAdapter(Context context, JSONArray response) {
        array = response;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (array == null) return 0;
        return array.length();
    }

    @Override
    public JSONObject getItem(int position) {
        try {
            return array.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_itemrow, null);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        try {
            // Display Name
            mHolder.name = (TextView) convertView.findViewById(R.id.rowitem_name);
            mHolder.name.setText(array.getJSONObject(position).getString("name"));

            // Display Start time and End time
            mHolder.time = (TextView) convertView.findViewById(R.id.rowitem_time);
            mHolder.time.setText(array.getJSONObject(position).getString("start_time") + "  -  " +
                    array.getJSONObject(position).getString("end_time"));

            // Display Channel and Rating
            mHolder.channel_rating = (TextView) convertView.findViewById(R.id.rowitem_channel_rating);
            mHolder.channel_rating.setText("Channel: " + array.getJSONObject(position).getString("channel") + "   Rating: " +
                    array.getJSONObject(position).getString("rating"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}

