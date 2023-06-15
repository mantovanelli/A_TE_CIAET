package it.unimib.ciaet.repository;
import it.unimib.ciaet.model.CriptoCurrency;

/**
 * Interface for Repositories that manage News objects.
 */
public interface IGraphsRepository {

    enum JsonParserType {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };

    void fetchCurrency(int page, long lastUpdate);


}
