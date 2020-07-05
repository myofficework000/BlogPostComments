package com.abhishekpathak.posts.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.abhishekpathak.posts.R
import com.abhishekpathak.posts.commons.PostDH
import com.abhishekpathak.posts.commons.data.PostWithUser
import com.abhishekpathak.posts.commons.data.local.Comment
import com.abhishekpathak.posts.core.application.BaseActivity
import com.abhishekpathak.posts.details.exceptions.DetailsExceptions
import com.abhishekpathak.posts.details.viewmodel.DetailsViewModel
import com.abhishekpathak.posts.details.viewmodel.DetailsViewModelFactory
import com.mpaani.core.networking.Outcome
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.btn_send
import kotlinx.android.synthetic.main.activity_details.edt_comments
import kotlinx.android.synthetic.main.activity_details.ivAvatar
import kotlinx.android.synthetic.main.activity_details.rvComments
import kotlinx.android.synthetic.main.activity_details.srlComments
import kotlinx.android.synthetic.main.activity_details.tvAuthorName
import kotlinx.android.synthetic.main.activity_details.tvBody
import kotlinx.android.synthetic.main.activity_details.tvCommentError
import kotlinx.android.synthetic.main.activity_details.tvTitle
import java.io.IOException
import javax.inject.Inject


class DetailsActivity : BaseActivity(), DetailsAdapter.Interaction {

    companion object {
        private const val SELECTED_POST = "post"
        //Transitions
        private const val TITLE_TRANSITION_NAME = "title_transition"
        private const val BODY_TRANSITION_NAME = "body_transition"
        private const val AUTHOR_TRANSITION_NAME = "author_transition"
        private const val AVATAR_TRANSITION_NAME = "avatar_transition"

        fun start(
            context: Context,
            post: PostWithUser,
            tvTitle: TextView,
            tvBody: TextView,
            tvAuthorName: TextView,
            ivAvatar: ImageView
        ) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(SELECTED_POST, post)

            //Transitions
            intent.putExtra(TITLE_TRANSITION_NAME, ViewCompat.getTransitionName(tvTitle))
            intent.putExtra(BODY_TRANSITION_NAME, ViewCompat.getTransitionName(tvBody))
            intent.putExtra(AUTHOR_TRANSITION_NAME, ViewCompat.getTransitionName(tvAuthorName))
            intent.putExtra(AVATAR_TRANSITION_NAME, ViewCompat.getTransitionName(ivAvatar))

            val p1 = Pair.create(tvTitle as View, ViewCompat.getTransitionName(tvTitle))
            val p2 = Pair.create(tvBody as View, ViewCompat.getTransitionName(tvBody))
            val p3 = Pair.create(tvAuthorName as View, ViewCompat.getTransitionName(tvAuthorName))
            val p4 = Pair.create(ivAvatar as View, ViewCompat.getTransitionName(ivAvatar))
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                p1,
                p2,
                p3,
                p4
            )

            context.startActivity(intent, options.toBundle())
        }

        fun start(context: Context, postId: Int) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(SELECTED_POST, postId)
            context.startActivity(intent)
        }
    }

    private val TAG = "DetailsActivity"
    private var selectedPost: PostWithUser? = null
    private val context: Context by lazy { this }
    private val component by lazy { PostDH.detailsComponent() }

    val adapter: DetailsAdapter by lazy { DetailsAdapter(this) }

    @Inject
    lateinit var viewModelFactory: DetailsViewModelFactory

    @Inject
    lateinit var picasso: Picasso

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        component.inject(this)
        getIntentData()
        intializeViews()
        srlComments.setOnRefreshListener { viewModel.refreshCommentsFor(selectedPost?.postId) }
    }

    private fun intializeViews() {
        val comment = edt_comments as EditText
        val send = btn_send as Button
        send.setOnClickListener(View.OnClickListener {
            val _item = Comment(
                100,
                100,
                "abhishek",
                "abhishek.pathak@nagarro.com",
                comment.text.toString()
            )
            viewModel.addComment(_item)
            comment.text = null
        })
    }

    private fun getIntentData() {
        if (!intent.hasExtra(SELECTED_POST)) {
            Log.d(TAG, "getIntentData: could not find selected post")
            finish()
            return
        }

        selectedPost = intent.getParcelableExtra(SELECTED_POST)
        tvTitle.text = selectedPost?.postTitle
        tvBody.text = selectedPost?.postBody
        tvAuthorName.text = selectedPost?.userName
        picasso.load(selectedPost?.getAvatarPhoto()).into(ivAvatar)

        handleTransition(intent.extras)

        rvComments.adapter = adapter

        viewModel.loadCommentsFor(selectedPost?.postId)
        observeData()
    }

    private fun handleTransition(extras: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvTitle.transitionName = extras?.getString(TITLE_TRANSITION_NAME)
            tvBody.transitionName = extras?.getString(BODY_TRANSITION_NAME)
            tvAuthorName.transitionName = extras?.getString(AUTHOR_TRANSITION_NAME)
            ivAvatar.transitionName = extras?.getString(AVATAR_TRANSITION_NAME)
        }
    }

    private fun observeData() {
        viewModel.commentsOutcome.observe(this, Observer<Outcome<List<Comment>>> { outcome ->
            Log.d(TAG, "initiateDataListener: " + outcome.toString())
            when (outcome) {

                is Outcome.Progress -> srlComments.isRefreshing = outcome.loading

                is Outcome.Success -> {
                    Log.d(TAG, "observeData:  Successfully loaded data")
                    tvCommentError.visibility = View.GONE
                    adapter.swapData(outcome.data)
                }

                is Outcome.Failure -> {
                    when (outcome.e) {
                        DetailsExceptions.NoComments() -> tvCommentError.visibility =
                            View.VISIBLE
                        IOException() -> Toast.makeText(
                            context,
                            R.string.need_internet_posts,
                            Toast.LENGTH_LONG
                        ).show()
                        else -> Toast.makeText(
                            context,
                            R.string.failed_post_try_again,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }
        })
    }

    override fun commentClicked(model: Comment) {
        Log.d(TAG, "Comment clicked: $model")
    }
}
