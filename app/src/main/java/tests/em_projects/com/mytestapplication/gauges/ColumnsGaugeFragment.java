package tests.em_projects.com.mytestapplication.gauges;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 6/27/16.
 */

public class ColumnsGaugeFragment extends Fragment {

    private static final String TAG = "ColumnsGaugeFragment";

    private ColumnsGaugeView theGauge;

    // Data Holders
    private ArrayList<Float> data;
    private ArrayList<String> titles;
    private int numberOfColumnsInGroup;
    private int[] groupColumnsColors;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get the LayoutView and a reference to the gauge
        View view = inflater.inflate(R.layout.fragment_columns_gauge, null, false);
        theGauge = view.findViewById(R.id.theGauge);

        // Set Data
        try {
            theGauge.setData(data, titles, numberOfColumnsInGroup, groupColumnsColors);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView", e);
        }
        return view;
    }

    public void setData(ArrayList<Float> data, ArrayList<String> titles, int numberOfColumnsInGroup, int[] groupColumnsColors) throws Exception {
        this.data = data;
        this.titles = titles;
        this.numberOfColumnsInGroup = numberOfColumnsInGroup;
        this.groupColumnsColors = groupColumnsColors;
        if (null != theGauge) {
            theGauge.setData(data, titles, numberOfColumnsInGroup, groupColumnsColors);
        }
    }
}
