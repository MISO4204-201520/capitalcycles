package com.sofactory.capitalcycles.cycletrip.Tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;
import com.google.gson.GsonBuilder;
import com.sofactory.capitalcycles.cycletrip.Activities.MainActivity;
import com.sofactory.capitalcycles.cycletrip.DTOs.RedimirProductoDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.RespuestaDTO;
import com.sofactory.capitalcycles.cycletrip.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by LuisSebastian on 10/19/15.
 */
public class UsePointsTask extends AsyncTask<Void,Void,RespuestaDTO> {

    private RedimirProductoDTO redimirProductoDTO;
    private Context context;


    public UsePointsTask(RedimirProductoDTO catalogoDTO, Context context) {
        this.redimirProductoDTO = catalogoDTO;
        this.context = context;
    }

    @Override
    protected RespuestaDTO doInBackground(Void... params) {

        String json = new GsonBuilder().create().toJson(redimirProductoDTO);
        String serverIp = context.getResources().getString(R.string.serverIp);
        String serviceUri = context.getResources().getString(R.string.serviceRediProductsURI);
        RespuestaDTO response = null;
        try {
            HttpPost httpPost = new HttpPost(serverIp.concat(serviceUri));
            HttpClient client = new DefaultHttpClient();
            HttpParams httpParams = client.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
            HttpConnectionParams.setSoTimeout(httpParams, 3000);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String json2 = EntityUtils.toString(httpResponse.getEntity());
            response = new GsonBuilder().create().fromJson(json2,RespuestaDTO.class);
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
    protected void onPostExecute(final RespuestaDTO response) {

        if (response.getMensaje().equals("OK")) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Toast toast = Toast.makeText(context, R.string.text_redim_points_success, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else{
            Toast toast = Toast.makeText(context, R.string.error_redim_noAvailablePoints, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
    }


}
