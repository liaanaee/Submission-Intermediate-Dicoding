package com.lianaekaw.myapkstory.repository

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lianaekaw.myapkstory.databinding.ItemStoriesBinding
import com.lianaekaw.myapkstory.response.ListStoryItem
import com.lianaekaw.myapkstory.view.detail.DetailActivity

class UserAdapter :  PagingDataAdapter<ListStoryItem, UserAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    inner class ViewHolder(private val binding: ItemStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            with(binding) {
                tvItemName.text = story.name
                tvDate.text = story.description
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(ivItemPhoto)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                        putExtra("STORY_ID", story.id)
                        putExtra("STORY_TITLE", story.name)
                        putExtra("STORY_CONTENT", story.description)
                        putExtra("STORY_IMAGE_URL", story.photoUrl)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}