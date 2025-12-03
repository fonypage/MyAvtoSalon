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
import com.example.myavtosalon.databinding.FragmentListBinding
import com.example.myavtosalon.ui.SharedViewModel

/**
 * Экран: список клиентов выбранной модели.
 */
class ClientsFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var adapter: ClientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ClientsAdapter { client ->
            // Открываем звонилку с номером клиента — выполнение требования по Intent
            val phone = client.phone
            if (phone.isNotBlank()) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    "У клиента не указан телефон",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        // Подписываемся на список клиентов для выбранной модели
        viewModel.clientsForSelectedModel.observe(viewLifecycleOwner) { clients ->
            adapter.submitList(clients)
        }

        // FAB для добавления нового клиента
        binding.fabAdd.setOnClickListener {
            showAddClientDialog()
        }
    }

    private fun showAddClientDialog() {
        val context = requireContext()

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 30, 50, 10)
        }

        val editName = EditText(context).apply {
            hint = "ФИО клиента"
        }
        val editPhone = EditText(context).apply {
            hint = "Телефон"
        }

        layout.addView(editName)
        layout.addView(editPhone)

        AlertDialog.Builder(context)
            .setTitle("Добавить клиента")
            .setView(layout)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("ОК") { _, _ ->
                val name = editName.text.toString().trim()
                val phone = editPhone.text.toString().trim()

                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Имя и телефон не могут быть пустыми",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.addClient(
                        fullName = name,
                        phone = phone,
                        purchaseDate = null,
                        dealPrice = null
                    )
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
