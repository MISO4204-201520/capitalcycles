package com.sofactory.capitalcycles.cycletrip.Tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.sofactory.capitalcycles.cycletrip.Activities.LoginActivity;
import com.sofactory.capitalcycles.cycletrip.Activities.MainActivity;

import com.sofactory.capitalcycles.cycletrip.DTOs.RespuestaUsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.UsuarioDTO;

import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.Utils.Preferences.UserPreferences;
import com.sofactory.capitalcycles.cycletrip.Utils.ProgressBar.GenericProgress;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * Created by LuisSebastian on 10/11/15.
 */
public class CreateUserTask extends AsyncTask<Void, Void, RespuestaUsuarioDTO> {

    private UsuarioDTO usuarioDTO;
    private Context context;
    private GenericProgress genericProgress;

    public CreateUserTask(UsuarioDTO usuarioDTO, Context context,GenericProgress genericProgress) {

        this.usuarioDTO = usuarioDTO;
        this.context = context;
        this.genericProgress = genericProgress;
    }

    @Override
    protected RespuestaUsuarioDTO doInBackground(Void... params) {

        String json = new GsonBuilder().create().toJson(usuarioDTO);
        String serverIp = context.getResources().getString(R.string.serverIp);
        String serviceUri = context.getResources().getString(R.string.serviceNewUserURI);
        RespuestaUsuarioDTO response = null;
        try {
            HttpPost httpPost = new HttpPost(serverIp.concat(serviceUri));
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String json2 = EntityUtils.toString(httpResponse.getEntity());
            response = new GsonBuilder().create().fromJson(json2,RespuestaUsuarioDTO.class);
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
        genericProgress.showProgress(false);
        if(response.getCodigo()==0){
            SharedPreferences sharedpreferences = context.getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(UserPreferences.USER_NAME, usuarioDTO.getNombres()).apply();
            editor.putString(UserPreferences.USER_LASTNAME, usuarioDTO.getApellidos()).apply();
            editor.putString(UserPreferences.USER_LOGIN, usuarioDTO.getLogin()).apply();
            editor.putString(UserPreferences.USER_EMAIL, usuarioDTO.getCorreo()).apply();
            editor.putString(UserPreferences.USER_PHONE, usuarioDTO.getCelular()).apply();
            editor.putString(UserPreferences.USER_CODE, response.getCodigoUsuario()+"").apply();
            Toast toast = Toast.makeText(context, R.string.create_user_success, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else if(response.getCodigo()==2){
            Toast toast = Toast.makeText(context, R.string.create_user_duplicate, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else{
            Toast toast = Toast.makeText(context, R.string.create_user_error, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }
}
