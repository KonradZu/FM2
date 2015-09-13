package com.akobets.fm2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Loader extends Activity {

    public static final String FILE = "http://www.renault.ua/media/brochures/att00452579/Fluence_ebrochure.pdf";
    private Button bDownload;
    private ProgressBar pbDownloadProgress;
    private TextView tvDigitProgress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        bDownload = (Button) findViewById(R.id.bDownload);
        pbDownloadProgress = (ProgressBar) findViewById(R.id.pbDownloadProgress);
        tvDigitProgress = (TextView) findViewById(R.id.tvDigitProgress);

        bDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Нажата кнопка Download",Toast.LENGTH_LONG).show();
                downloadFile(FILE);
            }
        });


//        setContentView(load, new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));

    }


    private void downloadFile(String url) {
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        final ProgressDialog progressDialog = new ProgressDialog(this);

        new AsyncTask<String, Integer, File>() {
            private Exception m_error = null;

//            @Override
//            protected void onPreExecute() {
////                progressDialog.setMessage("Downloading ...");
////                progressDialog.setCancelable(false);
////                progressDialog.setMax(100);
////                progressDialog
////                        .setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
////
////                progressDialog.show();
//            }

            @Override
            protected File doInBackground(String... params) {
                URL url;
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
                    urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();

                    file = File.createTempFile("TempFile", "download");
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

            // обновляем progressDialog
            @Override
            protected void onProgressUpdate(Integer... values) {
//                progressDialog.setProgress((int) ((values[0] / (float) values[1]) * 100));
                pbDownloadProgress.setProgress((int) ((values[0] / (float) values[1]) * 100));
                tvDigitProgress.setText(String.valueOf((values[0] / (float) values[1]) * 100)+"%");
            }

            ;

            @Override
            protected void onPostExecute(File file) {
                // отображаем сообщение, если возникла ошибка
                if (m_error != null) {
                    m_error.printStackTrace();
                    return;
                }
                // закрываем прогресс и удаляем временный файл
//                progressDialog.hide();
                file.delete();
            }
        }.execute(url);
    }
}
