package ru.samsung.case2022.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Retrofit class
 * @author Philipp Schepnov
 * This class is used to create base variables and methods to connect to server
 */

public class RetrofitClient {

    // Instance of class
    private static RetrofitClient instance = null;
    // Class that contains GET and POST request methods
    private final Api api;

    /**
     * Constructor of class
     * Is used to setup request params for all requests.
     */

    private RetrofitClient() {
        // Create main request variable and setup base url and response conversion type
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(Api.class);
    }

    /**
     * This method is used to get class instance
     * @return RetrofitClient
     */

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    /**
     * This method is used to get request api
     * @return current api
     */

    public Api getApi() {
        return api;
    }
}

