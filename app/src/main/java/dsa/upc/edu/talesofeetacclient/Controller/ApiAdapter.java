package dsa.upc.edu.talesofeetacclient.Controller;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mike on 4/12/17.
 */

public class ApiAdapter {
    private static ApiService API_SERVICE;

    public static ApiService getApiService(String baseUrl) {
        // Creating the interceptor, and setting the log level
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        //Patrón singleton
        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // <-- using the log level
                    .build();
            API_SERVICE = retrofit.create(ApiService.class);
        }

        return API_SERVICE;
    }
}
