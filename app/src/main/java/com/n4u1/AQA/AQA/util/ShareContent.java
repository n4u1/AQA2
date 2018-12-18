package com.n4u1.AQA.AQA.util;

import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class ShareContent {
    private String contentKey, pollMode;
    private Uri shortLink;

    public ShareContent() {
        shortLink = Uri.parse("https://aqapoll.page.link/AQA");
    }

    public ShareContent(String contentKey, String pollMode) {

        Log.d("lkj ShareContent C", contentKey);
        Log.d("lkj ShareContent P", pollMode);
        this.contentKey = contentKey;
        this.pollMode = pollMode;
        makeUrl();
    }


    public String getShareUrl() {
//        Log.d("lkj getShareUrl", shortLink.toString());
        return shortLink.toString();
    }

    private void makeUrl() {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(getPromotionDeepLink())
                .setDynamicLinkDomain("aqapoll.page.link")
//                .setDomainUriPrefix("aqapoll.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink()
                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        Log.d("lkj dyna Complete", "dyna Comp");
                        if (task.isSuccessful()) {
                            shortLink = task.getResult().getShortLink();
                        } else {
                            Log.w("lkj fail??", task.toString());
                            reTryMakeUrl();
                        }
                    }
                });
    }

    private void reTryMakeUrl() {
        makeUrl();
    }

    private Uri getPromotionDeepLink() {
        Log.d("lkj pro key", contentKey);
        Log.d("lkj pro pMode", pollMode);
        return Uri.parse("https://aqa.ranking.com/" + "share" + "?" + "contentKey" + "=" + contentKey
                + "&pollMode=" + pollMode);
    }

}
