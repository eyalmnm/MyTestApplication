package tests.em_projects.com.mytestapplication.billing;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tests.em_projects.com.mytestapplication.billing.products.Product;
import tests.em_projects.com.mytestapplication.billing.util.IabHelper;
import tests.em_projects.com.mytestapplication.billing.util.IabResult;
import tests.em_projects.com.mytestapplication.billing.util.Inventory;
import tests.em_projects.com.mytestapplication.billing.util.Purchase;
import tests.em_projects.com.mytestapplication.billing.util.SkuDetails;
import tests.em_projects.com.mytestapplication.utils.StringUtils;

/**
 * Created by eyal muchtar on 23/03/2017.
 */

public class InAppBillingService extends Service {

    // Actions for the BroadcastReceiver
    private static final String PACKAGE_NAME = "";
    public static final String ACTION_START_IN_APP_BILLING_SERVICE_SUCCESS = PACKAGE_NAME + ".start_in_app_billing_service_success";
    public static final String ACTION_START_IN_APP_BILLING_SERVICE_FAILED = PACKAGE_NAME + ".start_in_app_billing_service_failed";
    public static final String ACTION_RETRIEVE_PRODUCT_DETAILS_SUCCESS = PACKAGE_NAME + ".retrieve_product_details_success";
    public static final String ACTION_RETRIEVE_PRODUCT_DETAILS_FAILED = PACKAGE_NAME + ".retrieve_product_details_failed";
    public static final String ACTION_PURCHASE_ITEM_SUCCESS = PACKAGE_NAME + ".purchase_item_success";
    public static final String ACTION_PURCHASE_ITEM_FAILED = PACKAGE_NAME + ".purchase_item_failed";
    public static final String ACTION_QUERY_PURCHASED_ITEMS_SUCCESS = PACKAGE_NAME + ".query_purchased_items_success";
    public static final String ACTION_QUERY_PURCHASED_ITEMS_FAILED = PACKAGE_NAME + ".query_purchased_items_failed";
    public static final String ACTION_CONSUME_ITEM_SUCCESS = PACKAGE_NAME + ".consume_item_success";
    public static final String ACTION_CONSUME_ITEM_FAILED = PACKAGE_NAME + ".consume_item_failed";

    private static final String TAG = "InAppBillingService";
    // The Generated key by Google Play
    private final String base64EncodedPublicKey = "CONSTRUCT_YOUR_KEY_AND_PLACE_IT_HERE";
    // Registered products' skus // TODO
    private final String[] productSku = {"product_sku", "product_sku", "product_sku", "product_sku"};
    private IBinder binder = new LocalBinder();
    // Service class which is responsible for the connection
    private IabHelper iabHelper;
    // Hold a reference to our Google Play Inventory
    private Inventory inventory;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        // Create the helper.
        iabHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging.
        iabHelper.enableDebugLogging(true);

        // Start set up and bind the In-app Billing service
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {  // A problem has occurs
                    // Oh no, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    Intent intent = new Intent(ACTION_START_IN_APP_BILLING_SERVICE_SUCCESS);
                    intent.putExtra("result", result.getResponse());
                    sendBroadcast(intent);
                } else {
                    // Hooray, IAB is fully set up!
                    Log.d(TAG, "In-app Billing setting up completed successfully");
                    Intent intent = new Intent(ACTION_START_IN_APP_BILLING_SERVICE_FAILED);
                    intent.putExtra("result", result.getResponse());
                    sendBroadcast(intent);
                }
            }
        });
    }

    /**
     * Retrieve the registered items
     */
    public void retrieveProductDetails() {
        final List<String> skuList = createSkusList(productSku);
        IabHelper.QueryInventoryFinishedListener queryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                if (true == result.isFailure()) {
                    String errMsg = result.getMessage();
                    Log.d(TAG, "queryFinishedListener: " + errMsg);
                    Intent intent = new Intent(ACTION_RETRIEVE_PRODUCT_DETAILS_FAILED);
                    intent.putExtra("result", result.getResponse());
                    sendBroadcast(intent);
                }
                InAppBillingService.this.inventory = inventory;
                SkuDetails skuDetails;
                String title, description, price, type, currencyCode;
                ArrayList<Product> productsArray = new ArrayList<>(productSku.length);
                for (String sku : productSku) {
                    skuDetails = inventory.getSkuDetails(sku);
                    title = skuDetails.getTitle();
                    description = skuDetails.getDescription();
                    price = skuDetails.getPrice();
                    type = skuDetails.getType();
                    currencyCode = skuDetails.getPriceCurrencyCode();
                    Product product = new Product(sku, description, price, currencyCode, title, type);
                    productsArray.add(product);
                    Intent intent = new Intent(ACTION_RETRIEVE_PRODUCT_DETAILS_SUCCESS);
                    intent.putExtra("result", result.getResponse());
                    intent.putExtra("products", productsArray);
                    sendBroadcast(intent);
                }
            }
        };
        try {
            iabHelper.queryInventoryAsync(true, skuList, null, queryFinishedListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.e(TAG, "onCreate -> queryInventoryAsync", e);
        }
    }

    private ArrayList<String> createSkusList(String[] productSku) {
        ArrayList arrayList = new ArrayList(productSku.length);
        for (String sku : productSku)
            arrayList.add(sku);
        return arrayList;
    }

    /**
     * Used for execute a porches an item
     *
     * @param activity     the calling activity
     * @param sku          Item ID
     * @param requestCode  a code like in startActivityForResults
     * @param securityCode is a string token that uniquely identifies this purchase request
     * @throws Exception
     */
    public void purchaseItem(Activity activity, final String sku, int requestCode, final String securityCode) throws Exception {
        if (StringUtils.isContained(productSku, sku)) {
            iabHelper.launchPurchaseFlow(activity, sku, requestCode, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    if (true == result.isFailure()) {
                        String errMsg = result.getMessage();
                        Log.d(TAG, "purchaseItem: " + errMsg);
                        Intent intent = new Intent(ACTION_PURCHASE_ITEM_FAILED);
                        intent.putExtra("result", result.getResponse());
                        sendBroadcast(intent);
                    } else if (info.getSku().equals(sku)) {
//                        if (true == info.getDeveloperPayload().equals(securityCode)) {
                        String orderId = info.getOrderId();
                        Intent intent = new Intent(ACTION_PURCHASE_ITEM_SUCCESS);
                        intent.putExtra("result", result.getResponse());
                        sendBroadcast(intent);
//                        } else {
                        // inform UI about this unknown purchase
//                        }
//                    } else {
                        // inform UI about this unknown purchase.
                    }
                }
            }, securityCode);
        } else {
            throw new Exception("Invalid product ID (SKU): " + sku);
        }
    }

    /**
     * Query Purchased Items
     *
     * @param skus item Id (not title)
     * @throws IabHelper.IabAsyncInProgressException
     */
    public void queryPurchasedItems(final String[] skus) throws IabHelper.IabAsyncInProgressException {
        iabHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                if (true == result.isFailure()) {
                    String errMsg = result.getMessage();
                    Log.d(TAG, "queryPurchasedItems: " + errMsg);
                    Intent intent = new Intent(ACTION_QUERY_PURCHASED_ITEMS_FAILED);
                    intent.putExtra("result", result.getResponse());
                    sendBroadcast(intent);
                } else {
                    ArrayList<Purchase> purchases = new ArrayList<Purchase>(skus.length);
                    for (String sku : skus) {
                        if (null != inv.getPurchase(sku)) {
                            purchases.add(inv.getPurchase(sku));
                        }
                    }
                    Intent intent = new Intent(ACTION_QUERY_PURCHASED_ITEMS_SUCCESS);
                    intent.putExtra("result", result.getResponse());
                    intent.putExtra("purchased_item", purchases);
                    sendBroadcast(intent);
                }
            }
        });
    }

    /**
     * Request for consume a purchase
     *
     * @param sku the item's Id to be consumed.
     * @throws Exception
     */
    public void consumeItem(String sku) throws Exception {
        if (true == StringUtils.isNullOrEmpty(sku)) throw new Exception("sku is null");
        if (null == inventory)
            throw new Exception("Inventory is null. Please retrieveProductDetails first!");
        Purchase purchase = inventory.getPurchase(sku);
        if (null == purchase) throw new Exception("purchase is null");
        iabHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                if (true == result.isFailure()) {
                    String errMsg = result.getMessage();
                    Log.d(TAG, "consumeItem: " + errMsg);
                    Intent intent = new Intent(ACTION_CONSUME_ITEM_FAILED);
                    intent.putExtra("result", result.getResponse());
                    sendBroadcast(intent);
                    return;
                } else {
                    Intent intent = new Intent(ACTION_CONSUME_ITEM_SUCCESS);
                    intent.putExtra("result", result.getResponse());
                    sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (iabHelper != null) {
            try {
                iabHelper.dispose(); // Unbind from the In-app Billing service
            } catch (IabHelper.IabAsyncInProgressException e) {
            }
        }
        iabHelper = null;
        inventory = null;
    }

    //------------------------------------------   Binding the service helpers methods   -----------
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public InAppBillingService getService() {
            return InAppBillingService.this;
        }
    }
    //------------------------------------------   Binding the service helpers methods   -----------
}
