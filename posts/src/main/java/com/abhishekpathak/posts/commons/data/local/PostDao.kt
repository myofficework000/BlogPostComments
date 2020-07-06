package com.abhishekpathak.posts.commons.data.local

import androidx.room.*
import com.abhishekpathak.posts.commons.data.PostWithUser
import io.reactivex.Flowable


@Dao
interface PostDao {
    @Query("SELECT post.postId AS postId, post.postTitle AS postTitle ,post.postBody AS postBody, user.userName as userName FROM post, user WHERE post.userId= user.id")
    fun getAllPostsWithUser(): Flowable<List<PostWithUser>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertAll(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(post: Post)

    @Delete
    fun delete(id: Post)
/*

    @Delete
    fun delete(postId: Int)
*/


    @Query("SELECT * FROM post")
    fun getAll(): Flowable<List<Post>>
}