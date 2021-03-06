package com.truesightid.ui.mybookmark

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.truesightid.R
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.AddRemoveBookmarkRequest
import com.truesightid.data.source.remote.request.MyDataRequest
import com.truesightid.databinding.ActivityMyBookmarkBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.adapter.MyBookmarksAdapter
import com.truesightid.ui.main.MainActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.UserAction
import com.truesightid.utils.extension.showSuccessDialog
import com.truesightid.utils.extension.toastInfo
import com.truesightid.utils.extension.toastSuccess
import java.lang.Thread.sleep

class MyBookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyBookmarkBinding
    private lateinit var myBookmarksAdapter: MyBookmarksAdapter
    private lateinit var alertDialog: AlertDialog
    private lateinit var viewModel: MyBookmarkViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[MyBookmarkViewModel::class.java]

        binding.ibBackLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("shouldProfile", true)
            startActivity(intent)
        }

        myBookmarksAdapter = MyBookmarksAdapter(object : MyBookmarksAdapter.ItemClaimClickListener {
            override fun onClaimUpvote(claim_id: Int) {
                viewModel.upvoteClaimById(Prefs.getUser()?.apiKey as String, claim_id)
            }

            override fun onClaimDownvote(claim_id: Int) {
                viewModel.downvoteClaimById(Prefs.getUser()?.apiKey as String, claim_id)
            }

            override fun onRemoveClaimtoBookmark(claim_id: Int) {
                removeBookmarkPrompt(claim_id)
            }

        }, Prefs)

        with(binding.rvMyClaim) {
            layoutManager = LinearLayoutManager(context)
            adapter = myBookmarksAdapter
            setHasFixedSize(true)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getMyBookmarks(MyDataRequest(Prefs.getUser()?.apiKey as String))
                .observe(this, claimObserver)
            toastSuccess(resources.getString(R.string.page_refreshed))
            binding.refreshLayout.isRefreshing = false
        }


        viewModel.getMyBookmarks(MyDataRequest(Prefs.getUser()?.apiKey as String))
            .observe(this, claimObserver)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val claimObserver = Observer<ApiResponse<List<ClaimEntity>>> { claims ->
        showLoading()
        if (claims != null) {
            when (claims.status) {
                StatusResponse.ERROR -> {
                    toastInfo(resources.getString(R.string.something_went_wrong))
                    alertDialog.dismiss()
                }
                StatusResponse.SUCCESS -> {
                    alertDialog.dismiss()
                    if (claims.body.isNotEmpty()) {
                        showIllustrator(false)
                    } else {
                        showIllustrator(true)
                    }
                    myBookmarksAdapter.setData(claims.body)
                    myBookmarksAdapter.notifyDataSetChanged()
                }
                StatusResponse.EMPTY -> {
                    toastInfo(resources.getString(R.string.something_went_wrong))
                    alertDialog.dismiss()

                    showSuccessDialog("qwe", "qwe")
                }
            }
        }
    }

    private fun showIllustrator(isIllustrator: Boolean) {
        if (isIllustrator) {
            binding.ivIllustrator.visibility = View.VISIBLE
            binding.tvIllustrator1.visibility = View.VISIBLE
            binding.tvIllustrator2.visibility = View.VISIBLE
        } else {
            binding.ivIllustrator.visibility = View.GONE
            binding.tvIllustrator1.visibility = View.GONE
            binding.tvIllustrator2.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeBookmarkPrompt(claim_id: Int) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(resources.getString(R.string.are_you_sure_to_delete_this_bookmark))
            .setContentText(resources.getString(R.string.click_delete_to_continue))
            .setConfirmText(resources.getString(R.string.delete))
            .setConfirmClickListener {
                UserAction.applyUserBookmarks(claim_id, false)
                viewModel.removeBookmarkById(
                    AddRemoveBookmarkRequest(
                        Prefs.getUser()?.apiKey as String,
                        claim_id
                    )
                )
                sleep(200) // Must be delayed because MySQL is up to 200 ms per query
                viewModel.getMyBookmarks(MyDataRequest(Prefs.getUser()?.apiKey as String))
                    .observe(this, claimObserver)
                it.dismiss()
            }
            .setCancelText(resources.getString(R.string.cancel))
            .setCancelClickListener {
                it.dismiss()
            }
            .show()
    }

    private fun showLoading() {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.view_loading, null)
        alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setView(layout)
        alertDialog.setCancelable(false)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }
}