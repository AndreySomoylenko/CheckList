package ru.samsung.case2022.retrofit;

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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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