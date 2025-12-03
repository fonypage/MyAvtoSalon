package com.example.myavtosalon.ui.clients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myavtosalon.data.local.Client
import com.example.myavtosalon.databinding.ItemClientBinding

/**
 * Адаптер для списка клиентов выбранной модели.
 */
class ClientsAdapter(
    private val onClick: (Client) -> Unit
) : RecyclerView.Adapter<ClientsAdapter.ClientViewHolder>() {

    private var items: List<Client> = emptyList()

    fun submitList(list: List<Client>) {
        items = list
        notifyDataSetChanged()
    }

    inner class ClientViewHolder(
        val binding: ItemClientBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemClientBinding.inflate(inflater, parent, false)
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val item = items[position]

        holder.binding.textClientName.text = item.fullName
        holder.binding.textClientPhone.text = item.phone

        // Собираем дополнительную информацию
        val infoParts = mutableListOf<String>()
        item.purchaseDate?.takeIf { it.isNotBlank() }?.let { infoParts.add("Дата: $it") }
        item.dealPrice?.let { infoParts.add("Сумма: ${it.toInt()} ₽") }

        holder.binding.textClientInfo.text =
            if (infoParts.isEmpty()) ""
            else infoParts.joinToString(" • ")

        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
