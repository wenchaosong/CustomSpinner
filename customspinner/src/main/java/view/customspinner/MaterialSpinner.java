package view.customspinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MaterialSpinner extends RelativeLayout implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView mText;
    private ImageView mArrow;
    private Context mContext;
    private ListPopupWindow popupWindow;
    private SimpleAdapter adapter;
    private Animation mAnimation;
    private Animation mResetAnimation;

    public MaterialSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(attrs);
        initAnimation();
    }

    public void setItemData(List<String> data) {
        adapter = new SimpleAdapter(mContext, data);
        setBaseAdapter(adapter);
    }

    private void initAnimation() {
        mAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setDuration(200);
        mAnimation.setFillAfter(true);
        mResetAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mResetAnimation.setDuration(200);
        mResetAnimation.setFillAfter(true);
    }

    private void initView(AttributeSet attrs) {
        LayoutInflater.from(mContext).inflate(R.layout.spinner_layout, this);
        mText = (TextView) findViewById(R.id.sipnner_text);
        mArrow = (ImageView) findViewById(R.id.spinner_arrow);
        mArrow.setOnClickListener(this);
        mArrow.setRotation(0);
        TypedArray tArray = mContext.obtainStyledAttributes(attrs,
                R.styleable.EditSpinner);
        mText.setHint(tArray.getString(R.styleable.EditSpinner_es_hint));
        int hintColor = tArray.getColor(R.styleable.EditSpinner_es_hintColor, Color.GRAY);
        mText.setHintTextColor(hintColor);
        int textColor = tArray.getColor(R.styleable.EditSpinner_es_textColor, Color.BLACK);
        mText.setTextColor(textColor);

        int dimension = tArray.getDimensionPixelSize(R.styleable.EditSpinner_es_arrowSize, dpToPx(12));
        ViewGroup.LayoutParams params = mArrow.getLayoutParams();
        params.width = dimension;
        params.height = dimension;
        mArrow.setLayoutParams(params);

        tArray.recycle();
    }

    private void setBaseAdapter(BaseAdapter adapter) {
        if (popupWindow == null) {
            initPopupWindow();
        }
        popupWindow.setAdapter(adapter);
    }

    private void initPopupWindow() {
        popupWindow = new ListPopupWindow(mContext) {

            @Override
            public void dismiss() {
                super.dismiss();
                mArrow.startAnimation(mResetAnimation);
            }

            @Override
            public void show() {
                super.show();
                mArrow.startAnimation(mAnimation);
            }
        };
        popupWindow.setOnItemClickListener(this);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnchorView(mText);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (adapter == null || popupWindow == null) {
            return;
        }
        if (v.getId() == R.id.spinner_arrow)
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else {
                popupWindow.show();
            }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mText.setText(((SimpleAdapter) parent.getAdapter()).getItem(position));
        popupWindow.dismiss();
    }

    private int dpToPx(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(px);
    }
}