package com.sofactory.capitalcycles.cycletrip.Utils.Services;

/**
 * Created by LuisSebastian on 10/17/15.
 */
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.sofactory.capitalcycles.cycletrip.Activities.LoginActivity;
import com.sofactory.capitalcycles.cycletrip.Utils.Preferences.UserPreferences;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    String SENDER_ID = "779455017360";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(SENDER_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(UserPreferences.TOKEN_ID, token).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(UserPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {



    }

}
