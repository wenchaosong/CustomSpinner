package spinner.simple;

import android.app.Activity;
import android.os.Bundle;

import view.customspinner.MaterialSpinner;

/**
 * Author Administrator
 * on 2017/5/22.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        String[] ITEMS = {"Item 11111111111111", "Item 2", "Item 3", "Item 4"};
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setData(this, R.layout.spinner_item, ITEMS);
    }
}
