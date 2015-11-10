package com.sofactory.capitalcycles.cycletrip.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.google.gson.GsonBuilder;
import com.sofactory.capitalcycles.cycletrip.DTOs.RespuestaAmigoDTO;
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
 * Created by LuisSebastian on 11/9/15.
 */
public class GetFriendsTask extends AsyncTask<Void,Void,RespuestaAmigoDTO> {


    private Context context;
    private String codigoUsuario;


    public GetFriendsTask(Context context, String codigoUsuario) {

        this.context = context;
        this.codigoUsuario = codigoUsuario;
    }

    @Override
    protected RespuestaAmigoDTO doInBackground(Void... params) {

        String serverIp = context.getResources().getString(R.string.serverIp);
        String serviceUri = context.getResources().getString(R.string.serviceGetFriendsURI);
        String paramsGet = "/" + codigoUsuario;
        RespuestaAmigoDTO response = null;
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
            response = new GsonBuilder().create().fromJson(json, RespuestaAmigoDTO.class);
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

    @Override
    protected void onPostExecute(final RespuestaAmigoDTO response) {

    }
}

