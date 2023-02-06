package lib.spinner;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

public class CustomSpinner extends AppCompatTextView {

    private OnItemSelectedListener onItemSelectedListener;
    private ListAdapter adapter;
    private PopupWindow popupWindow;
    private CustomListView listView;
    private Drawable arrowDrawable;
    private boolean hideArrow;
    private int selectedIndex;
    private int elevation;
    private int listHeight;
    private int width, height;

    public CustomSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner);

        Drawable bg;
        try {
            hideArrow = ta.getBoolean(R.styleable.CustomSpinner_cs_hide_arrow, false);
            bg = ta.getDrawable(R.styleable.CustomSpinner_cs_popup_background);
            width = ta.getDimensionPixelSize(R.styleable.CustomSpinner_cs_arrow_width, dpToPx(15));
            height = ta.getDimensionPixelSize(R.styleable.CustomSpinner_cs_arrow_height, dpToPx(10));
            elevation = ta.getDimensionPixelSize(R.styleable.CustomSpinner_cs_elevation, dpToPx(0));
            listHeight = ta.getDimensionPixelSize(R.styleable.CustomSpinner_cs_list_height, dpToPx(400));
            arrowDrawable = ta.getDrawable(R.styleable.CustomSpinner_cs_arrow);
        } finally {
            ta.recycle();
        }

        if (!hideArrow) {
            if (arrowDrawable == null)
                arrowDrawable = getResources().getDrawable(R.drawable.ms__arrow);
            arrowDrawable.setBounds(0, 0, width, height);
            setCompoundDrawables(null, null, arrowDrawable, null);
        }

        setClickable(true);
        setLines(1);

        listView = new CustomListView(context);
        listView.setId(getId());
        listView.setDivider(null);
        listView.setItemsCanFocus(true);
        listView.setVerticalScrollBarEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;
                Object item = adapter.getItem(position);
                setText(item.toString());
                collapse();
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(CustomSpinner.this, position, id, item);
                }
            }
        });

        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(listView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.setFocusable(true);
        popupWindow.setElevation(elevation);
        if (bg != null)
            popupWindow.setBackgroundDrawable(bg);
        else
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_bg));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (!hideArrow) {
                    animateArrow(false);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        popupWindow.setWidth(MeasureSpec.getSize(widthMeasureSpec));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 将 dp 转成 px
     */
    private static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isEnabled() && isClickable()) {
                if (!popupWindow.isShowing()) {
                    expand();
                } else {
                    collapse();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("state", super.onSaveInstanceState());
        bundle.putInt("selected_index", selectedIndex);
        if (popupWindow != null) {
            bundle.putBoolean("is_popup_showing", popupWindow.isShowing());
            collapse();
        } else {
            bundle.putBoolean("is_popup_showing", false);
        }
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof Bundle) {
            Bundle bundle = (Bundle) savedState;
            selectedIndex = bundle.getInt("selected_index");
            if (adapter != null) {
                setText((String) adapter.getItem(selectedIndex));
            }
            if (bundle.getBoolean("is_popup_showing")) {
                if (popupWindow != null) {
                    // Post the show request into the looper to avoid bad token exception
                    post(new Runnable() {

                        @Override
                        public void run() {
                            expand();
                        }
                    });
                }
            }
            savedState = bundle.getParcelable("state");
        }
        super.onRestoreInstanceState(savedState);
    }

    /**
     * @return the selected item position
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Set the default spinner item using its index
     *
     * @param position the item's position
     */
    public void setSelectedIndex(int position) {
        if (adapter != null) {
            if (position >= 0 && position <= adapter.getCount()) {
                selectedIndex = position;
                setText((String) adapter.getItem(position));
            } else {
                throw new IllegalArgumentException("Position must be lower than adapter count!");
            }
        }
    }

    /**
     * Register a callback to be invoked when an item in the dropdown is selected.
     *
     * @param onItemSelectedListener The callback that will run
     */
    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setAnimationStyle(int anim) {
        popupWindow.setAnimationStyle(anim);
    }

    /**
     * Set the dropdown items
     *
     * @param items A list of items
     * @param <T>   The item type
     */
    public <T> void setItems(@NonNull List<T> items) {
        adapter = new MaterialSpinnerAdapter<>(getContext(), items);
        listView.setAdapter(adapter);
    }

    public void enableArrow(boolean value) {
        hideArrow = value;
        if (hideArrow) {
            if (arrowDrawable == null)
                arrowDrawable = getResources().getDrawable(R.drawable.ms__arrow);
            arrowDrawable.setBounds(0, 0, width, height);
        } else {
            arrowDrawable = null;
        }
        setCompoundDrawables(null, null, arrowDrawable, null);
    }

    public void setAdapter(ListAdapter adapter) {
        this.adapter = adapter;
        listView.setAdapter(adapter);
    }

    /**
     * Show the dropdown menu
     */
    public void expand() {
        if (!hideArrow) {
            animateArrow(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow.setOverlapAnchor(false);
            popupWindow.showAsDropDown(this);
        } else {
            int[] location = new int[2];
            getLocationOnScreen(location);
            int x = location[0];
            int y = getHeight() + location[1];
            popupWindow.showAtLocation(this, Gravity.TOP | Gravity.START, x, y);
        }
    }

    /**
     * Closes the dropdown menu
     */
    public void collapse() {
        if (!hideArrow) {
            animateArrow(false);
        }
        popupWindow.dismiss();
    }

    private void animateArrow(boolean shouldRotateUp) {
        int start = shouldRotateUp ? 0 : 10000;
        int end = shouldRotateUp ? 10000 : 0;
        ObjectAnimator animator = ObjectAnimator.ofInt(arrowDrawable, "level", start, end);
        animator.start();
    }

    /**
     * Interface definition for a callback to be invoked when an item in this view has been selected.
     *
     * @param <T> Adapter item type
     */
    public interface OnItemSelectedListener<T> {

        /**
         * <p>Callback method to be invoked when an item in this view has been selected. This callback is invoked only when
         * the newly selected position is different from the previously selected position or if there was no selected
         * item.</p>
         *
         * @param view     The {@link CustomSpinner} view
         * @param position The position of the view in the adapter
         * @param id       The row id of the item that is selected
         * @param item     The selected item
         */
        void onItemSelected(CustomSpinner view, int position, long id, T item);

    }

    private class CustomListView extends ListView {

        public CustomListView(Context context) {
            this(context, null);
        }

        public CustomListView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int size = MeasureSpec.getSize(heightMeasureSpec);
            if (size > listHeight) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(listHeight, MeasureSpec.AT_MOST);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
