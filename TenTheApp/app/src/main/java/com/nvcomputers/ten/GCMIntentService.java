package com.nvcomputers.ten;/*
package com.nvcomputers.ten;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.nvcomputers.ten.utils.Const;
import com.nvcomputers.ten.utils.GCMUtilities;
import com.nvcomputers.ten.utils.PreferenceHandler;
import com.nvcomputers.ten.utils.ServerUtilities;

*/
/**
 * Created by rkumar4 on 5/18/2017.
 *//*


public class GCMIntentService extends GCMBaseIntentService {

    private PreferenceHandler pref;

    public GCMIntentService() {
        super(Const.GCM_APP_KEY);
    }
    @Override
    protected void onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
    }

    @Override
    protected void onError(Context context, String errorId) {
        GCMUtilities.displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        //Utilities.showVLog(TAG, "onRegistered:: Device registered: regId = " + registrationId);
        GCMUtilities.displayMessage(context, getString(R.string.gcm_registered));
        if (!registrationId.equals("")) {
            pref = new PreferenceHandler(context);
            pref.getPreferences(context);
            pref.writeString(context, pref.PREF_DEVICE_ID, registrationId);
        }
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        //Utilities.showVLog(TAG, "Device unregistered");
        GCMUtilities.displayMessage(context, getString(R.string.gcm_unregistered));

        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, registrationId);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            //Utilities.showVLog(TAG, "Ignoring unregister callback");
        }
    }
}
*/
