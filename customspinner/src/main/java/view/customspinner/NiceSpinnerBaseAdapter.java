package view.customspinner;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class NiceSpinnerBaseAdapter<T> extends BaseAdapter {

    private Context mContext;
    public int mSelectedIndex;
    private int mTextColor;
    private int mBackgroundSelector;
    private int layoutId;

    public NiceSpinnerBaseAdapter(Context context, int textColor, int backgroundSelector, int layoutId) {
        mContext = context;
        this.layoutId = layoutId;
        mTextColor = textColor;
        mBackgroundSelector = backgroundSelector;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        if (convertView == null) {
            if (layoutId == 0) {
                layoutId = R.layout.spinner_list_item;
            }
            convertView = View.inflate(mContext, layoutId, null);
            textView = (TextView) convertView.findViewById(R.id.tv_tinted_spinner);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.setBackground(ContextCompat.getDrawable(mContext, mBackgroundSelector));
            }

            convertView.setTag(new ViewHolder(textView));
        } else {
            textView = ((ViewHolder) convertView.getTag()).textView;
        }

        textView.setText(getItem(position).toString());
        textView.setTextColor(mTextColor);

        return convertView;
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void notifyItemSelected(int index) {
        mSelectedIndex = index;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public abstract int getCount();

    public abstract T getItemInDataset(int position);

    protected static class ViewHolder {

        public TextView textView;

        public ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }
}
