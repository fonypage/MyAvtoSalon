package com.example.myavtosalon.ui.clients

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myavtosalon.data.local.Client
import com.example.myavtosalon.databinding.FragmentListBinding
import com.example.myavtosalon.ui.SharedViewModel

class ClientsFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: ClientsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ClientsAdapter(
            onCallClick = { client -> callClient(client) },
            onEditClick = { client -> showEditClientDialog(client) },
            onDeleteClick = { client -> showDeleteClientDialog(client) }
        )

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        viewModel.clientsForSelectedModel.observe(viewLifecycleOwner) { clients ->
            adapter.submitList(clients)
        }

        // На 3 экране FAB нужен (как в примере)
        binding.fabAdd.visibility = View.VISIBLE
        binding.fabAdd.setOnClickListener { showAddClientDialog() }
    }

    private fun callClient(client: Client) {
        val phone = client.phone
        if (phone.isBlank()) {
            Toast.makeText(requireContext(), "У клиента не указан телефон", Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
    }

    private fun showAddClientDialog() {
        val context = requireContext()

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val editName = EditText(context).apply { hint = "ФИО клиента" }
        val editPhone = EditText(context).apply { hint = "Телефон" }

        layout.addView(editName)
        layout.addView(editPhone)

        AlertDialog.Builder(context)
            .setTitle("Добавить клиента")
            .setView(layout)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("Подтвердить") { _, _ ->
                val name = editName.text.toString().trim()
                val phone = editPhone.text.toString().trim()

                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(context, "Имя и телефон не могут быть пустыми", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addClient(fullName = name, phone = phone)
                }
            }
            .show()
    }

    private fun showEditClientDialog(client: Client) {
        val context = requireContext()

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val editName = EditText(context).apply {
            hint = "ФИО клиента"
            setText(client.fullName)
        }
        val editPhone = EditText(context).apply {
            hint = "Телефон"
            setText(client.phone)
        }

        layout.addView(editName)
        layout.addView(editPhone)

        AlertDialog.Builder(context)
            .setTitle("Изменить клиента")
            .setView(layout)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("Подтвердить") { _, _ ->
                val name = editName.text.toString().trim()
                val phone = editPhone.text.toString().trim()

                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(context, "Имя и телефон не могут быть пустыми", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.updateClient(client.copy(fullName = name, phone = phone))
                }
            }
            .show()
    }

    private fun showDeleteClientDialog(client: Client) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление!")
            .setMessage("Удалить клиента \"${client.fullName}\"?")
            .setNegativeButton("Нет", null)
            .setPositiveButton("Да") { _, _ ->
                viewModel.deleteClient(client)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
