package com.truesightid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import com.truesightid.data.source.local.entity.CommentEntity
import com.truesightid.databinding.ItemCommentsBinding
import com.truesightid.utils.DateUtils.getDateAsTimeAgo

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {
    private val commentList = ArrayList<CommentEntity>()

    fun setComments(comments: List<CommentEntity>) {
        commentList.clear()
        commentList.addAll(comments)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemCommentsBinding =
            ItemCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(itemCommentsBinding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val items = commentList[position]
        with(holder) {
            binding.tvUsername.text = items.username
            binding.tvComment.text = items.comment
            binding.tvTimeComment.text = getDateAsTimeAgo(items.dateCreated.toLong(), this.binding.root.context)

            Glide.with(itemView.context)
                .load(items.avatar)
                .timeout(3000)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.logo_true_sight)
                )
                .into(binding.ivUser)
        }
    }

    override fun getItemCount(): Int = commentList.size

    class CommentViewHolder(val binding: ItemCommentsBinding) :
        RecyclerView.ViewHolder(binding.root)

}