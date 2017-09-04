package com.noordwind.apps.collectively.presentation.extension

import android.location.Location
import com.google.android.gms.maps.model.VisibleRegion
import io.reactivex.Observable


fun <T> T.asObservable() : Observable<T> = Observable.just(this)


fun VisibleRegion.visibleRadius() : Int {
    var distanceWidth = FloatArray(1)
    var distanceHeight = FloatArray(1)

    var farRight = farRight;
    var farLeft = farLeft;
    var nearRight = nearRight;
    var nearLeft = nearLeft;

    //calculate the distance width (left <-> right of map on screen)
    Location.distanceBetween(
            (farLeft.latitude + nearLeft.latitude) / 2,
            farLeft.longitude,
            (farRight.latitude + nearRight.latitude) / 2,
            farRight.longitude,
            distanceWidth
    );

    //calculate the distance height (top <-> bottom of map on screen)
    Location.distanceBetween(
            farRight.latitude,
            (farRight.longitude + farLeft.longitude) / 2,
            nearRight.latitude,
            (nearRight.longitude + nearLeft.longitude) / 2,
            distanceHeight
    );

    //visible radius is (smaller distance) / 2:
    return (if (distanceWidth[0] < distanceHeight[0]) distanceWidth[0] else distanceHeight[0]).toInt()
}
