package com.sofactory.capitalcycles.cycletrip.Tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.sofactory.capitalcycles.cycletrip.Activities.MainActivity;
import com.sofactory.capitalcycles.cycletrip.DTOs.RespuestaUsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.UsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.Fragments.UsersListFragment;
import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.Utils.ProgressBar.GenericProgress;
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
import java.util.List;

/**
 * Created by LuisSebastian on 10/17/15.
 */
public class UsersListTask extends AsyncTask<Void,Void,RespuestaUsuarioDTO> {


    private Context context;



    public UsersListTask(Context context) {

        this.context = context;
    }

    @Override
    protected RespuestaUsuarioDTO doInBackground(Void... params) {

        String serverIp = context.getResources().getString(R.string.serverIp);
        String serviceUri = context.getResources().getString(R.string.serviceGetAllUsersURI);
        RespuestaUsuarioDTO response = null;
        try {
            HttpGet httpGet = new HttpGet(serverIp.concat(serviceUri));
            HttpClient client = new DefaultHttpClient();
            HttpParams httpParams = client.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
            HttpConnectionParams.setSoTimeout(httpParams, 3000);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String json = EntityUtils.toString(httpResponse.getEntity());
            response = new GsonBuilder().create().fromJson(json,RespuestaUsuarioDTO.class);
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
    protected void onPostExecute(final RespuestaUsuarioDTO response) {

    }




}
