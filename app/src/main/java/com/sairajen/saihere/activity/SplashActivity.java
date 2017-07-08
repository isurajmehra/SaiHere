package com.sairajen.saihere.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.sairajen.saihere.R;
import com.sairajen.saihere.connection.API;
import com.sairajen.saihere.connection.RestAdapter;
import com.sairajen.saihere.connection.callbacks.CallbackCategories;
import com.sairajen.saihere.model.Category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    public static final String EXTRA_OBJC = "EXTRA_OBJC";
    private Call<CallbackCategories> callbackCallCategories = null;
    public static List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        categoryList = new ArrayList<>();
        requestCategoriesApi();

    }

    private void requestCategoriesApi() {
        API api = RestAdapter.createAPI();
        callbackCallCategories = api.getAllCategories();
        callbackCallCategories.enqueue(new Callback<CallbackCategories>() {
            @Override
            public void onResponse(Call<CallbackCategories> call, Response<CallbackCategories> response) {
                CallbackCategories resp = response.body();
                if (resp != null && resp.status.equals("ok")) {
                    categoryList = resp.categories;
                    Intent intent = new Intent(SplashActivity.this,Home.class);
                    intent.putExtra(EXTRA_OBJC, (Serializable) categoryList);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } else {
                    Toast.makeText(SplashActivity.this,"Error occurred !",Toast.LENGTH_SHORT).show();
                    SplashActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<CallbackCategories> call, Throwable t) {
                Toast.makeText(SplashActivity.this,"Error occurred !",Toast.LENGTH_SHORT).show();
                SplashActivity.this.finish();
            }

        });
    }

}
