package it.unimib.ciaet.repository;



import static it.unimib.ciaet.util.Constants.FRESH_TIMEOUT;
import static it.unimib.ciaet.util.Constants.GRAPHS_API_KEY;
import static it.unimib.ciaet.util.Constants.GRAPHS_API_OTHER_URL;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import it.unimib.ciaet.R;
import it.unimib.ciaet.database.CiaetDao;
import it.unimib.ciaet.database.CiaetRoomDatabase;
import it.unimib.ciaet.model.CriptoCurrency;
import it.unimib.ciaet.model.GraphsApiResponse;
import it.unimib.ciaet.service.GraphsApiService;
import it.unimib.ciaet.util.ResponseCallback;
import it.unimib.ciaet.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository to get the news using the API
 * provided by NewsApi.org (https://newsapi.org).
 */
public class GraphsRepository implements IGraphsRepository {


    private final Application application;
    private final GraphsApiService graphsApiService;
    private final CiaetDao ciaetDao;
    private final ResponseCallback responseCallback;

    public GraphsRepository(Application application, ResponseCallback responseCallback) {
        this.application = application;
        this.graphsApiService = ServiceLocator.getInstance().getNewsApiService();
        CiaetRoomDatabase graphsRoomDatabase = ServiceLocator.getInstance().getCiaetDao(application);
        this.ciaetDao = graphsRoomDatabase.newsDao();
        this.responseCallback = responseCallback;
    }

    @Override
    public void fetchCurrency(int page, long lastUpdate) {

        long currentTime = System.currentTimeMillis();

        // It gets the news from the Web Service if the last download
        // of the news has been performed more than FRESH_TIMEOUT value ago

        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            Call<GraphsApiResponse> graphsResponseCall = graphsApiService.getGraphs(GRAPHS_API_KEY);

            graphsResponseCall.enqueue(new Callback<GraphsApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<GraphsApiResponse> call,
                                       @NonNull Response<GraphsApiResponse> response) {





                    if (response.body() != null && response.isSuccessful() && !response.body().getStatus().equals("error")) {
                        List<CriptoCurrency> graphsList = response.body().getCurrencies();
                        saveDataInDatabase(graphsList);
                    } else {
                        responseCallback.onFailure(application.getString(R.string.error_retrieving_graphs));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GraphsApiResponse> call, @NonNull Throwable t) {
                    responseCallback.onFailure(t.getMessage());
                }
            });
        } else {
            readDataFromDatabase(lastUpdate);
        }
    }



    public void updateCurrency(CriptoCurrency currency) {
        CiaetRoomDatabase.databaseWriteExecutor.execute(() -> {
            ciaetDao.updateSingleFavoriteNews(currency);
        });
    }







    private void saveDataInDatabase(List<CriptoCurrency> currenciesList) {
        CiaetRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<CriptoCurrency> allGraphs = ciaetDao.getAll();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (CriptoCurrency currency : allGraphs) {
                // This check works because News and NewsSource classes have their own
                // implementation of equals(Object) and hashCode() methods
                if (currenciesList.contains(currency)) {
                    // The primary key and the favorite status is contained only in the News objects
                    // retrieved from the database, and not in the News objects downloaded from the
                    // Web Service. If the same news was already downloaded earlier, the following
                    // line of code replaces the the News object in newsList with the corresponding
                    // News object saved in the database, so that it has the primary key and the
                    // favorite status.
                    currenciesList.set(currenciesList.indexOf(currency), currency);
                }
            }

            // Writes the news in the database and gets the associated primary keys
            List<Long> insertedNewsIds = ciaetDao.insertNewsList(currenciesList);
            for (int i = 0; i < currenciesList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                currenciesList.get(i).setIdentificativo(insertedNewsIds.get(i));
            }

            responseCallback.onSuccess(currenciesList, System.currentTimeMillis());
        });
    }

    /**
     * Gets the news from the local database.
     * The method is executed in a Runnable because the database access
     * cannot been executed in the main thread.
     */
    private void readDataFromDatabase(long lastUpdate) {
        CiaetRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(ciaetDao.getAll(), lastUpdate);
        });
    }
}
