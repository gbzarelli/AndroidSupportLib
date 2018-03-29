package br.com.helpdev.supportlib.io.files;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import br.com.helpdev.supportlib.R;

/**
 * Created by Guilherme Biff Zarelli on 16/11/16.
 */

public class FileSelectorAdapter extends BaseAdapter implements View.OnClickListener {

    public interface FileSelectorCallback {
        void baseChange(File file);

        void fileSelected(File file);
    }

    private File baseDir;
    private File[] files;
    private ArrayList<String> extensionFilter;
    private Context context;
    private FileSelectorCallback fileSelectorCallback;

    public FileSelectorAdapter(Context context, File baseDir, ArrayList<String> extensionFilter, FileSelectorCallback fileSelectorCallback) {
        this.baseDir = baseDir;
        this.context = context;
        this.extensionFilter = extensionFilter;
        this.fileSelectorCallback = fileSelectorCallback;
        files = getListFiles(baseDir);
    }

    @Override
    public int getCount() {
        if (baseDir.getParentFile() != null) {
            return files.length + 1;
        }
        return files.length;
    }

    private File[] getListFiles(File dir) {
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File dir1) {
                if (dir1.isDirectory() || extensionFilter == null || extensionFilter.isEmpty())
                    return true;

                for (String ext : extensionFilter) {
                    return dir1.getName().toLowerCase().endsWith(ext);
                }
                return false;
            }
        });
        if (files == null) {
            files = new File[0];
        }
        return files;
    }

    public File getBaseDir() {
        return baseDir;
    }

    @Override
    public File getItem(int position) {
        if (position < 0) return null;
        if (baseDir.getParentFile() != null && position == 0) {
            return baseDir.getParentFile();
        } else if (baseDir.getParentFile() != null && position > 0) {
            return files[position - 1];
        }
        return files[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_file_selector, null);
        TextView tv = (TextView) view.findViewById(R.id.texto);
        ImageView imv = (ImageView) view.findViewById(R.id.icone);

        File item = getItem(position);
        if (baseDir.getParentFile() != null && position == 0) {
            tv.setText("../");
        } else {
            tv.setText(item.getName() + (item.isDirectory() ? "/" : ""));
        }

        if (item.isDirectory()) {
            imv.setImageResource(R.drawable.ic_folder_open_black_36dp);
        } else {
            imv.setImageResource(R.drawable.ic_insert_drive_file_black_36dp);
        }

        view.setOnClickListener(this);
        view.setTag(position);
        return view;
    }

    @Override
    public void onClick(View v) {
        Integer tag = (Integer) v.getTag();
        File item = getItem(tag);
        if (item.isDirectory()) {
            baseDir = item;
            if (fileSelectorCallback != null)
                fileSelectorCallback.baseChange(baseDir);
            files = getListFiles(baseDir);

            notifyDataSetChanged();
        } else {
            if (fileSelectorCallback != null)
                fileSelectorCallback.fileSelected(item);
        }
    }
}
