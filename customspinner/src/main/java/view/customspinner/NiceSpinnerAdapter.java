package view.customspinner;

import android.content.Context;

import java.util.List;

public class NiceSpinnerAdapter<T> extends NiceSpinnerBaseAdapter {

    private final List<T> mItems;

    public NiceSpinnerAdapter(Context context, List<T> items, int textColor, int backgroundSelector, int id) {
        super(context, textColor, backgroundSelector, id);
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public T getItemInDataset(int position) {
        return mItems.get(position);
    }
}