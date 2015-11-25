package com.sofactory.capitalcycles.cycletrip.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sofactory.capitalcycles.cycletrip.Activities.LoginActivity;
import com.sofactory.capitalcycles.cycletrip.DTOs.RolDTO;
import com.sofactory.capitalcycles.cycletrip.DTOs.UsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.Tasks.CreateUserTask;
import com.sofactory.capitalcycles.cycletrip.Tasks.UpdateUserTask;
import com.sofactory.capitalcycles.cycletrip.Utils.Preferences.UserPreferences;
import com.sofactory.capitalcycles.cycletrip.Utils.ProgressBar.GenericProgress;
import com.sofactory.capitalcycles.cycletrip.Utils.Security.JasyptUtils;

import java.io.UnsupportedEncodingException;


public class UserProfileFragment extends Fragment {

    private Spinner mSpinnerGender;
    private EditText mTextLogin;
    private EditText mTextEmail;
    private EditText mTextName;
    private EditText mTextLastName;
    private EditText mTextPhone;
    private Button mButtonUpdateUser;
    private View mProgressView;
    private View mNewUserFormView;
    private SharedPreferences preferences;
    private UpdateUserTask mUpdateUserTask = null;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_profile, container,
                false);
        preferences=getActivity().getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE);
        mTextLogin = (EditText) v.findViewById(R.id.update_text_login);
        mTextLogin.setText(preferences.getString(UserPreferences.USER_LOGIN, ""));
        mTextEmail = (EditText) v.findViewById(R.id.update_text_email);
        mTextEmail.setText(preferences.getString(UserPreferences.USER_EMAIL,""));
        mTextName = (EditText) v.findViewById(R.id.update_text_name);
        mTextName.setText(preferences.getString(UserPreferences.USER_NAME,""));
        mTextLastName = (EditText) v.findViewById(R.id.update_text_lastName);
        mTextLastName.setText(preferences.getString(UserPreferences.USER_LASTNAME,""));
        mTextPhone = (EditText) v.findViewById(R.id.update_text_phone);
        mTextPhone.setText(preferences.getString(UserPreferences.USER_PHONE,""));
        mSpinnerGender = (Spinner) v.findViewById(R.id.update_spinner_gender);

        mButtonUpdateUser = (Button) v.findViewById(R.id.button_update_user);
        mButtonUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptUpdateUser();
            }
        });


        mNewUserFormView = v.findViewById(R.id.new_update_user_form);
        mProgressView = v.findViewById(R.id.progressBar_updateUser);
        return v;
    }

    private void attemptUpdateUser() {

        // Reset errors.
        mTextName.setError(null);
        mTextLastName.setError(null);
        mTextLogin.setError(null);


        // Store values at the login attempt.
        String name = mTextName.getText().toString();
        String lastName = mTextLastName.getText().toString();
        String login = mTextLogin.getText().toString();
        String genderFull = String.valueOf(mSpinnerGender.getSelectedItem()).equals(R.string.gender_f)?"F":"M";
        String gender = genderFull ;
        String email = mTextEmail.getText().toString();
        String phone = mTextPhone.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if(TextUtils.isEmpty(login)){
            mTextLogin.setError(getString(R.string.error_required));
            focusView = mTextLogin;
            cancel = true;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = mTextEmail;
            cancel = true;
        }else if(TextUtils.isEmpty(name)){
            mTextName.setError(getString(R.string.error_required));
            focusView = mTextName;
            cancel = true;
        } else if(TextUtils.isEmpty(lastName)){
            mTextLastName.setError(getString(R.string.error_required));
            focusView = mTextLastName;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }else{
            GenericProgress genericProgress = new GenericProgress(mProgressView, mNewUserFormView,getActivity().getApplicationContext());
            genericProgress.showProgress(true);

                Long code = Long.parseLong(preferences.getString(UserPreferences.USER_CODE, ""));
                UsuarioDTO newUSer = new UsuarioDTO(code,login,"",name,lastName,phone,gender,email,preferences.getString(UserPreferences.TOKEN_ID,""));
                RolDTO rol = new RolDTO();
                rol.setId(1);
                newUSer.getRoles().add(rol);
                mUpdateUserTask = new UpdateUserTask(newUSer,getActivity().getApplicationContext(),genericProgress);
                mUpdateUserTask.execute((Void) null);

        }
    }



}
