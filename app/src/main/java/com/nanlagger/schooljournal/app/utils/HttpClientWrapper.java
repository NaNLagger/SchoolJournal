package com.nanlagger.schooljournal.app.utils;

import android.content.Context;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by NaNLagger on 10.05.15.
 *
 * @author Stepan Lyashenko
 */
public class HttpClientWrapper {
    static HttpContext localContext;

    public static String executeGet(String url) {
        HttpGet request = new HttpGet(url);
        HttpResponse responseBody = null;
        try {
            MLogger.console(HttpClientWrapper.class, "URL: " + url);
            HttpClient client = new DefaultHttpClient();
            responseBody = client.execute(request, localContext);
            HttpEntity entity = responseBody.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
        return null;
    }

    public static String simpleGet(String url) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            try {
                String localResult = EntityUtils.toString(response.getEntity());
                return localResult;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String executePost(String url, List<NameValuePair> args) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String responseBody = null;
        CookieStore cookieStore = new BasicCookieStore();
        if (localContext == null) {
            localContext = new BasicHttpContext();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        }
        try {
            MLogger.console(HttpClientWrapper.class, "URL: " + url);
            httppost.setEntity(new UrlEncodedFormEntity(args));
            HttpResponse response = httpclient.execute(httppost, localContext);
            HttpEntity entity = response.getEntity();
            responseBody = EntityUtils.toString(entity);
            MLogger.console(HttpClientWrapper.class, response.getStatusLine()
                    .toString());
            if (entity != null) {
                MLogger.console(HttpClientWrapper.class,
                        "Response content length: " + entity.getContentLength());
            }
            return responseBody;
        } catch (Exception e) {
            MLogger.console(HttpClientWrapper.class, e.getMessage(), e);
            throw new Exception("internet exception", e);
        }
    }

    public static String simplePost(String url) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            HttpResponse httpResponse = httpclient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                return result;
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }

    public static String postData(String url) {

        String responseBody = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            responseBody = EntityUtils.toString(entity, HTTP.UTF_8);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        return responseBody;
    }
}
