package tests.em_projects.com.mytestapplication.gallery;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import java.io.File;

import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.utils.DimenUtils;
import tests.em_projects.com.mytestapplication.utils.FileUtils;

/**
 * Created by eyalmuchtar on 11/10/2017.
 */

public class ShowImageDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "ShowImageDialog";

    private TouchImageView touchImageView;
    private ImageButton closeImageButton;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_show_image, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        touchImageView = view.findViewById(R.id.touchImageView);
        closeImageButton = view.findViewById(R.id.closeImageButton);
        closeImageButton.setEnabled(false);

        context = getActivity();
        closeImageButton.setOnClickListener(this);

        Bundle args = getArguments();
        String imagePath = args.getString("data");

        new ImageLoader().execute(imagePath);
    }

    @Override
    public void onClick(View view) {
        if (R.id.closeImageButton == view.getId()) {
            dismiss();
        }
    }

    private class ImageLoader extends AsyncTask<String, Void, Bitmap> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (null != dialog) {
                dialog.dismiss();
            }
            dialog = ProgressDialog.show(context, "", "טוען...", true, false);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            File file = new File(strings[0]);
            int size = (int) DimenUtils.dpToPx(200);
            Bitmap bitmap = null;
            if (true == FileUtils.isImageFile(file)) {
                try {
                    bitmap = FileUtils.decodeFile(file, size);
                } catch (Exception e) {
                    Log.e(TAG, "doInBackground", e);
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            touchImageView.setImageBitmap(bitmap);
            touchImageView.setMaxZoom(4f);
            if (null != dialog) {
                dialog.dismiss();
                dialog = null;
            }
            closeImageButton.setEnabled(true);
        }

    }

}
