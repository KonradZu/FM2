package com.akobets.fm2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Loader extends Activity {

    public static final String FILE_URL = "http://www.renault.ua/media/brochures/att00452579/Fluence_ebrochure.pdf";
    public static final String FILE_PATH = "/sdcard/download";
    public static final int ID_SAVE_FILE = 0;


    private Button bDownload;
    private EditText etUrl;
    private ProgressBar pbDownloadProgress;
    private TextView tvDigitProgress;


    private ArrayList<File> downloadedFiles = new ArrayList<>();

    private String currentFileURL = "empty";
    private String currentFilePath = "sdcard/download";
    private String currentFileName = "newfile";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        etUrl = (EditText) findViewById(R.id.etUrl);
        bDownload = (Button) findViewById(R.id.bDownload);
        pbDownloadProgress = (ProgressBar) findViewById(R.id.pbDownloadProgress);
        tvDigitProgress = (TextView) findViewById(R.id.tvDigitProgress);

        bDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Нажата кнопка Download", Toast.LENGTH_LONG).show();


                currentFileURL = etUrl.getText().toString();
                if (currentFileURL.equals("")) {
                    currentFileURL = FILE_URL;
                }
                Log.d("MyLog", "После нажатия кнопки Download, url = " + currentFileURL);
//                Bundle bundle = new Bundle();
//                bundle.putInt("position", position);
//                Log.d("MyLog", "position " + position + " name " + contactList.get(position).getContact());
//                showDialog(ID_SAVE_FILE, bundle);


                showDialog(ID_SAVE_FILE);


//                downloadFile(FILE_URL);
            }
        });


//        setContentView(load, new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));

    }


    private void downloadFile(String url, final String path, final String name) {

        new AsyncTask<String, Integer, File>() {
            private Exception m_error = null;

//            @Override
//            protected void onPreExecute() {
//            }

            @Override
            protected File doInBackground(String... params) {
                URL url;
                String path;
                String name;
                HttpURLConnection urlConnection;
                InputStream inputStream;
                int totalSize;
                int downloadedSize;
                byte[] buffer;
                int bufferLength;

                File file = null;
                FileOutputStream fos = null;

                try {
                    url = new URL(params[0]);
                    path = params[1];
                    name = params[2];
                    Log.d("MyLog", "path = " + path);
                    Log.d("MyLog", "name = " + name);


                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();

//                    file = File.createTempFile(name, null, new File(path));//испльзуем
                    file = new File(path, name);//испльзуем

                    fos = new FileOutputStream(file);
                    inputStream = urlConnection.getInputStream();

                    totalSize = urlConnection.getContentLength();
                    downloadedSize = 0;

                    buffer = new byte[1024];
                    bufferLength = 0;

                    // читаем со входа и пишем в выход,
                    // с каждой итерацией публикуем прогресс
                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        fos.write(buffer, 0, bufferLength);
                        downloadedSize += bufferLength;
                        publishProgress(downloadedSize, totalSize);
                    }

                    fos.close();
                    inputStream.close();

                    return file;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    m_error = e;
                } catch (IOException e) {
                    e.printStackTrace();
                    m_error = e;
                }

                return null;
            }

            // обновляем progressBar
            @Override
            protected void onProgressUpdate(Integer... values) {
                pbDownloadProgress.setProgress((int) ((values[0] / (float) values[1]) * 100));
                tvDigitProgress.setText(String.valueOf((int)((values[0] / (float) values[1]) * 100)) + "%");
            }

            @Override
            protected void onPostExecute(File file) {
                // отображаем сообщение, если возникла ошибка
                if (m_error != null) {
                    m_error.printStackTrace();
                    return;
                }

            }
        }.execute(url, path, name);

    }


    @Override
    protected Dialog onCreateDialog(int id, final Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (id) {
            case ID_SAVE_FILE:
                LayoutInflater liSF = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View vSF = liSF.inflate(R.layout.save_file_dialog, null);


                final EditText etPath = (EditText) vSF.findViewById(R.id.etPath);
                final EditText etName = (EditText) vSF.findViewById(R.id.etName);
                final CheckBox chNewFileName = (CheckBox) vSF.findViewById(R.id.chNewFileName);

                builder.setView(vSF);
                builder.setTitle("Сохранение файла");


                chNewFileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            Toast.makeText(getApplicationContext(),
                                    "Брат, ты реально поставил галку !!!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Галку сняли ! Чистим поле..", Toast.LENGTH_LONG).show();
                            etName.setText("");
                        }
                    }
                });


                builder.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        currentFilePath = etPath.getText().toString();
                        if (currentFilePath.equals("")) {
                            currentFilePath = FILE_PATH;
                        }
                        Log.d("MyLog", "После подтверждения в диалоге, путь для сохранения = " + currentFilePath);

                        if (chNewFileName.isChecked()) {
                            currentFileName = etName.getText().toString() + fileNameFromUrl(currentFileURL, 2);
                        } else {
                            currentFileName = fileNameFromUrl(currentFileURL, 0);
                        }
                        Log.d("MyLog", "После подтверждения в диалоге, имя файла = " + currentFileName);
//                        adapter.notifyDataSetChanged();
//                        saveSharedPreferences();
                        downloadFile(currentFileURL, currentFilePath, currentFileName);
                        removeDialog(ID_SAVE_FILE);
                    }
                });

                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeDialog(ID_SAVE_FILE);
                    }
                });
                break;
        }

        return builder.create();
    }

    public String getCurrentFileURL() {
        return currentFileURL;
    }

    public void setCurrentFileURL(String currentFileURL) {
        this.currentFileURL = currentFileURL;
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public void setCurrentFilePath(String currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }


    public String fileNameFromUrl(String url, int mode) {
        String result = "";
//        Log.d("MyLog", "url = " + url);
        String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
        String fileExtension = url.substring(url.lastIndexOf("."));
        switch (mode) {
            case 0:
                result = fileName;
                break;
            case 1:
                result = fileNameWithoutExtension;
                break;
            case 2:
                result = fileExtension;
                break;
        }
        return result;
    }


}
