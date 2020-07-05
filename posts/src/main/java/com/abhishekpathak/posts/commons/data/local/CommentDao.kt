package com.abhishekpathak.posts.commons.data.local

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface CommentDao {

    @Query("SELECT * from comment where postId = :postId")
    fun getForPost(postId: Int): Flowable<List<Comment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(comments: List<Comment>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(comment: Comment)

    @Delete
    fun delete(comment: Comment)
}