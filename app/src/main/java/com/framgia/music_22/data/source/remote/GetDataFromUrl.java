package com.framgia.music_22.data.source.remote;

import android.os.AsyncTask;
import com.framgia.music_22.data.OnFetchDataListener;
import com.framgia.music_22.utils.Constant;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetDataFromUrl extends AsyncTask<String, Void, String> {
    private OnFetchDataListener mOnFetchDataListener;

    public GetDataFromUrl(OnFetchDataListener onFetchDataListener) {
        mOnFetchDataListener = onFetchDataListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String jsonData = "";
        try {
            jsonData = getJsonFromUrl(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
            mOnFetchDataListener.onFetchDataError(e);
        }
        return jsonData;
    }

    @Override
    protected void onPostExecute(String resultData) {
        super.onPostExecute(resultData);
        if (resultData != null) {
            mOnFetchDataListener.onFetchDataSuccess(resultData);
        }
    }

    private String getJsonFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(Constant.REQUEST_METHOD);
        httpURLConnection.setConnectTimeout(Constant.CONNECT_TIMEOUT);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        httpURLConnection.disconnect();
        return stringBuilder.toString();
    }
}
