package com.abhishekpathak.posts.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.abhishekpathak.posts.R
import com.abhishekpathak.posts.commons.PostDH
import com.abhishekpathak.posts.commons.data.PostWithUser
import com.abhishekpathak.posts.commons.data.local.Post
import com.abhishekpathak.posts.core.application.BaseActivity
import com.abhishekpathak.posts.details.DetailsActivity
import com.abhishekpathak.posts.list.viewmodel.ListViewModel
import com.abhishekpathak.posts.list.viewmodel.ListViewModelFactory
import com.mpaani.core.networking.Outcome
import kotlinx.android.synthetic.main.activity_list.btn_create_post
import kotlinx.android.synthetic.main.activity_list.edt_post_detail
import kotlinx.android.synthetic.main.activity_list.edt_post_heading
import kotlinx.android.synthetic.main.activity_list.layout_new_post
import kotlinx.android.synthetic.main.activity_list.rvPosts
import kotlinx.android.synthetic.main.activity_list.srlPosts
import java.io.IOException
import javax.inject.Inject

class ListActivity : BaseActivity(), PostListAdapter.Interaction {

    private val component by lazy { PostDH.listComponent() }

    @Inject
    lateinit var viewModelFactory: ListViewModelFactory

    @Inject
    lateinit var adapter: PostListAdapter

    private val viewModel: ListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private val context: Context by lazy { this }

    private val TAG = "ListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        component.inject(this)

        adapter.interaction = this
        rvPosts.adapter = adapter
        srlPosts.setOnRefreshListener { viewModel.refreshPosts() }
        btn_create_post.setOnClickListener {
            createPost()
        }
        viewModel.getPosts()
        initiateDataListener()
    }

    private fun initiateDataListener() {
        //Observe the outcome and update state of the screen  accordingly
        viewModel.postsOutcome.observe(this, Observer<Outcome<List<PostWithUser>>> { outcome ->
            Log.d(TAG, "initiateDataListener: $outcome")
            when (outcome) {

                is Outcome.Progress -> srlPosts.isRefreshing = outcome.loading

                is Outcome.Success -> {
                    Log.d(TAG, "initiateDataListener: Successfully loaded data")
                    adapter.swapData(outcome.data)
                }

                is Outcome.Failure -> {

                    if (outcome.e is IOException)
                        Toast.makeText(
                            context,
                            R.string.need_internet_posts,
                            Toast.LENGTH_LONG
                        ).show()
                    else
                        Toast.makeText(
                            context,
                            R.string.failed_post_try_again,
                            Toast.LENGTH_LONG
                        ).show()
                }

            }
        })
    }

    override fun postClicked(
        post: PostWithUser,
        tvTitle: TextView,
        tvBody: TextView,
        tvAuthorName: TextView,
        ivAvatar: ImageView
    ) {
        DetailsActivity.start(context, post, tvTitle, tvBody, tvAuthorName, ivAvatar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.itemId

        if (id == R.id.action_add_post) {
            layout_new_post.visibility = View.VISIBLE
            srlPosts.visibility = View.GONE
            return true
        }
        if (id == R.id.action_profile) {
            Toast.makeText(this, "Item Two Clicked", Toast.LENGTH_LONG).show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createPost() {
        layout_new_post.visibility = View.GONE
        srlPosts.visibility = View.VISIBLE
        viewModel.addPost(
            Post(
                1,
                1,
                edt_post_heading.text.toString(),
                edt_post_detail.text.toString()
            )
        )
    }
}
