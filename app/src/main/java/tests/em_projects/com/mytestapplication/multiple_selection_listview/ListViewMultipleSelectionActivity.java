package tests.em_projects.com.mytestapplication.multiple_selection_listview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyalmuchtar on 06/10/2017.
 */

public class ListViewMultipleSelectionActivity extends Activity implements View.OnClickListener {

    Button button;
    ListView listView;
    //ArrayAdapter<String> adapter;
    MyAdapter adapter;
    String[] sports;

    Context context;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_selection_listview);
        context = this;

        findViewsById();

        sports = getResources().getStringArray(R.array.sports_array);
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, sports);
        adapter = new MyAdapter();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        button.setOnClickListener(this);
    }

    private void findViewsById() {
        listView = (ListView) findViewById(R.id.list);
        button = (Button) findViewById(R.id.testbutton);
    }

    public void onClick(View v) {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
                selectedItems.add((String) adapter.getItem(position));
        }

        String[] outputStrArr = new String[selectedItems.size()];

        for (int i = 0; i < selectedItems.size(); i++) {
            outputStrArr[i] = selectedItems.get(i);
        }

        Intent intent = new Intent(this,
                ListViewMultipleSelectionResultActivity.class);

        // Create a bundle object
        Bundle b = new Bundle();
        b.putStringArray("selectedItems", outputStrArr);

        // Add the bundle to the intent.
        intent.putExtras(b);

        // start the ResultActivity
        startActivity(intent);
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sports.length;
        }

        @Override
        public Object getItem(int position) {
            return sports[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.activity_multiple_selection_listview_item, null);
            }
            TextView title = (TextView) convertView.findViewById(R.id.title);
            title.setText(sports[position]);
            return convertView;
        }
    }
}
