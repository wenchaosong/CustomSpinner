package spinner.simple;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import lib.spinner.CustomSpinner;

/**
 * Author Administrator
 * on 2017/5/22.
 */
public class MainActivity extends Activity {

    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        list.add("选项一");
        list.add("选项二选项二选项二选项二选项二选项二选项二");
        list.add("选项三");
        list.add("选项四");
        list.add("选项二选项二选项二选项二选项二选项二选项二");
        list.add("选项三");
        list.add("选项四");
        CustomSpinner mEditSpinner = (CustomSpinner) findViewById(R.id.edit_spinner);
        mEditSpinner.setItems(list);
    }
}
