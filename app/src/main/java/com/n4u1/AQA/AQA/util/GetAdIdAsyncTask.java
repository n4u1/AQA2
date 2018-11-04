package com.n4u1.AQA.AQA.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

public class GetAdIdAsyncTask extends AsyncTask<Void, Void, String> {

    Context mContext;
    String adId;


    public GetAdIdAsyncTask(Context context, String adId) {
        this.mContext = context;
        this.adId = adId;
    }

    @Override
    protected String doInBackground(Void... voids) {
        AdvertisingIdClient.Info idInfo = null;
        try {
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String advertId = null;
        try {
            advertId = idInfo.getId();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        return advertId;
    }

    @Override
    protected void onPostExecute(String advertId) {
        Log.d("lkj advertId", advertId);
        if (advertId != null && advertId.trim().isEmpty()) {
            adId = advertId;
        }



    }
}
