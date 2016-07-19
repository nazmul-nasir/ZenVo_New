package com.revesoft.zenvo_new;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

public class DownloadBroadcastImage extends AsyncTask<String, String, String> {

    public static final String UPLOAD_COMPLETE = "uploadComplete";
    public static final String DOWNLOAD_COMPLETE = "downloadComplete";
    private Context mContext;
    private static final String REQUEST_TYPE_NONCE = "getNonce";

    private static final int RESPONSE_TYPE_UPLOAD_SUCCESSFULL = 0;
    private static final int RESPONSE_TYPE_INVALID_PASSWORD = 108;
    private static final int RESPONSE_TYPE_INVALID_USER = 109;
    private static final int RESPONSE_TYPE_NEW_NONCE_CREATED = 110;
    private static final int RESPONSE_TYPE_INTERNAL_SERVER_ERROR = 101;
    private static final int RESPONSE_TYPE_INVALID_NONCE = 106;
    private static final int RESPONSE_TYPE_HASH_MATCH_FOUND = 111;
    private static final int RESPONSE_TYPE_HASH_MATCH_NOT_FOUND = 112;
    private static final int RESPONSE_TYPE_INSUFFICIENT_PARAM = 113;
    private static final int RESPONSE_TYPE_INVALID_PARAM = 114;
    private static final int RESPONSE_TYPE_PRO_PIC_NOT_YET_SET = 115;

    private static final int OPERATION_FAILED = -1;
    public static final String OWN_PROPIC_FILENAME = "OwnPropic.jpg";
    public static final String DEFAULT_APP_NAME = "ItelMobileDialer";


    private static final String TAG = "Nasir";

    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        int count;
        String md5Password = null;
        String fileName = params[0];
        String username = params[1];
        String password = params[2];
        try {
            HttpResponse response1 = null;
            int statusCode = -1;
            HttpClient httpClient = new DefaultHttpClient();
            ResponseHolder responseHolder = getNonceHttpPost(
                    "http://5.63.150.228/zenvo/profilePictureHandler.do", username, null);
            Log.i(TAG, "Nonce " + responseHolder.nonce);
            if (responseHolder.nonce != null) {
                md5Password = createMD5Password(
                        responseHolder.nonce, username, password);
            }
            Uri.Builder builder = new Uri.Builder();
               /* builder.scheme("http")
                        .authority("5.63.150.228")
                        .appendPath("zenvo")
                        .appendPath("api")
                        .appendPath("downloadAttachment.jsp")
                        .appendQueryParameter("fileName", "36006_1452831040961AOMcVK0djIWnr0DJFcOoH0EFXJ9YNnncCkSvzFdZ.jpg")
                        .appendQueryParameter("user", "8801814655953")
                        .appendQueryParameter("password", "039e0b049ca626b11d34d74782ec9012")
                        .appendQueryParameter("nonce", "9606F895");*/
            builder.scheme("http")
                    .authority("5.63.150.228")
                    .appendPath("zenvo")
                    .appendPath("api")
                    .appendPath("downloadAttachment.jsp")
                    .appendQueryParameter("fileName", fileName)
                    .appendQueryParameter("user", username)
                    .appendQueryParameter("password", md5Password)
                    .appendQueryParameter("nonce", responseHolder.nonce);
            String myUrl = builder.build().toString();
            HttpPost httpGet = new HttpPost(myUrl);
            HttpResponse response = httpClient.execute(httpGet);
            /*if (response == null)
                Log.d(TAG, "Response is null ");
            else
                Log.d(TAG, "Response is not null ");*/

            HttpEntity entity = response.getEntity();
            String responseBodyType = entity.getContentType().toString();
            Log.d(TAG, "Response type: " + responseBodyType);

            if (responseBodyType.contains("text")) {
                byte[] responseByteArray = EntityUtils
                        .toByteArray(entity);
                String responseBody = new String(responseByteArray);
                Log.d(TAG,
                        "Response before parsing " + responseBody.trim());
                String[] parsedResponse = parseResponse(responseBody.trim());
                if (parsedResponse != null) {
                    statusCode = Integer.parseInt(parsedResponse[0]);
                    Log.d(TAG, "status code: " + statusCode);
                }

            } else if (entity != null) {
                InputStream instream = entity.getContent();
                File dir = new File("/storage/emulated/0/ZenVo/");
                File[] files = dir.listFiles();
                int numberOfFiles = files.length + 1;
                String path = "/storage/emulated/0/ZenVo/" + numberOfFiles + ".jpg";
                FileOutputStream output = new FileOutputStream(path);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int len = 0;
                while ((len = instream.read(buffer)) != -1) {
                    output.write(buffer, 0, len);
                }
                output.close();
            } else {
                Log.i(TAG, "Image Download Failed");
            }


        } catch (Exception e) {
            // TODO: handle exception
            Log.d(TAG, "Exeption during httpExecute: " + e);
        }
        return null;
    }

    private String createMD5Password(String nonce, String username,
                                     String password) {
        String md5String = nonce + username + password;
        Log.d(TAG, "String to be md5 " + md5String);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(md5String.getBytes("UTF-8"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] mdbytes = md.digest();
        // convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return sb.toString();

    }

    private static class ResponseHolder {
        public int statusCode;
        public String nonce;

    }

    private ResponseHolder getNonceHttpPost(String serverUri, String username,
                                            String imagehash) {
        Log.d(TAG, "**********Inside get nonce new************");
        HttpResponse httpResponse = null;
        ResponseHolder responseHolder = null;
        String nonce = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(serverUri);
            MultipartEntity multipartEntity = new MultipartEntity();
            multipartEntity.addPart("requesttype", new StringBody(
                    REQUEST_TYPE_NONCE));
            multipartEntity.addPart("username", new StringBody(username));
            // the image hash will be null when during the case of image
            // download attempt
            if (imagehash != null) {
                multipartEntity.addPart("hash", new StringBody(imagehash));
            }
            httpPost.setEntity(multipartEntity);
            httpResponse = httpClient.execute(httpPost);


        } catch (Exception e) {
            // TODO: handle exception
            Log.d(TAG, "Exeption while http execute of get nonce" + e);
            e.printStackTrace();
        }
        HttpEntity responseEntity = httpResponse.getEntity();
        if (responseEntity == null) {
            Log.d(TAG, "responseEntity is null");

        } else {
            String responseBody = null;
            try {
                responseBody = EntityUtils.toString(httpResponse.getEntity());
                Log.d(TAG, "Response before parsing " + responseBody.trim());
                String[] parsedResponse = parseResponse(responseBody.trim());
                if (parsedResponse != null) {
                    int statusCode = Integer.parseInt(parsedResponse[0]);
                    Log.d(TAG, "Status code :" + statusCode + " Description :"
                            + describeStatusCode(statusCode));
                    nonce = parsedResponse[1];
                    responseHolder = new ResponseHolder();
                    responseHolder.nonce = nonce;
                    responseHolder.statusCode = statusCode;
                } else {
                    Log.d(TAG, "Response can not be parsed properly");
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return responseHolder;
    }

    private String[] parseResponse(String response) {
        Log.d(TAG, "Trying to parse server response");
        //	Log.d(TAG, response);
        String[] responses = response.split(",");
        String[] parsedRespones = new String[responses.length];
        for (int i = 0; i < responses.length; i++) {
            String[] responseParts = responses[i].split("=");
            if (responseParts.length != 2) {
                return null;
            } else {
                Log.d(TAG, responses[i].trim());
                parsedRespones[i] = responseParts[1];
            }
        }
        return parsedRespones;
    }

    private String describeStatusCode(int statusCode) {
        String statusCodeMeaning = null;
        switch (statusCode) {
            case RESPONSE_TYPE_UPLOAD_SUCCESSFULL:
                statusCodeMeaning = "RESPONSE_TYPE_UPLOAD_SUCCESSFULL";
                break;
            case RESPONSE_TYPE_INVALID_PASSWORD:
                statusCodeMeaning = "RESPONSE_TYPE_INVALID_PASSWORD";
                break;
            case RESPONSE_TYPE_INVALID_USER:
                statusCodeMeaning = "RESPONSE_TYPE_INVALID_USER";
                break;
            case RESPONSE_TYPE_NEW_NONCE_CREATED:
                statusCodeMeaning = "RESPONSE_TYPE_NEW_NONCE_CREATED";
                break;
            case RESPONSE_TYPE_INTERNAL_SERVER_ERROR:
                statusCodeMeaning = "RESPONSE_TYPE_INTERNAL_SERVER_ERROR";
                break;
            case RESPONSE_TYPE_INVALID_NONCE:
                statusCodeMeaning = "RESPONSE_TYPE_INVALID_NONCE";
                break;
            case RESPONSE_TYPE_HASH_MATCH_FOUND:
                statusCodeMeaning = "RESPONSE_TYPE_HASH_MATCH_FOUND";
                break;
            case RESPONSE_TYPE_HASH_MATCH_NOT_FOUND:
                statusCodeMeaning = "RESPONSE_TYPE_HASH_MATCH_NOT_FOUND";
                break;
            case RESPONSE_TYPE_INSUFFICIENT_PARAM:
                statusCodeMeaning = "RESPONSE_TYPE_INSUFFICIENT_PARAM";
                break;
            case RESPONSE_TYPE_INVALID_PARAM:
                statusCodeMeaning = "RESPONSE_TYPE_INVALID_PARAM";
                break;
            case RESPONSE_TYPE_PRO_PIC_NOT_YET_SET:
                statusCodeMeaning = "RESPONSE_TYPE_PRO_PIC_NOT_YET_SET";
                break;
            default:
                break;
        }
        return statusCodeMeaning;
    }


}
