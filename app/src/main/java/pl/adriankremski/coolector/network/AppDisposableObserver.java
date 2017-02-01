package pl.adriankremski.coolector.network;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import io.reactivex.observers.DisposableObserver;

public class AppDisposableObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            onNetworkError();
        }
    }

    public void onNetworkError() {

    }

    @Override
    public void onComplete() {

    }
}
