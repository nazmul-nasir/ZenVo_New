package com.revesoft.zenvo_new;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntity;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadBroadcastImage downloadBroadcastImage = new DownloadBroadcastImage();
        // parameters : fileName, username,password
        downloadBroadcastImage.execute("36006_1452831040961AOMcVK0djIWnr0DJFcOoH0EFXJ9YNnncCkSvzFdZ.jpg",
                "8801814655953",
                "7ZGe55b5348y968"
                );


    }


   /* class DownloadFile extends AsyncTask<String, String, String> {


        private static final String TAG = "Nasir";

        @Override
        protected String doInBackground(String... f_url) {

            URL url = null;
            int count;

            try {
                HttpResponse response1 = null;
                int statusCode = -1;


                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://5.63.150.228/zenvo/api/downloadAttachment.jsp");

                    MultipartEntity multipartEntity = new MultipartEntity();

				*//*multipartEntity.addPart("requesttype", new StringBody(
						REQUEST_TYPE_DOWNLOAD));*//*
                    multipartEntity.addPart("fileName", new StringBody(
                            "36006_1452831040961AOMcVK0djIWnr0DJFcOoH0EFXJ9YNnncCkSvzFdZ.jpg"));
                    multipartEntity.addPart("user", new StringBody("8801814655953"));
                    multipartEntity.addPart("password", new StringBody("039e0b049ca626b11d34d74782ec9012"));
                    multipartEntity.addPart("nonce", new StringBody("9606F895"));



				*//*if (targetUsername != null) {
					multipartEntity.addPart("targetUsername", new StringBody(
							targetUsername));
				}*//*


                   *//* httpPost.setEntity(multipartEntity);
                    response1 = httpClient.execute(httpPost);*//*


                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("5.63.150.228")
                        .appendPath("zenvo")
                        .appendPath("api")
                        .appendPath("downloadAttachment.jsp")
                        .appendQueryParameter("fileName", "36006_1452831040961AOMcVK0djIWnr0DJFcOoH0EFXJ9YNnncCkSvzFdZ.jpg")
                        .appendQueryParameter("user", "8801814655953")
                        .appendQueryParameter("password", "039e0b049ca626b11d34d74782ec9012")
                        .appendQueryParameter("nonce", "9606F895");

                String myUrl = builder.build().toString();
                HttpPost httpGet = new HttpPost(myUrl);
                HttpResponse response = httpClient.execute(httpGet);

                    if (response==null)
                        Log.d(TAG, "Response is null ");
                    else
                        Log.d(TAG, "Response is not null ");






                   // Log.d("Response ", EntityUtils.toString( response.getEntity()));




                HttpEntity entity = response.getEntity();
               // HttpEntity entity = response.getEntity();
                *//*if (entity != null) {
                    String path = "/storage/emulated/0/ZenVo/response.txt";
                    FileOutputStream outstream = new FileOutputStream(path) ;
                        entity.writeTo(outstream);

                }*//*
              //  Log.i(TAG,entity.getContentType().toString());

                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String path = "/storage/emulated/0/ZenVo/response.jpg";
                    FileOutputStream output = new FileOutputStream(path);
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int len = 0;
                    while ((len = instream.read(buffer)) != -1) {
                        output.write(buffer, 0, len);
                    }
                    output.close();
                }


                } catch (Exception e) {
                    // TODO: handle exception
                    Log.d(TAG, "Exeption during httpExecute: " + e);
                }

                *//*
                url = new URL("http://5.63.150.228/zenvo/api/downloadAttachment.jsp");



                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(20000);
                conn.setConnectTimeout(25000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type","image/png");
                conn.setDoInput(true);
                conn.setDoOutput(true);





                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("fileName", "36006_1452831040961AOMcVK0djIWnr0DJFcOoH0EFXJ9YNnncCkSvzFdZ.jpg")
                        .appendQueryParameter("user", "8801814655953")
                        .appendQueryParameter("password", "039e0b049ca626b11d34d74782ec9012")
                        .appendQueryParameter("nonce", "9606F895");
                String query = builder.build().getEncodedQuery();


                Log.i("Nasir","Start");
                OutputStream os = conn.getOutputStream();
                Log.i("Nasir","End");
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);

                writer.flush();
                writer.close();
                os.close();


                conn.connect();
                Log.i("Response: ",conn.getResponseMessage().toString());
                Log.i("Response: ",conn.toString());


                int lenghtOfFile = conn.getContentLength();
                String filetype = conn.getContentType();

                Log.i("File",lenghtOfFile+" "+filetype);
                String fileExtenstion = MimeTypeMap.getFileExtensionFromUrl("http://5.63.150.228/zenvo/api/downloadAttachment.jsp?fileName=36006_1452831040961AOMcVK0djIWnr0DJFcOoH0EFXJ9YNnncCkSvzFdZ.jpg&user=8801814655953&password=039e0b049ca626b11d34d74782ec9012&nonce=9606F895");
               String name = URLUtil.guessFileName(f_url[0], null, fileExtenstion);
                Log.i("File Type: ",filetype.toString());
                Log.i("File Ext: ",fileExtenstion.toString());
                Log.i("File Name: ",name.toString());*//*


              *//*  InputStream input = new BufferedInputStream(url.openStream(), 81920);

                // Output stream to write file
                String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ZenVoNasirNew";
                File dir = new File(fullPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                //  OutputStream output = new FileOutputStream("/sdcard/ZenVoNasir/downloadedfile.jpg");
                OutputStream output = new FileOutputStream("/sdcard/ZenVoNasirNew/"+"Nasir.txt");

                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();*//*

           *//* } catch (Exception e) {
                e.printStackTrace();
            }*//*
            return null;
        }
    }*/
}
