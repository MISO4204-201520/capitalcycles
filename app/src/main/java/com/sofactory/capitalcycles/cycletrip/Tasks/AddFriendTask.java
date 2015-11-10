package com.sofactory.capitalcycles.cycletrip.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;
import com.google.gson.GsonBuilder;
import com.sofactory.capitalcycles.cycletrip.DTOs.AmigoDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.RespuestaAmigoDTO;
import com.sofactory.capitalcycles.cycletrip.R;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by LuisSebastian on 11/10/15.
 */
public class AddFriendTask extends AsyncTask<Void, Void, RespuestaAmigoDTO> {

    private AmigoDTO amigoDTO;
    private Context context;


    public AddFriendTask(AmigoDTO amigoDTO, Context context) {

        this.amigoDTO = amigoDTO;
        this.context = context;

    }

    @Override
    protected RespuestaAmigoDTO doInBackground(Void... params) {

        String json = new GsonBuilder().create().toJson(amigoDTO);
        String serverIp = context.getResources().getString(R.string.serverIp);
        String serviceUri = context.getResources().getString(R.string.serviceAddFriendsURI);
        RespuestaAmigoDTO response = null;
        try {
            HttpPost httpPost = new HttpPost(serverIp.concat(serviceUri));
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String json2 = EntityUtils.toString(httpResponse.getEntity());
            response = new GsonBuilder().create().fromJson(json2, RespuestaAmigoDTO.class);
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


        if (response.getCodigo() == 0) {
            Toast toast = Toast.makeText(context, R.string.add_friend_success, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            Toast toast = Toast.makeText(context, R.string.add_friend_fail, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
