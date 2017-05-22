package spinner.simple;

import android.app.Activity;
import android.os.Bundle;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import view.customspinner.NiceSpinner;

/**
 * Author Administrator
 * on 2017/5/22.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        niceSpinner.attachDataSource(R.layout.child, dataset);
    }
}
