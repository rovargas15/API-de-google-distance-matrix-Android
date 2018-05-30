package com.example.empresariales.empresariales.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.empresariales.empresariales.R;
import com.example.empresariales.empresariales.model.DataModel;
import com.example.empresariales.empresariales.util.ApplicationController;
import com.example.empresariales.empresariales.util.util;
import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class MainPresenter extends Presenter<MainPresenter.View> {

    public MainPresenter() {
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void calcDistance(Place start, Place finish) {

        if(new util().isConnectingToInternet()) {
            getView().showLoading();

            String origin = "&origins=" + start.getLatLng().latitude + "," + start.getLatLng().longitude;
            String destination = "&destinations=" + finish.getLatLng().latitude + "," + finish.getLatLng().longitude;
            String key = "&key=" + getContext().getString(R.string.GOOGLE_KEY);
            String language = "&language=es";

            String url = getContext().getString(R.string.urlPrincipal) + origin + destination + key + language;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Gson gson = new Gson();
                                List<DataModel> item = Collections.singletonList(gson.fromJson(response.toString(), DataModel.class));
                                getView().show(item);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                getView().hideLoading();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getMessage() != null)
                                Log.e("error ", error.getMessage());
                            getView().hideLoading();
                        }
                    }
            );

            Volley.newRequestQueue(getContext()).add(request);
        }else{
            Toast.makeText(getContext(), "No se encuentra conectado a una red.", Toast.LENGTH_SHORT).show();
        }
    }

    private Context getContext() {
        return ApplicationController.getInstance();
    }

    public interface View extends Presenter.View {
        void show(List<DataModel> filesList);
    }

}
