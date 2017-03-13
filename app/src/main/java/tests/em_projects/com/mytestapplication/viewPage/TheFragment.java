package tests.em_projects.com.mytestapplication.viewPage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyal on 26/04/16.
 */

// Ref: http://stackoverflow.com/questions/10024739/how-to-determine-when-fragment-becomes-visible-in-viewpager
public class TheFragment extends Fragment {

    private static final String TAG = "TheFragment";

    public static final String TITLE = "TITLE";
    public static final String NUMBER = "NUMBER";

    private TextView labelTextView;

    private String title;
    private int pageNumber;
    private static int counter;

    private ViewPagerActivity viewPagerActivity;

    @Override
    public void onAttach(Activity activity) {
        try {
            viewPagerActivity = (ViewPagerActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage());
        }
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated (" + pageNumber + ")");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(TheFragment.TITLE);
        pageNumber = getArguments().getInt(TheFragment.NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView (" + pageNumber + ") (counter: " + counter + ")");
        View view = inflater.inflate(R.layout.fragment_viewpage, container, false);
        labelTextView = (TextView) view.findViewById(R.id.labelTextView);
        labelTextView.setText(title + " " + pageNumber + " - " + counter);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d(TAG, pageNumber + " Is Visible To User viewPagerActivity " + (viewPagerActivity == null ? "null" : "not null"));
            registerAsListener();
        } else {
//            Log.d(TAG, pageNumber + " Is Not Visible To User viewPagerActivity " + (viewPagerActivity == null ? "null" : "not null"));
//            unregisterAsListener();
        }
    }

//    private void unregisterAsListener() {
//        if (viewPagerActivity != null) {
//            viewPagerActivity.removeListener(this);
//        }
//    }

    private void registerAsListener() {
        Log.d(TAG, "registerAsListener 1");
        if (viewPagerActivity != null) {
            counter = viewPagerActivity.addListener(this);
        }
        Log.d(TAG, "registerAsListener 2 (" + counter +")");
    }

    public void updateLabel(int actCounter) {
//        Log.d(TAG, "updateLabel actCounter: " + actCounter);
        counter = actCounter;
        if (labelTextView != null) {
            labelTextView.setText(title + " " + pageNumber + " - " + counter);
        }
    }
}
