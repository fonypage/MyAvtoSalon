package com.example.myavtosalon.ui.brands

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myavtosalon.data.local.Brand
import com.example.myavtosalon.databinding.ItemBrandBinding

/**
 * Адаптер для списка марок автомобилей.
 */
class BrandsAdapter(
    private val onClick: (Brand) -> Unit
) : RecyclerView.Adapter<BrandsAdapter.BrandViewHolder>() {

    private var items: List<Brand> = emptyList()

    fun submitList(list: List<Brand>) {
        items = list
        notifyDataSetChanged()
    }

    inner class BrandViewHolder(
        val binding: ItemBrandBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBrandBinding.inflate(inflater, parent, false)
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val item = items[position]
        holder.binding.textBrandName.text = item.name

        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
