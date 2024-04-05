package com.example.rxpress10.ui.interaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rxpress10.databinding.DrugCardBinding
import kotlinx.android.synthetic.main.activity_login.view.*

// This adapter holds the data for drug interactions after searching for them on the drug interaction screen
class CardAdapter(
    private val drugs: List<Drugs>
    ): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(val binding: DrugCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = DrugCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        with(holder) {
            with(drugs[position]){
                binding.drugOne.text = this.drugOne
                binding.drugTwo.text = this.drugTwo
                binding.severity.text = this.severity
                binding.interaction.text = this.interaction
            }
        }
    }

    override fun getItemCount(): Int {
        return drugs.size
    }
}