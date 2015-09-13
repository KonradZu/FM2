package com.akobets.fm2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by akobets on 12.09.2015.
 */
public class FileListAdapter extends ArrayAdapter<File> {

    public static final String LEVEL_UP = "00000000000000000000.abc";
    private Context context;
    private int resource;
    private ArrayList<File> fileList;


    public FileListAdapter(Context context, int resource, ArrayList<File> fileList) {
        super(context, resource, fileList);
        this.context = context;
        this.resource = resource;
        this.fileList = fileList;
    }

    @Override
    public File getItem(int position) {
        return fileList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("MyLog", "resource = " + resource);
        Log.d("MyLog", "R.layout.row_file_manager = " + R.layout.row_file_manager);
        View view = inflater.inflate(resource, parent, false);

        // Связываем поля и графические элементы
        ImageView ivFileIcon = (ImageView) view.findViewById(R.id.ivFileIcon);
        TextView tvFileName = (TextView) view.findViewById(R.id.tvFileName);

        // получаем объект из списка объектов
        File currentFile = fileList.get(position);
        Log.d("MyLog", "currentFile = " + currentFile.getName());

        // устанавливаем значения компонентам одного элемента списка
        if (currentFile.getName() == LEVEL_UP) {
            ivFileIcon.setImageResource(R.drawable.level_up);
            tvFileName.setText(currentFile.getParent());
        } else {
            if (currentFile.isDirectory()) {
                ivFileIcon.setImageResource(R.drawable.folder);
            } else {
                ivFileIcon.setImageResource(R.drawable.file);
            }
            tvFileName.setText(currentFile.getName());
        }


        return view;
    }


}
