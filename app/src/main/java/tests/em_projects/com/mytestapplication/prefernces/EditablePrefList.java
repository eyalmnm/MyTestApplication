package tests.em_projects.com.mytestapplication.prefernces;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

import java.util.ArrayList;
import java.util.Arrays;

import tests.em_projects.com.mytestapplication.R;

// Ref: http://developer.android.com/guide/topics/ui/settings.html
// Ref: http://stackoverflow.com/questions/6136770/android-dynamic-array-listpreference

/**
 * Created by eyal on 27/04/16.
 */
public class EditablePrefList extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.editable_pref_list);
        ListPreference listPreferenceCategory = (ListPreference) findPreference("default_category");
        if (listPreferenceCategory != null) {
            ArrayList<String> categoryList = getCategories();
            CharSequence entries[] = new String[categoryList.size()];
            CharSequence entryValues[] = new String[categoryList.size()];
            int i = 0;
            for (String category : categoryList) {
                entries[i] = category;
                entryValues[i] = Integer.toString(i);
                i++;
            }
            listPreferenceCategory.setEntries(entries);
            listPreferenceCategory.setEntryValues(entryValues);
        }
    }

    private ArrayList<String> getCategories() {
        String[] strings = {"one", "two", "three", "four"};
        ArrayList ret = new ArrayList<String>(Arrays.asList(strings));
        return ret;
    }
}
