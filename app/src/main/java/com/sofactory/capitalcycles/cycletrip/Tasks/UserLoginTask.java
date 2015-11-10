package com.sofactory.capitalcycles.cycletrip.Tasks;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;
import com.google.gson.GsonBuilder;
import com.sofactory.capitalcycles.cycletrip.Activities.LoginActivity;
import com.sofactory.capitalcycles.cycletrip.Activities.MainActivity;
import com.sofactory.capitalcycles.cycletrip.DTOs.RespuestaSeguridadDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.UsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.Utils.Enums.LoginEnum;
import com.sofactory.capitalcycles.cycletrip.Utils.Preferences.UserPreferences;
import com.sofactory.capitalcycles.cycletrip.Utils.ProgressBar.GenericProgress;
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
 * Created by LuisSebastian on 10/5/15.
 */
public class UserLoginTask extends AsyncTask<Void,Void,RespuestaSeguridadDTO> {

    private UsuarioDTO usuarioDTO;
    private GenericProgress genericProgress;
    private Context context;
    private SharedPreferences sharedpreferences;



    public UserLoginTask(UsuarioDTO usuarioDTO, Context context, GenericProgress genericProgress) {
        this.usuarioDTO = usuarioDTO;
        this.genericProgress = genericProgress;
        this.context = context;
    }

    @Override
    protected RespuestaSeguridadDTO doInBackground(Void... params) {

        String json = new GsonBuilder().create().toJson(usuarioDTO);
        String serverIp = context.getResources().getString(R.string.serverIp);
        String serviceUri = context.getResources().getString(R.string.serviceUserLoginURI);
        RespuestaSeguridadDTO response = null;
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
            response = new GsonBuilder().create().fromJson(json2,RespuestaSeguridadDTO.class);
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
    protected void onPostExecute(final RespuestaSeguridadDTO response) {

        genericProgress.showProgress(false);

        if (response.getCodigo()==0) {
            sharedpreferences = context.getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(UserPreferences.USER_CODE, response.getCodigoUsuario()+"").apply();
            editor.putString(UserPreferences.USER_LOGIN, response.getLogin()).apply();
            editor.putString(UserPreferences.USER_NAME, response.getNombres()).apply();
            editor.putString(UserPreferences.USER_LASTNAME, response.getApellidos()).apply();
            editor.putString(UserPreferences.USER_EMAIL, response.getCorreo()).apply();
            editor.putString(UserPreferences.LOGIN_TYPE, LoginEnum.CYCLETRIP.toString()).apply();
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else if(response.getCodigo()==1){
            Toast toast = Toast.makeText(context, R.string.error_user_not_found, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }else if(response.getCodigo()==2){
            Toast toast = Toast.makeText(context, R.string.error_invalid_password, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


}