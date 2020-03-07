package tests.em_projects.com.mytestapplication.gallery;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tests.em_projects.com.mytestapplication.BuildConfig;
import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.config.Constants;
import tests.em_projects.com.mytestapplication.utils.DimenUtils;
import tests.em_projects.com.mytestapplication.utils.FileUtils;

/**
 * Created by eyalmuchtar on 11/22/17.
 */

public class ImageGalleryActivity extends Activity implements View.OnClickListener,
        DeleteItemsDialog.OnDeleteConfirmListener, ShowCameraDialog.ShowCameraDialogListener {
    private static final String TAG = "ImageGallery";
    // Camera Properties
    private static final int OPEN_CAMERA_REQUEST_CODE = 1234;
    private Context context;
    // Directory management components
    private String currentDirectoryPath;
    private String fSa = File.separator;
    private boolean selectableMode = false;
    private ArrayList<ImageGalleryFile> galleryFiles;
    private GridView gridview;
    private ImagesGridViewAdapter adapter;
    private TextView titleTextView;
    private ImageView noImagesImageView;
    private ImageButton cameraButton;
    private ImageButton saveButton;
    private ImageButton cancelButton;
    private ImageButton deleteButton;
    private ImageLoader imageLoader;
    // Navigation properties
    private String currentRecordId;
    private String subDirectory;
    private boolean isFromRecord;
    private boolean dialogIsShown;
    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (false == selectableMode) {
                selectableMode = true;
                titleTextView.setText(getString(R.string.selected_files, 0));
                adapter.notifyDataSetChanged();
            }
            return false;
        }
    };
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (true == selectableMode) {
                int selectedIndex = adapter.selectedPositions.indexOf(position);
                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView) view).display(false);
                } else {
                    adapter.selectedPositions.add(position);
                    ((GridItemView) view).display(true);
                }
                titleTextView.setText(getString(R.string.selected_files, adapter.selectedPositions.size()));
                adapter.notifyDataSetChanged();
            } else {
                if (true == ((GridItemView) view).isDirectory()) {
                    currentDirectoryPath += fSa + ((GridItemView) view).getFileName();
                    loadBitMap(currentDirectoryPath);
                } else {
                    FragmentManager fm = getFragmentManager();
                    ShowImageDialog dialog = new ShowImageDialog();
                    Bundle args = new Bundle();
                    args.putString("data", ((GridItemView) view).getFullPath());
                    dialog.setArguments(args);
                    dialog.show(fm, "ShowImageDialog");
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_image_gallery);
        context = this;

        Intent intent = getIntent();
        if (true == intent.hasExtra("data")) {
            currentRecordId = intent.getStringExtra("data");
            isFromRecord = intent.getBooleanExtra("origin", false);
            Log.e(TAG, "OnCreate - DO NOT FORGET TO USE CURRENT RECORD ID FOR THE CAMERA DIALOG AND THE ORIGIN IF FROM RECORD"); // TODO remove
            currentRecordId = "omdan/1000"; // TODO remove
        } else {
            throw new NullPointerException("PLEASE DO NOT FORGET TO SEND THE CURRENT RECORD ID AND THE ORIGIN IF FROM RECORD");
        }

        titleTextView = findViewById(R.id.titleTextView);
        gridview = findViewById(R.id.gridview);
        noImagesImageView = findViewById(R.id.noImagesImageView);

        initButtons();

        currentDirectoryPath = "";
        initImagesGrid();
    }

    private void initButtons() {
        cameraButton = findViewById(R.id.cameraButton);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        deleteButton = findViewById(R.id.deleteButton);

        cameraButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    private void initImagesGrid() {
        galleryFiles = new ArrayList<>();
        adapter = new ImagesGridViewAdapter();
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(itemClickListener);
        gridview.setOnItemLongClickListener(longClickListener);
        if (true == isFromRecord) {
            cameraButton.performClick();
            titleTextView.setText(currentRecordId);
        } else {
            loadBitMap(currentDirectoryPath);
        }
    }

    private void loadBitMap(String currentDirectoryPath) {
        this.currentDirectoryPath = currentDirectoryPath;
        if (0 < galleryFiles.size()) {
            galleryFiles.clear();
            selectableMode = false;
            System.gc();
            if (null != adapter) {
                adapter.resetSelectedPositions();
            }
        }
        titleTextView.setText(currentDirectoryPath);
        imageLoader = new ImageLoader();
        imageLoader.execute(Constants.BASE_PATH + File.separator + currentDirectoryPath);
    }

    private void updateGrid() {
        if (0 < galleryFiles.size()) {
            noImagesImageView.setVisibility(View.INVISIBLE);
            gridview.setVisibility(View.VISIBLE);
        } else {
            noImagesImageView.setVisibility(View.VISIBLE);
            gridview.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cameraButton:
                if (false == dialogIsShown) {
                    showCameraDialog();
                } else {
                    openCameraApi();
                }
                break;
            case R.id.saveButton:
                showSaveDialog();
                break;
            case R.id.cancelButton:
                onBackPressed();
                break;
            case R.id.deleteButton:
                showDeleteDialog();
                break;
        }
    }

    private void showCameraDialog() {
        Log.d(TAG, "showCameraDialog");
        FragmentManager fm = getFragmentManager();
        ShowCameraDialog dialog = new ShowCameraDialog();
        Bundle args = new Bundle();
        args.putString("data", currentRecordId);
        args.putStringArrayList("additions", getSubRecords(currentRecordId));
        dialog.setArguments(args);
        dialog.show(fm, "ShowCameraDialog");
    }

    private ArrayList<String> getSubRecords(String recordId) {
        String path = Constants.BASE_PATH + File.separator + recordId;
        ArrayList<String> allSubs = new ArrayList<>();
        allSubs.add(getResources().getString(R.string.root));
        allSubs.addAll(FileUtils.getDirectories(path));
        return allSubs;
    }

    public void openCameraApi() {
        Log.d(TAG, "openCamera");
        Intent intent = new Intent(this, ApiCameraActivity.class);
        intent.putExtra("recordId", currentRecordId);
        intent.putExtra("subDir", subDirectory);
        startActivityForResult(intent, OPEN_CAMERA_REQUEST_CODE);
    }

    private void showSaveDialog() {
        // TODO Missing implementation
    }

    private void showDeleteDialog() {
        List selectedItems = adapter.getSelectedItems();
        if (selectedItems.size() > 0) {
            DeleteItemsDialog dialog = new DeleteItemsDialog();
            FragmentManager fm = getFragmentManager();
            Bundle args = new Bundle();
            args.putInt("data", selectedItems.size());
            dialog.setArguments(args);
            dialog.show(fm, "showDeleteDialog");
        }
    }

    // Implementation of DeleteItemsDialog.OnDeleteConfirmListener
    @Override
    public void OnDeleteConfirm(boolean confirm) {
        if (true == confirm) {
            List selectedItems = adapter.getSelectedItems();
            if (selectedItems.size() > 0) {
                ArrayList<String> toRemove = new ArrayList<>();
                for (int i = 0; i < selectedItems.size(); i++) {
                    int position = (int) selectedItems.get(i);
                    toRemove.add(galleryFiles.get(position).toString());
                    FileUtils.removeFile(galleryFiles.get(position).getFullPath());
                }
                loadBitMap(currentDirectoryPath);
            }
        }
    }

    // Implementation of ShowCameraDialog.ShowCameraDialogListener
    @Override
    public void openCamera(String recordId, String subRecord) {
        titleTextView.setText(getString(R.string.record_id) + " " + subRecord);
        if (getString(R.string.root).equalsIgnoreCase(subRecord)) {
            subDirectory = "";
        } else {
            subDirectory = subRecord;
        }
        loadBitMap(recordId + fSa + subDirectory);
        dialogIsShown = true;
        cameraButton.performClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        if (OPEN_CAMERA_REQUEST_CODE == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                Bundle extras = data.getExtras();
                boolean newBitMaps = extras.getBoolean("bitmaps");
                if (true == newBitMaps) {
                    loadBitMap(currentRecordId + fSa + subDirectory);
                }
            } else {
                // TODO Do something
            }
        }
    }

    @Override
    public void onBackPressed() {
        if ("".equalsIgnoreCase(currentDirectoryPath)) {
            super.onBackPressed();
        } else {
            int lastIndex = currentDirectoryPath.lastIndexOf(fSa);
            if (0 < lastIndex) {
                currentDirectoryPath = currentDirectoryPath.substring(0, lastIndex);
            } else {
                currentDirectoryPath = "";
            }
            loadBitMap(currentDirectoryPath);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != imageLoader) {
            imageLoader.cancel(true);
        }
    }


    private class ImagesGridViewAdapter extends BaseAdapter {

        private List selectedPositions;

        public ImagesGridViewAdapter() {
            selectedPositions = new ArrayList();
        }

        public List getSelectedItems() {
            return selectedPositions;
        }

        public void resetSelectedPositions() {
            selectedPositions.clear();
        }

        @Override
        public int getCount() {
            return galleryFiles.size();
        }

        @Override
        public Object getItem(int i) {
            return galleryFiles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            GridItemView customView = new GridItemView(context);

            String fullPath = galleryFiles.get(position).getFullPath();
            String fileName = galleryFiles.get(position).getFileName();
            boolean isDir = new File(fullPath).isDirectory();
//            if (true == BuildConfig.DEBUG) {
//                Log.d(TAG, "GetView path: " + fullPath + " file name: " + fileName + " isDir: " + String.valueOf(isDir));
//            }
            customView.display(fullPath, fileName, isDir, selectableMode, selectedPositions.contains(position));

            // Set height and width constraints for the image view
            int cellHeight = (int) DimenUtils.dpToPx(100);
            int cellWidth = (int) DimenUtils.dpToPx(100);
            customView.setLayoutParams(new AbsListView.LayoutParams(cellWidth, cellHeight));

            return customView;
        }
    }

    private class ImageLoader extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, R.string.loading, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            ArrayList<ImageGalleryFile> bitmapHolders = new ArrayList<>();
            File directory = new File(strings[0]);
            boolean resulst = false;
            if (false == directory.exists()) {
                resulst = directory.mkdirs();
                if (false == resulst) {
                    Log.e(TAG, "Directory " + directory + " can not be created!!!!");
                    return null;
                }
            }
            if (false == directory.exists() || false == directory.isDirectory()) {
                return null;
            }
            File[] files = directory.listFiles();
            if (null == files) {
                return null;
            }
            int size = (int) DimenUtils.dpToPx(200);
            for (int i = 0; i < files.length; i++) {
                if (true == BuildConfig.DEBUG) {
                    Log.d(TAG, "doInBackground path: " + directory.getAbsolutePath() + " file name: [" + files[i].getName() + "]");
                }
                if (true == FileUtils.isImageFile(files[i]) || files[i].isDirectory()) {
                    try {
                        bitmapHolders.add(new ImageGalleryFile(files[i].getAbsolutePath(), files[i].getName(), files[i].isDirectory()));
                    } catch (Exception e) {
                        Log.e(TAG, "doInBackground", e);
                    }
                }
                publishProgress(i + "/" + files.length);
            }
            galleryFiles = bitmapHolders;
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //Toast.makeText(context, getString(R.string.loading) + " " + values[0], Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Loading " + values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
            updateGrid();
            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    gridview.clearChoices();
                    adapter.resetSelectedPositions();
                }
            }, 100);
        }
    }
}
