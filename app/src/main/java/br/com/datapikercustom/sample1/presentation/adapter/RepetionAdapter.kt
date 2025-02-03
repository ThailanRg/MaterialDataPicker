package br.com.datapikercustom.sample1.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.datapikercustom.databinding.ItemRepetionBinding

class RepetionAdapter(
    val onClick:(String) -> Unit
) : RecyclerView.Adapter<RepetionAdapter.ViewHolder>() {

    private val list = mutableListOf("semanal", "quinzenal")
    private var selectedOption: String? = null

    inner class ViewHolder(private val binding: ItemRepetionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) = with(binding) {
           radioButton.text = item
           radioButton.isChecked = item == selectedOption
           radioButton.setOnClickListener {
               onClick(item)
           }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelectedPerson(person: String) {
        this.selectedOption = person
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRepetionBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

}