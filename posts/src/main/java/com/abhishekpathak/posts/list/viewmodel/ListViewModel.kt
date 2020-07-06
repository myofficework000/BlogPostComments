package com.abhishekpathak.posts.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.abhishekpathak.posts.commons.PostDH
import com.abhishekpathak.posts.commons.data.PostWithUser
import com.abhishekpathak.posts.commons.data.local.Post
import com.abhishekpathak.posts.core.extensions.toLiveData
import com.abhishekpathak.posts.list.model.ListDataContract
import com.mpaani.core.networking.Outcome
import io.reactivex.disposables.CompositeDisposable

class ListViewModel(private val repo: ListDataContract.Repository,
                    private val compositeDisposable: CompositeDisposable) : ViewModel() {

    val postsOutcome: LiveData<Outcome<List<PostWithUser>>> by lazy {
        //Convert publish subject to livedata
        repo.postFetchOutcome.toLiveData(compositeDisposable)
    }

    fun getPosts() {
        if (postsOutcome.value == null)
            repo.fetchPosts()
    }

    fun refreshPosts() {
        repo.refreshPosts()
    }
    fun addPost(post : Post) {
        repo.saveUserNewPost(post);
    }


    override fun onCleared() {
        super.onCleared()
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        PostDH.destroyListComponent()
    }
}