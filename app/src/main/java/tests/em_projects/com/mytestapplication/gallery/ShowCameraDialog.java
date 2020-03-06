package tests.em_projects.com.mytestapplication.gallery;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.utils.DimenUtils;
import tests.em_projects.com.mytestapplication.utils.StringUtils;

/**
 * Created by eyalmuchtar on 05/10/2017.
 */

public class ShowCameraDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "showCameraDialog";
    private ShowCameraDialogListener listener;
    private AutoCompleteTextView recordIdAutoComplete;
    private Button okButton;
    //    private Button cancelButton;
    private String currentRecId;
    private String[] subDirectoriesName;

    @Override
    public void onAttach(Activity activity) {
        listener = (ShowCameraDialogListener) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_show_camera, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setCancelable(false);

        recordIdAutoComplete = view.findViewById(R.id.recordIdAutoComplete);
        okButton = view.findViewById(R.id.okButton);
//        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        currentRecId = getArguments().getString("data");
        ArrayList<String> theSubs = getArguments().getStringArrayList("additions");
        subDirectoriesName = StringUtils.arrayList2StringArray(theSubs);

        recordIdAutoComplete.setDropDownBackgroundResource(R.color.windowBackground);
        recordIdAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    okButton.performClick();
                    return true;
                }
                return false;
            }
        });
        recordIdAutoComplete.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.layout_category_spinner_item, subDirectoriesName));
        recordIdAutoComplete.setThreshold(1);
        recordIdAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordIdAutoComplete.showDropDown();
                recordIdAutoComplete.setDropDownHeight(DimenUtils.dpToPx(120));
            }
        });

        okButton.setOnClickListener(this);
//        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        dismiss();
        if (R.id.okButton == view.getId()) {
            String subRecord = recordIdAutoComplete.getText().toString();
            listener.openCamera(currentRecId, subRecord);
        } else if (R.id.cancelButton == view.getId()) {
            String subRecord = recordIdAutoComplete.getText().toString();
            listener.openCamera(currentRecId, subRecord);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface ShowCameraDialogListener {
        void openCamera(String recordId, String subRecord);
    }
}
