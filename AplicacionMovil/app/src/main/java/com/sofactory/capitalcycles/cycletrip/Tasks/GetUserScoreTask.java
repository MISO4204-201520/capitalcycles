package com.sofactory.capitalcycles.cycletrip.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.GsonBuilder;
import com.sofactory.capitalcycles.cycletrip.DTOs.PuntosUsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by LuisSebastian on 10/18/15.
 */
public class GetUserScoreTask extends AsyncTask<Void,Void,PuntosUsuarioDTO> {


    private Context context;
    private String idUser;


    public GetUserScoreTask(Context context, String idUser) {

        this.context = context;
        this.idUser = idUser;
    }

    @Override
    protected PuntosUsuarioDTO doInBackground(Void... params) {

        String serverIp = context.getResources().getString(R.string.serverIp);
        String serviceUri = context.getResources().getString(R.string.serviceGetUserScoreURI);
        String paramsGet = "/" + idUser;
        PuntosUsuarioDTO response = null;
        try {
            HttpGet httpGet = new HttpGet(serverIp.concat(serviceUri + paramsGet));
            HttpClient client = new DefaultHttpClient();
            HttpParams httpParams = client.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
            HttpConnectionParams.setSoTimeout(httpParams, 3000);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String json = EntityUtils.toString(httpResponse.getEntity());
            response = new GsonBuilder().create().fromJson(json, PuntosUsuarioDTO.class);
            return response;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }
}

