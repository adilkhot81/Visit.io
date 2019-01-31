package com.example.mohdadil.visit_10;

import android.graphics.Bitmap;

import es.situm.sdk.SitumSdk;
import es.situm.sdk.error.Error;
import es.situm.sdk.model.cartography.Poi;
import es.situm.sdk.utils.Handler;
import java.util.Random;

public class GetPoiCategoryIconUseCase {

    public interface Callback{
        void onSuccess(Bitmap bitmap);
        void onError(String error);
    }

    public void getUnselectedIcon(Poi poi, final Callback callback) {
        SitumSdk.communicationManager().fetchPoiCategoryIconNormal(poi.getCategory(), new Handler<Bitmap>() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                callback.onSuccess(bitmap);
            }

            @Override
            public void onFailure(Error error) {
                callback.onError(error.getMessage());
            }
        });

    }
}
