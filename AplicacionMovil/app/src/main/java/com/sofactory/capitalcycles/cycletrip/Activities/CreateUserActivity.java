package com.sofactory.capitalcycles.cycletrip.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sofactory.capitalcycles.cycletrip.DTOs.RolDTO;
import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.Tasks.CreateUserTask;
import com.sofactory.capitalcycles.cycletrip.Utils.Enums.LoginEnum;
import com.sofactory.capitalcycles.cycletrip.Utils.Preferences.UserPreferences;
import com.sofactory.capitalcycles.cycletrip.Utils.ProgressBar.GenericProgress;
import com.sofactory.capitalcycles.cycletrip.Utils.Security.JasyptUtils;
import com.sofactory.capitalcycles.cycletrip.DTOs.UsuarioDTO;

import java.io.UnsupportedEncodingException;


/**
 * Created by LuisSebastian on 10/11/15.
 */
public class CreateUserActivity extends Activity{

    //E:11 --> Encryption error Jasypt.

    private Spinner mSpinnerGender;
    private EditText mTextLogin;
    private EditText mTextPassword;
    private EditText mTextEmail;
    private EditText mTextName;
    private EditText mTextLastName;
    private EditText mTextPhone;
    private Button mButtonCreateUser;
    private Button mButtonCancel;
    private View mProgressView;
    private View mNewUserFormView;
    private CreateUserTask mCreateUserTask = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        mTextLogin = (EditText) findViewById(R.id.text_login);
        mTextPassword = (EditText) findViewById(R.id.text_password);
        mTextEmail = (EditText) findViewById(R.id.text_email);
        mTextName = (EditText) findViewById(R.id.text_name);
        mTextLastName = (EditText) findViewById(R.id.text_lastName);
        mTextPhone = (EditText) findViewById(R.id.text_phone);
        mSpinnerGender = (Spinner) findViewById(R.id.spinner_gender);

        mButtonCreateUser = (Button) findViewById(R.id.button_createUser);
        mButtonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreateUser();
            }
        });

        mButtonCancel = (Button) findViewById(R.id.button_cancelCreateUser);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCreateUser();
            }
        });

        mNewUserFormView = findViewById(R.id.new_user_form);
        mProgressView = findViewById(R.id.progressBar_createUser);

    }



    private void attemptCreateUser() {

        // Reset errors.
        mTextName.setError(null);
        mTextLastName.setError(null);
        mTextLogin.setError(null);
        mTextPassword.setError(null);

        // Store values at the time of the login attempt.
        String name = mTextName.getText().toString();
        String lastName = mTextLastName.getText().toString();
        String login = mTextLogin.getText().toString();
        String password = mTextPassword.getText().toString();
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
        else if(TextUtils.isEmpty(password)){
            mTextPassword.setError(getString(R.string.error_required));
            focusView = mTextPassword;
            cancel = true;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
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
            GenericProgress genericProgress = new GenericProgress(mProgressView, mNewUserFormView,getApplicationContext());
            genericProgress.showProgress(true);
            try {
                String ePassword = JasyptUtils.encryptPassword(password);
                SharedPreferences preferences=getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE);
                UsuarioDTO newUSer = new UsuarioDTO(Long.valueOf(0),login,ePassword,name,lastName,phone,gender,email,preferences.getString(UserPreferences.TOKEN_ID,""));
                RolDTO rol = new RolDTO();
                rol.setId(1);
                newUSer.getRoles().add(rol);
                mCreateUserTask = new CreateUserTask(newUSer,getApplicationContext(),genericProgress, LoginEnum.cycletrip.toString());
                mCreateUserTask.execute((Void) null);
            } catch (UnsupportedEncodingException e) {
                genericProgress.showProgress(false);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_encryption, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                e.printStackTrace();
            }

        }


    }

    private void cancelCreateUser() {
        Intent intent= new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
