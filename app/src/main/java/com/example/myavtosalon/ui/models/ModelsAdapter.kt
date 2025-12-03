package com.example.myavtosalon.ui.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myavtosalon.data.local.CarModel
import com.example.myavtosalon.databinding.ItemModelBinding

/**
 * Адаптер для списка моделей выбранной марки.
 */
class ModelsAdapter(
    private val onClick: (CarModel) -> Unit
) : RecyclerView.Adapter<ModelsAdapter.ModelViewHolder>() {

    private var items: List<CarModel> = emptyList()

    fun submitList(list: List<CarModel>) {
        items = list
        notifyDataSetChanged()
    }

    inner class ModelViewHolder(
        val binding: ItemModelBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemModelBinding.inflate(inflater, parent, false)
        return ModelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        val item = items[position]

        holder.binding.textModelName.text = item.name

        // Собираем строку с кузовом и ценой
        val infoParts = mutableListOf<String>()
        item.bodyType?.takeIf { it.isNotBlank() }?.let { infoParts.add(it) }
        item.price?.let { infoParts.add("${it.toInt()} ₽") }

        holder.binding.textModelInfo.text =
            if (infoParts.isEmpty()) ""
            else infoParts.joinToString(" • ")

        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
