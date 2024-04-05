package com.example.rxpress10.ui.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rxpress10.databinding.SingleItemBinding
import com.example.rxpress10.ui.faq.model.FAQ
//This class is an adapter for the recycler view used on the faq screen
class RvAdapter (
    private var faqList: List<FAQ>
) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {
        inner class ViewHolder(val binding: SingleItemBinding) : RecyclerView.ViewHolder(binding.root)

    //Creates a container for the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //Creates binding for the rv and watches for navigation link clicks
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(faqList[position]){
                binding.tvLangName.text = this.name
                binding.tvDescription.text = this.description
                binding.tvLink.text = this.link
                //waiting for navigation click
                binding.tvLink.setOnClickListener {
                    if (this.link == "Go To Prescription Page") {
                        goToPrescript(binding.tvLink)
                    } else if (this.link == "Go To Profile Page") {
                        goToProfile(binding.tvLink)
                    } else if (this.link == "Go To Calendar") {
                        goToCal(binding.tvLink)
                    } else if (this.link == "Go To Settings") {
                        goToSettings(binding.tvLink)
                    }
                }
                binding.expandedView.visibility = if (this.expand) View.VISIBLE else View.GONE
                binding.cardLayout.setOnClickListener {
                    this.expand = !this.expand
                    notifyDataSetChanged()
                }
            }
        }
    }
    //Function to navigate to prescription page
    fun goToPrescript(view: View) {
        val action = FAQFragmentDirections.actionNavFaqToNavPrescriptionHome()
        view.findNavController().navigate(action)
    }
    //Function to navigate to profile page
    fun goToProfile(view: View) {
        val action = FAQFragmentDirections.actionNavFaqToProfileFragment()
        view.findNavController().navigate(action)
    }
    //Function to navigate to calendar page
    fun goToCal(view: View) {
        val action = FAQFragmentDirections.actionNavFaqToNavCalendarFragment()
        view.findNavController().navigate(action)
    }
    //Function to navigate to settings page
    fun goToSettings(view: View) {
        val action = FAQFragmentDirections.actionNavFaqToNavSettings()
        view.findNavController().navigate(action)
    }
    //getter for size of the recycler view
    override fun getItemCount(): Int {
        return faqList.size
    }
}