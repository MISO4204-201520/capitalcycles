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
import android.widget.TextView;
import android.widget.Toast;

import com.sofactory.capitalcycles.cycletrip.Activities.LoginActivity;
import com.sofactory.capitalcycles.cycletrip.DTOs.CatalogoDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.ProductoDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.PuntosUsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.RedimirProductoDTO;
import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.Tasks.GetProductsTask;
import com.sofactory.capitalcycles.cycletrip.Tasks.GetUserScoreTask;
import com.sofactory.capitalcycles.cycletrip.Tasks.UsePointsTask;
import com.sofactory.capitalcycles.cycletrip.Utils.Preferences.UserPreferences;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class FidelizationFragment extends Fragment implements AbsListView.OnItemClickListener {

    private AbsListView mListView;
    private ListAdapter mAdapter;
    private SharedPreferences preferences;
    private TextView mEditTextUserScore;
    private int userScore;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        preferences=getActivity().getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE);
        Long code = Long.parseLong(preferences.getString(UserPreferences.USER_CODE, ""));
        GetProductsTask mGetProductosTask = new GetProductsTask(getActivity(),code.toString());
        GetUserScoreTask mGetUserScoreTask = new GetUserScoreTask(getActivity(),code.toString());
        try {
            CatalogoDTO response = mGetProductosTask.execute().get();
            PuntosUsuarioDTO response2 = mGetUserScoreTask.execute().get();

            if (response.getCodigo()==0) {

                List<ProductoDTO> mensajeDTOList = response.getProductos();
                updateAdapter(mensajeDTOList);

            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.error_product_list, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            if(response2.getCodigo()==0){
                userScore = response2.getPuntos();
            }else{
                Toast toast = Toast.makeText(getActivity(), R.string.error_user_score, Toast.LENGTH_LONG);
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
        View view = inflater.inflate(R.layout.fragment_fidelization, container, false);

        mEditTextUserScore = (TextView) view.findViewById(R.id.textView_available_score);
        mEditTextUserScore.setText(userScore+"");

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final ProductoDTO productoDTO = ( ProductoDTO ) mAdapter.getItem(position);

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.text_redim_points)
                .setMessage(getActivity().getString(R.string.text_redin_confirmation)+"\n"+productoDTO.getNombre()+"\n"+getActivity().getString(R.string.text_redim_confirmation2))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       Long code = Long.parseLong(preferences.getString(UserPreferences.USER_CODE, ""));
                        RedimirProductoDTO redimirProductoDTO = new RedimirProductoDTO();
                        redimirProductoDTO.setCodigoUsuario(code+"");
                        redimirProductoDTO.setIdProducto(productoDTO.getId());
                       UsePointsTask mUsePointsTask = new UsePointsTask(redimirProductoDTO,getActivity());
                       mUsePointsTask.execute((Void) null);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public void updateAdapter(List<ProductoDTO> productos) {
        mAdapter = new ArrayAdapter<ProductoDTO>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, productos);

    }


}
