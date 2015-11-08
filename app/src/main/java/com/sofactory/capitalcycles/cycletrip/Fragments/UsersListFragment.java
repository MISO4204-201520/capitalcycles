package com.sofactory.capitalcycles.cycletrip.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.sofactory.capitalcycles.cycletrip.Activities.LoginActivity;
import com.sofactory.capitalcycles.cycletrip.DTOs.MensajeDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.RespuestaUsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.UsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.Tasks.SendMessageTask;
import com.sofactory.capitalcycles.cycletrip.Tasks.UsersListTask;
import com.sofactory.capitalcycles.cycletrip.Utils.Preferences.UserPreferences;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class UsersListFragment extends Fragment implements AbsListView.OnItemClickListener {


    private AbsListView mListView;
    private ListAdapter mAdapter;
    private SharedPreferences preferences;
    private SendMessageTask mSendMessageTask;


    public UsersListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        UsersListTask mUsersListTask = new UsersListTask(getActivity());
        preferences=getActivity().getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE);
        try {
            RespuestaUsuarioDTO response = mUsersListTask.execute().get();

            if (response.getCodigo()==0) {

                List<UsuarioDTO> usuarioDTOList = response.getUsuarios();
                updateAdapter(usuarioDTOList);

            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.error_users_list, Toast.LENGTH_LONG);
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
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        return view;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final UsuarioDTO usuarioDTO = ( UsuarioDTO ) mAdapter.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_send_message);


        final EditText input = new EditText(getActivity());

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        builder.setPositiveButton(R.string.button_send_menssage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                MensajeDTO mensajeDTO = new MensajeDTO();
                Long code = Long.parseLong(preferences.getString(UserPreferences.USER_CODE, ""));
                mensajeDTO.setUsrdesde(code);
                mensajeDTO.setUsrpara(usuarioDTO.getCodigo());
                mensajeDTO.setTexto(m_Text);
                mSendMessageTask = new SendMessageTask(mensajeDTO,getActivity());
                mSendMessageTask.execute((Void) null);
            }
        });
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }



    public void updateAdapter(List<UsuarioDTO> usuarios) {
        mAdapter = new ArrayAdapter<UsuarioDTO>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, usuarios);

    }


}
