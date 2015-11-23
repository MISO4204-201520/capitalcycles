package com.sofactory.capitalcycles.cycletrip.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sofactory.capitalcycles.cycletrip.DTOs.UsuarioDTO;
import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.Tasks.UserLoginSocialTask;
import com.sofactory.capitalcycles.cycletrip.Tasks.UserLoginTask;
import com.sofactory.capitalcycles.cycletrip.Utils.Enums.LoginEnum;
import com.sofactory.capitalcycles.cycletrip.Utils.Parser.ParseFile;
import com.sofactory.capitalcycles.cycletrip.Utils.Preferences.UserPreferences;
import com.sofactory.capitalcycles.cycletrip.Utils.ProgressBar.GenericProgress;
import com.sofactory.capitalcycles.cycletrip.Utils.Security.JasyptUtils;
import com.sofactory.capitalcycles.cycletrip.Utils.Services.RegistrationIntentService;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import com.facebook.FacebookSdk;
import com.sofactory.capitalcycles.cycletrip.VO.Feature;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by LuisSebastian on 10/4/15.
 *
 */
public class LoginActivity extends Activity {

    public static final String USER_PREFERENCES = "UserPreferences" ;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "cycletrip";
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private GoogleCloudMessaging gcm;
    private String regid;
    private boolean isLogged;
    private SharedPreferences preferences;
    private LoginButton loginButton;
    private TwitterLoginButton loginButtonTwitter;
    private CallbackManager callbackManager;
    private UserLoginTask mAuthTask = null;
    private UserLoginSocialTask mUserLoginSocialTask = null;
    private GenericProgress genericProgress;
    private static List<Feature> featureList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        featureList = ParseFile.parseFile(getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        preferences=getSharedPreferences(USER_PREFERENCES,Context.MODE_PRIVATE);
        isLogged = preferences.getBoolean(UserPreferences.IS_LOGGED,false);
        if(isLogged){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.emailText);
        mPasswordView = (EditText) findViewById(R.id.passwordText);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.passwordText || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.buttonLogin);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.progressBar);

        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = preferences.getString(UserPreferences.TOKEN_ID,"");
            if (regid.isEmpty()) {
//                registerInBackground();
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }


        loginButton = (LoginButton) findViewById(R.id.login_button_fb);
        for(Feature feature : featureList){
            if(feature.getFeature().equals("gestionUsuario.redesSociales.facebook.excludes")){
                if(feature.getValue().equals("false")){
                    loginButton.setVisibility(View.GONE);
                    break;
                }
            }
        }
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_photos,user_about_me"));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("onSuccess", "--------" + loginResult.getAccessToken());
                Log.e("Token", "--------" + loginResult.getAccessToken().getToken());
                Log.e("Permision", "--------" + loginResult.getRecentlyGrantedPermissions());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code

                                try {
                                    if(object!=null) {
                                        String fbId = object.getString("id");
                                        String birthday = object.getString("birthday");
                                        String gender = object.getString("gender");
                                        String email = object.getString("email");
                                        String firstName = object.getString("first_name");
                                        String lastName = object.getString("last_name");
                                        String genderFull = gender.equals(R.string.gender_f)?"F":"M";
                                        genericProgress = new GenericProgress(mProgressView, mLoginFormView, getApplicationContext());
                                        genericProgress.showProgress(true);
                                        UsuarioDTO newUSer = new UsuarioDTO();
                                        newUSer.setVerificador(fbId);
                                        newUSer.setRedSocial(LoginEnum.facebook.name());
                                        mUserLoginSocialTask = new UserLoginSocialTask(newUSer,getApplicationContext(),genericProgress,LoginEnum.facebook.name());
                                        mUserLoginSocialTask.execute((Void) null);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("GraphResponse", "-------------" + response.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,gender,birthday,email,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Toast toast = Toast.makeText(getApplicationContext(), "Se cancelo", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast toast = Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        loginButtonTwitter = (TwitterLoginButton) findViewById(R.id.login_button_twtr);
        for(Feature feature : featureList){
            if(feature.getFeature().equals("gestionUsuario.redesSociales.twitter.excludes")){
                if(feature.getValue().equals("false")){
                    loginButtonTwitter.setVisibility(View.GONE);
                    break;
                }
            }
        }


    }

    private void updateUI() {

        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

        Profile profile = Profile.getCurrentProfile();
        if (profile == null) {
            Log.e("Profile", "null");
        }
        if (enableButtons && profile != null) {
            Log.e("Access Token", AccessToken.getCurrentAccessToken().toString());
            Log.e("TabSocial", profile.getName());
        }
    }

    /**
     * Attempts login an user.
     */
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(email)){
            mEmailView.setError(getString(R.string.error_email_required));
            focusView = mEmailView;
            cancel = true;
        } else if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_password_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        }else{
            genericProgress = new GenericProgress(mProgressView,mLoginFormView,getApplicationContext());
            genericProgress.showProgress(true);
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setLogin(email);
            try {
                usuarioDTO.setCredencial(JasyptUtils.encryptPassword(password));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mAuthTask = new UserLoginTask(usuarioDTO,getApplicationContext(),genericProgress,LoginEnum.cycletrip.name());
            mAuthTask.execute((Void) null);
        }



    }
    public void createUser(View v){
        Intent intent= new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public static List<Feature> getFeatureList() {
        return featureList;
    }
}