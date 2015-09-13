package com.akobets.fm2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class FileManager extends Activity {

    private ArrayList<File> filesByFolder = new ArrayList<>();
    private File currentDirectory = new File("sdcard/download");
    private FileListAdapter fileListAdapter;
    private File fileLevelUp = new File(FileListAdapter.LEVEL_UP);


    private TextView titleManager;
    private ListView lvFileManager;
    private Button bBackToLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);


        Intent intent = getIntent();
//        String receivedData;
//        receivedData = intent.getStringExtra(Loader.TRANSFER_DATA);
//        browseTo(new File(receivedData));
        browseTo(new File(intent.getStringExtra(Loader.TRANSFER_DATA)));

        bBackToLoader = (Button) findViewById(R.id.bBackToLoader);
        bBackToLoader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        fileListAdapter = new FileListAdapter(this, R.layout.row_file_manager, filesByFolder);
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
