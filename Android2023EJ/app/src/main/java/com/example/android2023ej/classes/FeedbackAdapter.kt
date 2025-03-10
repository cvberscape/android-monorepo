package com.example.android2023ej.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android2023ej.databinding.FeedbackItemBinding

class FeedbackAdapter(private val feedbackList: MutableList<Feedback>) : RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

    private var _binding: FeedbackItemBinding? = null

    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        _binding = FeedbackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedbackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val feedback = feedbackList[position]
        holder.bind(feedback)
    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }

    inner class FeedbackViewHolder(private val binding: FeedbackItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Binds the data from the feedback object to the views in the item layout.
        fun bind(feedback: Feedback) {
            // Sets values for textViews
            binding.nameTextView.text = "Name: " + feedback.attributes?.name + "\n"
            binding.locationTextView.text = "Location: " + feedback.attributes?.location + "\n"
            binding.valueTextView.text = "Feedback: " + feedback.attributes?.value + "\n"

        }
    }
}
