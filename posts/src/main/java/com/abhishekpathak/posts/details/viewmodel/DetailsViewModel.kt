package com.abhishekpathak.posts.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.abhishekpathak.posts.commons.PostDH
import com.abhishekpathak.posts.commons.data.local.Comment
import com.abhishekpathak.posts.core.extensions.toLiveData
import com.abhishekpathak.posts.details.model.DetailsDataContract
import com.mpaani.core.networking.Outcome
import io.reactivex.disposables.CompositeDisposable

class DetailsViewModel(private val repo: DetailsDataContract.Repository, private val compositeDisposable: CompositeDisposable) : ViewModel() {

    val commentsOutcome: LiveData<Outcome<List<Comment>>> by lazy {
        repo.commentsFetchOutcome.toLiveData(compositeDisposable)
    }

    fun loadCommentsFor(postId: Int?) {
        repo.fetchCommentsFor(postId)
    }

    fun addComment(comment: Comment) {
        repo.saveCommentForPost(comment)
        repo.fetchCommentsFor(comment.postId)
    }

    fun refreshCommentsFor(postId: Int?) {
        if (postId != null)
            repo.refreshComments(postId)
    }

    override fun onCleared() {
        super.onCleared()
        //clear the disposables when the viewmodel is cleared
        compositeDisposable.clear()
        PostDH.destroyDetailsComponent()
    }
}