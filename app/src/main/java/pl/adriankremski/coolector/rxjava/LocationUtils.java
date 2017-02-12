package pl.adriankremski.coolector.rxjava;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.functions.Func1;

import java.util.List;

public class LocationUtils {

    public static Observable<List<Address>> getUserAddress(Context context) {
        ReactiveLocationProvider reactiveLocationProvider = new ReactiveLocationProvider(context);

        return reactiveLocationProvider.getLastKnownLocation().flatMap(new Func1<Location, Observable<List<Address>>>() {
            @Override
            public Observable<List<Address>> call(Location location) {
                return reactiveLocationProvider.getReverseGeocodeObservable(location.getLatitude(), location.getLongitude(), 1);
            }
        });
    }
}
