package view.customspinner;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SimpleAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mCacheData;

    public SimpleAdapter(Context context, List<String> spinnerData) {
        mContext = context;
        mCacheData = new ArrayList<>(spinnerData);
    }

    @Override
    public int getCount() {
        return mCacheData == null ? 0 : mCacheData.size();
    }

    @Override
    public String getItem(int position) {
        return mCacheData == null ? "" : mCacheData.get(position) == null ? "" : mCacheData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.spinner_item, null);
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(Html.fromHtml(getItem(position)));
        return textView;
    }
}
