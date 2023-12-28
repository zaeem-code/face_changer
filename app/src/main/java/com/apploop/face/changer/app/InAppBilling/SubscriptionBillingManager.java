package com.apploop.face.changer.app.InAppBilling;

import static com.android.billingclient.api.BillingClient.ProductType.SUBS;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryPurchasesParams;
import com.android.billingclient.api.SkuDetailsParams;
import com.apploop.face.changer.app.utils.Constants;
import com.apploop.face.changer.app.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionBillingManager implements PurchasesUpdatedListener {

    private final String TAG = "billingmanager";
    public static final String PREF_FILE = "prefs";
    public static final String  SUBSCRIBE_KEY = String.valueOf(Constants.ADS_ID_KEY);
    public static String ITEM_SKU_SUBSCRIBE = "";

    private static SubscriptionBillingManager subscriptionBillingManager;

    private Context context;
    private Activity activity;

    public BillingClient billingClient;

    public static SubscriptionBillingManager getInstance(Context context, Activity activity) {
        if (subscriptionBillingManager == null) {
            subscriptionBillingManager = new SubscriptionBillingManager(context, activity);
        }
        return subscriptionBillingManager;
    }

    public SubscriptionBillingManager(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        billingClient = BillingClient
                .newBuilder(context)
                .enablePendingPurchases()
                .setListener(this)
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NotNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                   /* Purchase.PurchasesResult queryPurchase = null;
                    queryPurchase = billingClient.queryPurchases(SUBS);
                    billingClient.queryPurchaseHistoryAsync(SUBS, (billingResult1, list) -> {
                        //Toast.makeText(context, list.size() + "", Toast.LENGTH_SHORT).show();
                    });
                    List<Purchase> queryPurchases = queryPurchase.getPurchasesList();
                    if (queryPurchases != null && queryPurchases.size() > 0) {
                        handlePurchases(queryPurchases);
                    }
                    //if no item in purchase list means subscription is not subscribed
                    //Or subscription is cancelled and not renewed for next month
                    // so update pref in both cases
                    // so next time on app launch our premium content will be locked again
                    else {
                        saveSubscribeValueToPref(false);
                    }*/
                    billingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(SUBS).build(),
                            new PurchasesResponseListener() {
                                public void onQueryPurchasesResponse(
                                        @NonNull BillingResult billingResult,
                                        @NonNull List<Purchase> purchases) {
                                    // Process the result
                                    if(purchases.size()>0){
                                        handlePurchases(purchases);
                                    }else {
                                        saveSubscribeValueToPref(false);
                                    }
                                }
                            });

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                //billingClient.startConnection(this);
                //Toast.makeText(context.getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
            }
        });

        //item subscribed
        if (getSubscribeValueFromPref()) {
            Log.d(TAG, "Subscription Status : Subscribed");
        }
        //item not subscribed
        else {
            Log.d(TAG, "Subscription Status : Not Subscribed");
        }
    }

    //initiate purchase on subBtn click
    public void subscribe(String product_id) {
        ITEM_SKU_SUBSCRIBE = product_id;
        //check if service is already connected
        if (billingClient.isReady()) {
            initiatePurchase();
        }
        //else reconnect service
        else {
            billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(@NotNull BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase();
                    } else {
                        //Toast.makeText(context.getApplicationContext(), "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    //Toast.makeText(context.getApplicationContext(), "Service Disconnected ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initiatePurchase() {
        List<String> skuList = new ArrayList<>();
        skuList.add(ITEM_SKU_SUBSCRIBE);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        BillingResult billingResult = null;
        params.setSkusList(skuList).setType(SUBS);
        billingResult = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            billingClient.querySkuDetailsAsync(params.build(),
                    (billingResult1, skuDetailsList) -> {
                        if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetailsList.get(0))
                                        .build();
                                billingClient.launchBillingFlow(activity, flowParams);
                            } else {
                                //try to add subscription item "sub_example" in google play console
                                Toast.makeText(context.getApplicationContext(), "Item not Found "+ITEM_SKU_SUBSCRIBE, Toast.LENGTH_SHORT).show();
                            }
                        } else {
//                            Toast.makeText(context.getApplicationContext(),
//                                    " Error " + billingResult1.getDebugMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context.getApplicationContext(),
                    "Sorry Subscription not Supported. Please Update Play Store", Toast.LENGTH_SHORT).show();
        }
    }

    void handlePurchases(List<Purchase> purchases) {
        for (Purchase purchase : purchases) {
            purchase.getSkus().contains(ITEM_SKU_SUBSCRIBE);
            //if item is purchased
            if (purchase.getSkus().contains(ITEM_SKU_SUBSCRIBE) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    // Invalid purchase
                    // show error to user
                    Toast.makeText(context.getApplicationContext(), "Error : invalid Purchase", Toast.LENGTH_SHORT).show();
                    return;
                }
                // else purchase is valid
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase);
                }
                //else item is purchased and also acknowledged
                else {
                    // Grant entitlement to the user on item purchase
                    // restart activity
                    /*if(!getSubscribeValueFromPref()){
                        saveSubscribeValueToPref(true);
                        Toast.makeText(context.getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
                        activity.recreate();
                    }*/
                    saveSubscribeValueToPref(true);
                    Toast.makeText(context.getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
                    activity.recreate();
                }
            }
            //if purchase is pending
            else if (purchase.getSkus().contains(ITEM_SKU_SUBSCRIBE) && purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                Toast.makeText(context.getApplicationContext(),
                        "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
            }
            //if purchase is unknown mark false
            else if (purchase.getSkus().contains(ITEM_SKU_SUBSCRIBE) && purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                saveSubscribeValueToPref(false);
                //premiumContent.setVisibility(View.GONE);
                //subscribe.setVisibility(View.VISIBLE);
                //subscriptionStatus.setText("Subscription Status : Not Subscribed");
                Toast.makeText(context.getApplicationContext(), "Purchase Status Unknown", Toast.LENGTH_SHORT).show();
            }
        }
    }

    AcknowledgePurchaseResponseListener ackPurchase = billingResult -> {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            //if purchase is acknowledged
            // Grant entitlement to the user. and restart activity
            saveSubscribeValueToPref(true);
            activity.recreate();
        }
    };

    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhiqPfb1kxIl67XVNmlafHlkl1Yf6dHI5+9+755Je+o6q576F1GjGrOleCV7V5zMjts67YE4sskZI9dJ5mObfCL80S1EugzUAWCZdH9FpFCCAlPBT65XG8+P0T44j+wOfOtRaYPNx4xls7Mm64KcEXSSQTBsBjLEB0OCWZHAToryqdCw+C3OYYRfGvZ7ZGlW4PrTOZtNW8zl5ILKXsNc0EBMpMUuk2U8IRyBQFZ8G0ZuaAI6CaEF7NBFdXKgTIX8LYO31JNSS+9q7xaImELdPbTzitvZZHtTcoo5h+k5BI+qk/AN4+khC/y87UeK2ih0tQ5/ui03d7gCwOF89MsQpMQIDAQAB";
            //Toast.makeText(context,"Signature: " + signature,Toast.LENGTH_SHORT).show();
            return Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }

    private boolean getSubscribeValueFromPref() {
        return getPreferenceObject().getBoolean(SUBSCRIBE_KEY, false);
    }

    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }

    private void saveSubscribeValueToPref(boolean value) {
//        preferencesManager.put(PreferenceManager.Key.IS_APP_ADS_FREE, value);
        getPreferenceEditObject().putBoolean(SUBSCRIBE_KEY, value).commit();
    }

    private SharedPreferences getPreferenceObject() {
        return context.getApplicationContext().getSharedPreferences(PREF_FILE, 0);
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        //if item subscribed
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases);
        }
        //if item already subscribed then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
//            Purchase.PurchasesResult queryAlreadyPurchasesResult = null;
//            queryAlreadyPurchasesResult = billingClient.queryPurchases(SUBS);
//            List<Purchase> alreadyPurchases = queryAlreadyPurchasesResult.getPurchasesList();
//            if (alreadyPurchases != null) {
//                handlePurchases(alreadyPurchases);
//            }
            billingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(SUBS).build(),
                    new PurchasesResponseListener() {
                        public void onQueryPurchasesResponse(
                                @NonNull BillingResult billingResult,
                                @NonNull List<Purchase> purchases) {
                            // Process the result
                            if(purchases.size()>0){
                                handlePurchases(purchases);
                            }
                        }
                    });
        }
        //if Purchase canceled
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(context.getApplicationContext(), "Purchase Canceled", Toast.LENGTH_SHORT).show();
        }
        // Handle any other error msgs
        else {
            //Toast.makeText(context.getApplicationContext(), "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}