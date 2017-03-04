package pl.adriankremski.collectively.repository;

import io.reactivex.Observable;
import pl.adriankremski.collectively.model.Statistics;
import pl.adriankremski.collectively.network.Api;

/**
 * Created by adriankremski on 26/02/17.
 */

public class StatisticsZipObservable {
    public static Observable<Statistics> getStatistics(Api api) {
        return Observable.zip(api.loadCategoriesStatistics(), api.loadTagStatistics(), (catStatistics, tagStatistics) -> new Statistics(catStatistics, tagStatistics));
    }
}
