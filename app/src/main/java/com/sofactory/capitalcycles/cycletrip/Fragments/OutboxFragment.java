package com.sofactory.capitalcycles.cycletrip.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.sofactory.capitalcycles.cycletrip.Activities.LoginActivity;
import com.sofactory.capitalcycles.cycletrip.DTOs.MensajeDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.RespuestaMensajeDTO;
import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.Tasks.GetOutMessagesTask;
import com.sofactory.capitalcycles.cycletrip.Utils.Preferences.UserPreferences;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class OutboxFragment extends Fragment implements AbsListView.OnItemClickListener {

    private AbsListView mListView;
    private ListAdapter mAdapter;
    private SharedPreferences preferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        preferences=getActivity().getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE);
        Long code = Long.parseLong(preferences.getString(UserPreferences.USER_CODE, ""));
        GetOutMessagesTask mGetOutMessagesTask = new GetOutMessagesTask(getActivity(),code.toString());
        try {
            RespuestaMensajeDTO response = mGetOutMessagesTask.execute().get();

            if (response.getCodigo()==0) {

                List<MensajeDTO> mensajeDTOList = response.getMensajes();
                updateAdapter(mensajeDTOList);

            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.error_messages_list, Toast.LENGTH_LONG);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outbox, container, false);


        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final MensajeDTO mensajeDTO = ( MensajeDTO ) mAdapter.getItem(position);

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.text_alert_messageDetail)
                .setMessage(mensajeDTO.getTexto() + "\n" +
                        "Destino: " + mensajeDTO.getNombresRecibe() + " " + mensajeDTO.getApellidosRecibe() + "\n" +
                        "Fecha mensaje: "+mensajeDTO.getFecha())
                .setPositiveButton(R.string.button_close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_email)
                .show();
    }


    public void updateAdapter(List<MensajeDTO> mensajes) {
        mAdapter = new ArrayAdapter<MensajeDTO>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mensajes);

    }


}
