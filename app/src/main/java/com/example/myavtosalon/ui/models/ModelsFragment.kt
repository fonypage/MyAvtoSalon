package com.example.myavtosalon.ui.models

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myavtosalon.R
import com.example.myavtosalon.databinding.FragmentListBinding
import com.example.myavtosalon.ui.SharedViewModel
import com.example.myavtosalon.ui.clients.ClientsFragment

/**
 * Экран: список моделей выбранной марки.
 */
class ModelsFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var adapter: ModelsAdapter

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

        adapter = ModelsAdapter { model ->
            // 1) запоминаем выбранную модель
            viewModel.selectModel(model.id)

            // 2) переходим на экран клиентов
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ClientsFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        // Подписываемся на список моделей для выбранной марки
        viewModel.modelsForSelectedBrand.observe(viewLifecycleOwner) { models ->
            adapter.submitList(models)
        }

        // FAB для добавления новой модели
        binding.fabAdd.setOnClickListener {
            showAddModelDialog()
        }
    }

    private fun showAddModelDialog() {
        val editName = EditText(requireContext()).apply {
            hint = "Название модели (например, Camry)"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Добавить модель")
            .setView(editName)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("ОК") { _, _ ->
                val name = editName.text.toString().trim()
                if (name.isNotEmpty()) {
                    viewModel.addModel(
                        name = name,
                        bodyType = null,
                        price = null
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Название не может быть пустым",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
