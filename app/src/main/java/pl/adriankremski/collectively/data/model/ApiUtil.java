package pl.adriankremski.collectively.data.model;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import pl.adriankremski.collectively.data.net.Api;

/**
 * Created by adriankremski on 12/02/17.
 */

public class ApiUtil {

    public static Observable<RemarkNotFromList> pollRemark(Api api, Observable<Operation> operationObservable) {
        return operationObservable.repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                return objectObservable.delay(500, TimeUnit.MILLISECONDS);
            }
            }).takeUntil(new Predicate<Operation>() {
            @Override
            public boolean test(Operation operation) throws Exception {
                return operation.getSuccess();
            }
        }).filter(
                new Predicate<Operation>() {
            @Override
            public boolean test(Operation operation) throws Exception {
                return operation.getSuccess();
            }
        }
        ).flatMap(new Function<Operation, ObservableSource<RemarkNotFromList>>() {
            @Override
            public ObservableSource<RemarkNotFromList> apply(Operation operation) throws Exception {
                return api.createdRemark(operation.getResource());
            }
        });
    }
}
