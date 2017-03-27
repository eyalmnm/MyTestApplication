package tests.em_projects.com.mytestapplication.billing;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

import tests.em_projects.com.mytestapplication.R;

/**
 * Created by eyal muchtar on 23/03/2017.
 */

public class BillingActivity extends Activity {
    private static final String TAG = "BillingActivity";

    private InAppBillingService billingService;
    private ServiceConnection billingServiceConnection;

    private BroadcastReceiver billingServiceReceiver;

    private Button buyItemButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        Log.d(TAG, "onCreate");

        initBillingServiceReceiver();

        bindBillingService();

        buyItemButton = (Button) findViewById(R.id.buyItemButton);
        buyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick");
            }
        });
    }

    private void initBillingServiceReceiver() {
        billingServiceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // ***   For debug only   ***
                Bundle extras = intent.getExtras();
                Set<String> keys = extras.keySet();
                Log.d(TAG, intent.getAction());
                for (String s : keys) {
                    Log.d(TAG, "--- key: " + s + " value: " + extras.get(s));
                }
                // ***   For debug only   ***
                switch (intent.getAction()) {
                    case InAppBillingService.ACTION_START_IN_APP_BILLING_SERVICE_SUCCESS:
                        break;
                    case InAppBillingService.ACTION_START_IN_APP_BILLING_SERVICE_FAILED:
                        break;
                    case InAppBillingService.ACTION_RETRIEVE_PRODUCT_DETAILS_SUCCESS:
                        break;
                    case InAppBillingService.ACTION_RETRIEVE_PRODUCT_DETAILS_FAILED:
                        break;
                    case InAppBillingService.ACTION_PURCHASE_ITEM_SUCCESS:
                        break;
                    case InAppBillingService.ACTION_PURCHASE_ITEM_FAILED:
                        break;
                    case InAppBillingService.ACTION_QUERY_PURCHASED_ITEMS_SUCCESS:
                        break;
                    case InAppBillingService.ACTION_QUERY_PURCHASED_ITEMS_FAILED:
                        break;
                    case InAppBillingService.ACTION_CONSUME_ITEM_SUCCESS:
                        break;
                    case InAppBillingService.ACTION_CONSUME_ITEM_FAILED:
                        break;
                }
            }
        };
    }

    private void bindBillingService() {
        billingServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                billingService = ((InAppBillingService.LocalBinder) service).getService();
                if (null != billingService) {
                    billingService.retrieveProductDetails();
                } else {
                    Toast.makeText(BillingActivity.this, "Failed to initial billing service...", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                billingService = null;
            }
        };
        Intent intent = new Intent(this, InAppBillingService.class);
        bindService(intent, billingServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(billingServiceReceiver, getBillingServiceReceiverIntentFilter());
    }

    private IntentFilter getBillingServiceReceiverIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(InAppBillingService.ACTION_START_IN_APP_BILLING_SERVICE_SUCCESS);
        intentFilter.addAction(InAppBillingService.ACTION_START_IN_APP_BILLING_SERVICE_FAILED);
        intentFilter.addAction(InAppBillingService.ACTION_RETRIEVE_PRODUCT_DETAILS_SUCCESS);
        intentFilter.addAction(InAppBillingService.ACTION_RETRIEVE_PRODUCT_DETAILS_FAILED);
        intentFilter.addAction(InAppBillingService.ACTION_PURCHASE_ITEM_SUCCESS);
        intentFilter.addAction(InAppBillingService.ACTION_PURCHASE_ITEM_FAILED);
        intentFilter.addAction(InAppBillingService.ACTION_QUERY_PURCHASED_ITEMS_SUCCESS);
        intentFilter.addAction(InAppBillingService.ACTION_QUERY_PURCHASED_ITEMS_FAILED);
        intentFilter.addAction(InAppBillingService.ACTION_CONSUME_ITEM_SUCCESS);
        intentFilter.addAction(InAppBillingService.ACTION_CONSUME_ITEM_FAILED);
        return intentFilter;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(billingServiceReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindServiceBillingService();
    }

    private void unbindServiceBillingService() {
        if (null != billingService) {
            unbindService(billingServiceConnection);
        }
    }
}
