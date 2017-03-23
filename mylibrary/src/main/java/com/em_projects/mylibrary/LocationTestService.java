package com.em_projects.mylibrary;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by eyal on 27/04/16.
 */
public class LocationTestService extends Service implements GoogleApiClient.ConnectionCallbacks {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
