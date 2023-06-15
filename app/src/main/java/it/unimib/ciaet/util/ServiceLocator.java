package it.unimib.ciaet.util;

import android.app.Application;

import it.unimib.ciaet.database.CiaetRoomDatabase;
import it.unimib.ciaet.service.GraphsApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 *  Registry to provide the dependencies for the classes
 *  used in the application.
 */
public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * It creates an instance of NewsApiService using Retrofit.
     * @return an instance of NewsApiService.
     */
    public GraphsApiService getNewsApiService() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.GRAPHS_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(GraphsApiService.class);
    }

    public CiaetRoomDatabase getCiaetDao(Application application) {
        return CiaetRoomDatabase.getDatabase(application);
    }
}
