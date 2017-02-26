package pl.adriankremski.coolector.repository;

import io.reactivex.Observable;
import pl.adriankremski.coolector.model.Statistics;
import pl.adriankremski.coolector.network.Api;

/**
 * Created by adriankremski on 26/02/17.
 */

public class StatisticsZipObservable {
    public static Observable<Statistics> getStatistics(Api api) {
        return Observable.zip(api.loadCategoriesStatistics(), api.loadTagStatistics(), (catStatistics, tagStatistics) -> new Statistics(catStatistics, tagStatistics));
    }
}
