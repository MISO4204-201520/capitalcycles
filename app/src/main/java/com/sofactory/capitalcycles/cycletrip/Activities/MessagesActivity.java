package com.sofactory.capitalcycles.cycletrip.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.sofactory.capitalcycles.cycletrip.DTOs.MensajeDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.RespuestaMensajeDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.RespuestaUsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.UsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.Tasks.GetInMessagesTask;
import com.sofactory.capitalcycles.cycletrip.Tasks.UsersListTask;
import com.sofactory.capitalcycles.cycletrip.Utils.Preferences.UserPreferences;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MessagesActivity extends Activity implements AdapterView.OnItemClickListener {

    private AbsListView mListView;
    private ListAdapter mAdapter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences=getApplicationContext().getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE);
        Long code = Long.parseLong(preferences.getString(UserPreferences.USER_CODE, ""));
        GetInMessagesTask mGetInMessagesTask = new GetInMessagesTask(getApplicationContext(),code.toString());
        setContentView(R.layout.activity_messages);
        try {
            RespuestaMensajeDTO response = mGetInMessagesTask.execute().get();

            if (response.getCodigo()==0) {

                List<MensajeDTO> mensajeDTOList = response.getMensajes();
                mAdapter = new ArrayAdapter<MensajeDTO>(MessagesActivity.this,
                        R.layout.list_item_black, android.R.id.text1, mensajeDTOList);

                mListView = (AbsListView) findViewById(android.R.id.list);
                ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

                mListView.setOnItemClickListener(this);
                getActionBar().setTitle(R.string.info_text_in_messages);

            } else {
                Toast toast = Toast.makeText(MessagesActivity.this, R.string.error_users_list, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final MensajeDTO mensajeDTO = ( MensajeDTO ) mAdapter.getItem(position);

        new AlertDialog.Builder(MessagesActivity.this)
                .setTitle(R.string.text_alert_messageDetail)
                .setMessage(mensajeDTO.getTexto()+"\n"+
                        "Enviado por: "+mensajeDTO.getNombres() +" "+mensajeDTO.getApellidos()+"\n"+
                        "Fecha mensaje: "+mensajeDTO.getFecha())
                .setPositiveButton(R.string.button_close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_email)
                .show();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MessagesActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}


