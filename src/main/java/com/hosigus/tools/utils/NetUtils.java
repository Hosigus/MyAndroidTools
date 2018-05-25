package com.hosigus.tools.utils;

import com.hosigus.tools.items.JSONBean;
import com.hosigus.tools.options.NetOption;
import com.hosigus.tools.interfaces.NetCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by 某只机智 on 2018/5/5.
 */

public class NetUtils {
    private NetUtils(){throw new UnsupportedOperationException("cannot be instantiated");}

    public static void post(final NetOption options, final NetCallback callback) {
        ThreadUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(options.getUrl());
                    if(options.getUrl().contains("https"))
                        conn = (HttpsURLConnection) url.openConnection();
                    else
                        conn = (HttpURLConnection) url.openConnection();

                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    if(!(options.getParamSet() == null || options.getParamSet().isEmpty())){
                        conn.setDoOutput(true);
                        OutputStream os = conn.getOutputStream();
                        os.write(formParamString(options.getParamSet()).getBytes());
                        os.flush();
                        os.close();
                    }

                    int resCode = conn.getResponseCode();
                    if (resCode != HttpsURLConnection.HTTP_OK) {
                        callback.onFail(new Exception(String.valueOf(resCode)));
                        return;
                    }

                    InputStream inputStream = conn.getInputStream();
                    Scanner in = new Scanner(inputStream);
                    StringBuilder builder = new StringBuilder();
                    while (in.hasNextLine()) {
                        builder.append(in.nextLine()).append("\n");
                    }
                    final String jsonStr = builder.toString();
                    ThreadUtils.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONBean bean;
                                if (options.getBeanClass() == null) {
                                    bean = new JSONBean() {
                                        @Override
                                        public void parser(JSONObject object) {
                                            setJson(object.toString());
                                        }
                                    };
                                } else {
                                    bean = options.getBeanClass().newInstance();
                                }
                                bean.setJson(jsonStr);
                                bean.parser(new JSONObject(jsonStr));
                                callback.onSuccess(bean);
                            } catch (InstantiationException | IllegalAccessException | JSONException e) {
                                callback.onFail(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    callback.onFail(e);
                }
            }
        });
    }

    private static String formParamString(Map<String, String> paramSet) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : paramSet.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
