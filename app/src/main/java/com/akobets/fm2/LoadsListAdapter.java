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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by akobets on 13.09.2015.
 */
public class LoadsListAdapter extends ArrayAdapter<File> {


    public static final String LEVEL_UP = "00000000000000000000.abc";
    private Context context;
    private int resource;
    private ArrayList<File> fileList;


    public LoadsListAdapter(Context context, int resource, ArrayList<File> fileList) {
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
//        Log.d("MyLog", "resource = " + resource);
//        Log.d("MyLog", "R.layout.row_file_manager = " + R.layout.row_file_manager);
        View view = inflater.inflate(resource, parent, false);

        // Связываем поля и графические элементы
        ImageView ivIconFileType = (ImageView) view.findViewById(R.id.ivIconFileType);
        TextView tvLoadName = (TextView) view.findViewById(R.id.tvLoadName);
        TextView tvLoadPath = (TextView) view.findViewById(R.id.tvLoadPath);
        TextView tvLoadSize = (TextView) view.findViewById(R.id.tvLoadSize);
        TextView tvDateLastModif = (TextView) view.findViewById(R.id.tvDateLastModif);

        // получаем объект из списка объектов
        File currentFile = fileList.get(position);
        Log.d("MyLog", "currentFile = " + currentFile.getName());

        // устанавливаем значения компонентам одного элемента списка
        //иконка по типу файла
        switch (currentFile.getName().substring(currentFile.getName().lastIndexOf("."))) {
            case ".pdf":
                ivIconFileType.setImageResource(R.drawable.pdf);
                break;
            case ".jpg":
                ivIconFileType.setImageResource(R.drawable.jpg);
                break;
            case ".mp3":
                ivIconFileType.setImageResource(R.drawable.mp3);
                break;
            case ".xls":
                ivIconFileType.setImageResource(R.drawable.xls);
                break;
            default:
                ivIconFileType.setImageResource(R.drawable.file);
        }

        //параметры файла:
        tvLoadName.setText(currentFile.getName());
        tvLoadPath.setText(currentFile.getAbsolutePath());
        tvLoadSize.setText(fileSizeToString(currentFile.length()));
        tvDateLastModif.setText(dateModifToString(currentFile.lastModified()));

        return view;
    }


    public String fileSizeToString(long fileSize) {
        String result;
        if (fileSize < 1024) {
            result = String.valueOf(fileSize) + " B";
        } else {
            if (fileSize < Math.pow(1024, 2)) {
                fileSize = (long) ((int) ((fileSize / 1024) * 100)) / 100;
                result = String.valueOf(fileSize) + " KB";
            } else {
                if (fileSize < Math.pow(1024, 3)) {
                    fileSize = (long) ((int) ((fileSize / Math.pow(1024, 2)) * 100)) / 100;
                    result = String.valueOf(fileSize) + " MB";
                } else {
                    fileSize = (long) ((int) ((fileSize / Math.pow(1024, 3)) * 100)) / 100;
                    result = String.valueOf(fileSize) + " GB";
                }
            }
        }
        return result;
    }

    public String dateModifToString (long time){
        String result;
        Calendar dating = Calendar.getInstance();
        dating.setTimeInMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:YYYY");
        result = String.valueOf(sdf.format(dating.getTime()));
        return result;
    }

}