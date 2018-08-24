package com.houz.chef.apiBase;

import android.content.Context;
import android.util.Log;

import com.houz.chef.utils.Preferences;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FetchServiceBase {
    private static String BASE_URL = "http://app.houzchef.com/public/api/";

    private static Retrofit GetRestAdapter(final Context context) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        };

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("DATA", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(loggingInterceptor)
                .addInterceptor(interceptor)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(45, TimeUnit.SECONDS);

        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();

    }

    public static FetchServiceInterFace getFetcherService(Context context) {
        return GetRestAdapter(context).create(FetchServiceInterFace.class);
    }

    private static Retrofit GetRestAdapterWithToken(final Context context) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                String token = "gca " + new Preferences(context).getSessionToken();
                Log.e("TOKEN", token);
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", token)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        };

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("DATA", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(loggingInterceptor)
                .addInterceptor(interceptor)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(45, TimeUnit.SECONDS);

        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();

    }

    public static FetchServiceInterFace getFetcherServiceWithToken(Context context) {
        return GetRestAdapterWithToken(context).create(FetchServiceInterFace.class);
    }

    private static Retrofit getRestAdapterWithUrl(final Context context, String header) {
        //setup cache
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("DATA", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        String headerUrl= header;
        builder.addInterceptor(loggingInterceptor)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES);

        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(headerUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static FetchServiceInterFace getFetcherServiceWithUrl(Context context, String headerUrl) {
        return getRestAdapterWithUrl(context,headerUrl).create(FetchServiceInterFace.class);
    }
}
