package it.unimib.ciaet.service;

import static it.unimib.ciaet.util.Constants.GRAPHS_API_OTHER_URL;
import static it.unimib.ciaet.util.Constants.TOP_HEADLINES_ENDPOINT;


import it.unimib.ciaet.model.GraphsApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Interface for Service to get news from the Web Service.
 */
public interface GraphsApiService {
    @GET(GRAPHS_API_OTHER_URL)
    Call<GraphsApiResponse> getGraphs(
                                      @Header("X-CMC_PRO_API_KEY") String apiKey);
}
