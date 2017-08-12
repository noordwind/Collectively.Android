package com.noordwind.apps.collectively.domain.interactor.remark

import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class LoadRemarkCategoriesUseCase(val remarksRepository: RemarksRepository,
                                  val translationsDataSource: FiltersTranslationsDataSource,
                                  useCaseThread: UseCaseThread,
                                  postExecutionThread: PostExecutionThread) : UseCase<List<RemarkCategory>, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<RemarkCategory>> {
        return remarksRepository.loadRemarkCategories().flatMap {
            it.forEach {
                it.translation = translationsDataSource.translateFromType(it.name)
            }

            Observable.just(it)
        }
    }
}