package com.abhishekpathak.posts.details.di

import com.abhishekpathak.posts.commons.data.local.PostDb
import com.abhishekpathak.posts.commons.data.remote.PostService
import com.abhishekpathak.posts.core.networking.Scheduler
import com.abhishekpathak.posts.details.DetailsActivity
import com.abhishekpathak.posts.details.model.DetailsDataContract
import com.abhishekpathak.posts.details.model.DetailsLocalData
import com.abhishekpathak.posts.details.model.DetailsRemoteData
import com.abhishekpathak.posts.details.model.DetailsRepository
import com.abhishekpathak.posts.details.viewmodel.DetailsViewModelFactory
import com.abhishekpathak.posts.list.di.ListComponent
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@DetailsScope
@Component(dependencies = [ListComponent::class], modules = [DetailsModule::class])
interface DetailsComponent {
    fun inject(detailsActivity: DetailsActivity)
}

@Module
class DetailsModule {

    /*ViewModel*/
    @Provides
    @DetailsScope
    fun detailsViewModelFactory(repo: DetailsDataContract.Repository, compositeDisposable: CompositeDisposable): DetailsViewModelFactory {
        return DetailsViewModelFactory(repo, compositeDisposable)
    }

    /*Repository*/
    @Provides
    @DetailsScope
    fun detailsRepo(local: DetailsDataContract.Local, remote: DetailsDataContract.Remote, scheduler: Scheduler, compositeDisposable: CompositeDisposable)
            : DetailsDataContract.Repository = DetailsRepository(local, remote, scheduler, compositeDisposable)

    @Provides
    @DetailsScope
    fun remoteData(postService: PostService): DetailsDataContract.Remote = DetailsRemoteData(postService)

    @Provides
    @DetailsScope
    fun localData(postDb: PostDb, scheduler: Scheduler): DetailsDataContract.Local = DetailsLocalData(postDb, scheduler)

    @Provides
    @DetailsScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
}