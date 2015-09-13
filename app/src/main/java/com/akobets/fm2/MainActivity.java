package com.akobets.fm2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends Activity {

    private ArrayList<File> filesByFolder = new ArrayList<>();
    private File currentDirectory = new File("sdcard/download");
    private FileListAdapter fileListAdapter;
    private File fileLevelUp = new File(FileListAdapter.LEVEL_UP);


    private TextView titleManager;
    private ListView lvFileManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browseTo(currentDirectory);
    }

    //browse to parent directory
    private void upOneLevel() {
        if (currentDirectory.getParent() != null) {
            browseTo(currentDirectory.getParentFile());
        }
    }

    //browse to file or directory
//    private void browseTo(final File aDirectory) {
    private void browseTo(File aDirectory) {
        //if we want to browse directory
        if (aDirectory.isDirectory()) {
            //fill list with files from this directory
            currentDirectory = aDirectory;
            fill(aDirectory.listFiles());

            //set titleManager text
            TextView titleManager = (TextView) findViewById(R.id.tvTitleManager);
            titleManager.setText(aDirectory.getAbsolutePath());
        }
//        else {
//            //if we want to open file, show this dialog:
//            //listener when YES button clicked
//            DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface arg0, int arg1) {
//                    //intent to navigate file
//                    Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("file://" + aDirectory.getAbsolutePath()));
//                    //start this activity
//                    startActivity(i);
//                }
//            };
//            //listener when NO button clicked
//            DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface arg0, int arg1) {
//                    //do nothing
//                    //or add something you want
//                }
//            };
//
//            //create dialog
//            new AlertDialog.Builder(this)
//                    .setTitle("Подтверждение") //title
//                    .setMessage("Хотите открыть файл " + aDirectory.getName() + "?") //message
//                    .setPositiveButton("Да", okButtonListener) //positive button
//                    .setNegativeButton("Нет", cancelButtonListener) //negative button
//                    .show(); //show dialog
//        }
    }

    //fill list
    private void fill(File[] files) {
        //clear list
        filesByFolder.clear();

        if (currentDirectory.getParent() != null) {
            filesByFolder.add(fileLevelUp);
        }


            //add every file into list
            for (File f : files) {
                filesByFolder.add(f);
                Log.d("MyLog", "" + filesByFolder.size());
            }


        //create array adapter to show everything
        lvFileManager = (ListView) findViewById(R.id.lvFileManager);
        fileListAdapter = new FileListAdapter(this, R.layout.row, filesByFolder);
        lvFileManager.setAdapter(fileListAdapter);

        lvFileManager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (filesByFolder.get(position).getName() == FileListAdapter.LEVEL_UP) {
                    Log.d("MyLog", "Выходим на уровень выше, имя файла - " + filesByFolder.get(position).getName());
                    upOneLevel();
                } else {
                    if (filesByFolder.get(position).isDirectory()) {
                        Log.d("MyLog", "Заходим в директорию  - " + filesByFolder.get(position).getName());
                        browseTo(filesByFolder.get(position));
                    } else {
                        Toast.makeText(getApplicationContext(), "В будущем человечество научится работать с файлами, но не сейчас...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

//    //when you clicked onto item
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        //get selected file name
//        int selectionRowID = position;
//        String selectedFileString = this.filesByFolder.get(selectionRowID);
//
//        //if we select ".." then go upper
//        if (selectedFileString.equals("..")) {
//            this.upOneLevel();
//        } else {
//            //browse to clicked file or directory using browseTo()
//            File clickedFile = null;
//            clickedFile = new File(selectedFileString);
//            if (clickedFile != null)
//                this.browseTo(clickedFile);
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
