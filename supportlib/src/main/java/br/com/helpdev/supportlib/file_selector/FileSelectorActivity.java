package br.com.helpdev.supportlib.file_selector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import br.com.helpdev.supportlib.R;
import br.com.helpdev.supportlib.file_selector.adapter.FileSelectorAdapter;

/**
 * Intent intent = new Intent(MainActivity.this, FileSelectorActivity.class);
 * ArrayList<String> lista = new ArrayList<String>();
 * lista.add("jpg");
 * lista.add("png");
 * intent.putStringArrayListExtra(FileSelectorActivity.PARAM_IT_FILE_TYPES_ARRAYLIST,lista);
 * startActivity(intent);
 * <p>
 * <color name="colorPrimary">#3F51B5</color>
 * <color name="colorPrimaryDark">#303F9F</color>
 * <color name="colorAccent">#FF4081</color>
 * </p>
 * <p>
 * Created by Guilherme Biff Zarelli on 16/11/16.
 */
public class FileSelectorActivity extends AppCompatActivity implements FileSelectorAdapter.FileSelectorCallback {

    private static final int MY_PERMISSIONS_EXTERNAL_STORAGE = 1;
    public static final String PARAM_IT_FILE_RESPONSE = "PARAM_IT_FILE_RESPONSE";
    /**
     * .add("gpx").add("jpg").add("png");
     */
    public static final String PARAM_IT_FILE_TYPES_ARRAYLIST = "PARAM_IT_FILE_TYPES_ARRAYLIST";
    /**
     * /a/a/a/
     */
    public static final String PARAM_IT_FILE_HOME = "PARAM_IT_FILE_HOME";

    private ListView listView;
    private File base;
    private File home;
    private ArrayList<String> extensionFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selector);

        checkPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_file_selector, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            base = home;
            loadAdapter();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            load();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_EXTERNAL_STORAGE);
            }
        }
    }

    private void load() {
        listView = (ListView) findViewById(R.id.list_view);
        extensionFilter = getIntent().getStringArrayListExtra(PARAM_IT_FILE_TYPES_ARRAYLIST);
        String stringHome = getIntent().getStringExtra(PARAM_IT_FILE_HOME);
        if (stringHome != null) {
            try {
                home = new File(stringHome);
            } catch (Throwable t) {
                t.printStackTrace();
                stringHome = null;
            }
        }
        if (stringHome == null) {
            home = Environment.getExternalStorageDirectory();
        }
        base = home;
        loadAdapter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            load();
        } else {
            checkPermission();
        }
    }

    private void loadAdapter() {
        FileSelectorAdapter fileSelectorAdapter = new FileSelectorAdapter(this, base, extensionFilter, this);
        listView.setAdapter(fileSelectorAdapter);
    }


    @Override
    public void baseChange(File file) {
        this.base = file;
    }

    @Override
    public void fileSelected(File file) {
        Intent data = new Intent();
        data.putExtra(PARAM_IT_FILE_RESPONSE, file.getAbsolutePath());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (base.getParentFile() == null || base.getAbsoluteFile().equals(Environment.getExternalStorageDirectory().getAbsoluteFile())) {
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        } else {
            base = base.getParentFile();
            loadAdapter();
        }
    }
}
