package com.example.myavtosalon.ui.models

import android.os.Bundle
import android.view.*
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

class ModelsFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: ModelsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // На экране Моделей добавление через Menu, поэтому FAB скрываем
        binding.fabAdd.visibility = View.GONE

        adapter = ModelsAdapter(
            onClick = { model ->
                // короткий клик — открыть клиентов
                viewModel.selectModel(model.id)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ClientsFragment())
                    .addToBackStack(null)
                    .commit()
            },
            onLongClick = { model ->
                // долгий клик — выбрать модель для "Изменить/Удалить"
                viewModel.selectModel(model.id)
                Toast.makeText(requireContext(), "Выбрано: ${model.name}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        viewModel.modelsForSelectedBrand.observe(viewLifecycleOwner) { models ->
            adapter.submitList(models)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_models, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> { showAddModelDialog(); true }
            R.id.action_edit -> { showEditModelDialog(); true }
            R.id.action_delete -> { showDeleteModelDialog(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAddModelDialog() {
        val edit = EditText(requireContext()).apply { hint = "Модель (например, Camry)" }

        AlertDialog.Builder(requireContext())
            .setTitle("Добавить модель")
            .setView(edit)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("Подтвердить") { _, _ ->
                val name = edit.text.toString().trim()
                if (name.isNotBlank()) viewModel.addModel(name = name, bodyType = null, price = null)
                else Toast.makeText(requireContext(), "Пустое имя", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun showEditModelDialog() {
        val selectedId = viewModel.selectedModelId.value
        val models = viewModel.modelsForSelectedBrand.value

        if (selectedId == null || models.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Выберите модель (долгое нажатие)", Toast.LENGTH_SHORT).show()
            return
        }

        val model = models.find { it.id == selectedId } ?: run {
            Toast.makeText(requireContext(), "Модель не найдена", Toast.LENGTH_SHORT).show()
            return
        }

        val edit = EditText(requireContext()).apply { setText(model.name) }

        AlertDialog.Builder(requireContext())
            .setTitle("Изменить модель")
            .setView(edit)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("Подтвердить") { _, _ ->
                val newName = edit.text.toString().trim()
                if (newName.isNotBlank()) viewModel.updateModel(model.copy(name = newName))
                else Toast.makeText(requireContext(), "Пустое имя", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun showDeleteModelDialog() {
        val selectedId = viewModel.selectedModelId.value
        val models = viewModel.modelsForSelectedBrand.value

        if (selectedId == null || models.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Выберите модель (долгое нажатие)", Toast.LENGTH_SHORT).show()
            return
        }

        val model = models.find { it.id == selectedId } ?: run {
            Toast.makeText(requireContext(), "Модель не найдена", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Удаление!")
            .setMessage("Вы действительно хотите удалить модель \"${model.name}\"?")
            .setNegativeButton("Нет", null)
            .setPositiveButton("Да") { _, _ ->
                viewModel.deleteModel(model)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
