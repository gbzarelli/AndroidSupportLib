package br.com.helpdev.supportlib.file_selector;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import br.com.helpdev.mapaalerta.R;
import br.com.helpdev.supportlib.file_selector.adapter.FileSelectorAdapter;

/**
 * Created by Guilherme Biff Zarelli on 16/11/16.
 */

public class FileSelectorActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * .add("gpx").add("jpg").add("png");
     */
    public static String PARAM_IT_FILE_TYPES_ARRAYLIST = "PARAM_IT_FILE_TYPES_ARRAYLIST";
    private ListView listView;
    private File base;
    private ArrayList<String> extensionFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selector);

        listView = (ListView) findViewById(R.id.list_view);
        if (savedInstanceState == null) {
            extensionFilter = getIntent().getStringArrayListExtra(PARAM_IT_FILE_TYPES_ARRAYLIST);
            base = Environment.getExternalStorageDirectory();
        }
        loadAdapter();
    }


    private void loadAdapter() {
        FileSelectorAdapter fileSelectorAdapter = new FileSelectorAdapter(this, base, extensionFilter,null);
        listView.setAdapter(fileSelectorAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
