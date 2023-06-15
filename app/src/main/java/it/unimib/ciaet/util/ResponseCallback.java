package it.unimib.ciaet.util;

import java.util.List;

import it.unimib.ciaet.model.CriptoCurrency;


/**
 * Interface to send data from Repositories to Activity/Fragment.
 */
public interface ResponseCallback {
    void onSuccess(List<CriptoCurrency> newsList, long lastUpdate);
    void onFailure(String errorMessage);
}
