package com.example.myavtosalon.ui.clients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myavtosalon.data.local.Client
import com.example.myavtosalon.databinding.ItemClientBinding

class ClientsAdapter(
    private val onCallClick: (Client) -> Unit,
    private val onEditClick: (Client) -> Unit,
    private val onDeleteClick: (Client) -> Unit
) : RecyclerView.Adapter<ClientsAdapter.ClientViewHolder>() {

    private var items: List<Client> = emptyList()

    fun submitList(list: List<Client>) {
        items = list
        notifyDataSetChanged()
    }

    inner class ClientViewHolder(val binding: ItemClientBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemClientBinding.inflate(inflater, parent, false)
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val item = items[position]

        holder.binding.textClientName.text = item.fullName
        holder.binding.textClientPhone.text = item.phone

        val infoParts = mutableListOf<String>()
        item.purchaseDate?.takeIf { it.isNotBlank() }?.let { infoParts.add("Дата: $it") }
        item.dealPrice?.let { infoParts.add("Сумма: ${it.toInt()} ₽") }
        holder.binding.textClientInfo.text = infoParts.joinToString(" • ")

        // Клик по карточке = звонок
        holder.binding.root.setOnClickListener { onCallClick(item) }

        // Кнопки рядом с ФИО
        holder.binding.btnEdit.setOnClickListener { onEditClick(item) }
        holder.binding.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount(): Int = items.size
}
