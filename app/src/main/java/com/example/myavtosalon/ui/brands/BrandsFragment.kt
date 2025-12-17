package com.example.myavtosalon.ui.brands

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
import com.example.myavtosalon.ui.models.ModelsFragment

class BrandsFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: BrandsAdapter

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

        // На экране Марок добавление через Menu, поэтому FAB скрываем
        binding.fabAdd.visibility = View.GONE

        adapter = BrandsAdapter(
            onClick = { brand ->
                viewModel.selectBrand(brand.id)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ModelsFragment())
                    .addToBackStack(null)
                    .commit()
            },
            onLongClick = { brand ->
                viewModel.selectBrand(brand.id)
                Toast.makeText(requireContext(), "Выбрано: ${brand.name}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        viewModel.brands.observe(viewLifecycleOwner) { brands ->
            adapter.submitList(brands)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_brands, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> { showAddBrandDialog(); true }
            R.id.action_edit -> { showEditBrandDialog(); true }
            R.id.action_delete -> { showDeleteBrandDialog(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAddBrandDialog() {
        val edit = EditText(requireContext()).apply { hint = "Марка (например, Toyota)" }
        AlertDialog.Builder(requireContext())
            .setTitle("Добавить марку")
            .setView(edit)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("Подтвердить") { _, _ ->
                val name = edit.text.toString().trim()
                if (name.isNotBlank()) viewModel.addBrand(name)
                else Toast.makeText(requireContext(), "Пустое имя", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun showEditBrandDialog() {
        val selectedId = viewModel.selectedBrandId.value
        val brands = viewModel.brands.value

        if (selectedId == null || brands.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Выберите марку (долгое нажатие)", Toast.LENGTH_SHORT).show()
            return
        }

        val brand = brands.find { it.id == selectedId } ?: run {
            Toast.makeText(requireContext(), "Марка не найдена", Toast.LENGTH_SHORT).show()
            return
        }

        val edit = EditText(requireContext()).apply { setText(brand.name) }

        AlertDialog.Builder(requireContext())
            .setTitle("Изменить марку")
            .setView(edit)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("Подтвердить") { _, _ ->
                val newName = edit.text.toString().trim()
                if (newName.isNotBlank()) viewModel.updateBrand(brand.copy(name = newName))
                else Toast.makeText(requireContext(), "Пустое имя", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun showDeleteBrandDialog() {
        val selectedId = viewModel.selectedBrandId.value
        val brands = viewModel.brands.value

        if (selectedId == null || brands.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Выберите марку (долгое нажатие)", Toast.LENGTH_SHORT).show()
            return
        }

        val brand = brands.find { it.id == selectedId } ?: run {
            Toast.makeText(requireContext(), "Марка не найдена", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Удаление!")
            .setMessage("Вы действительно хотите удалить марку \"${brand.name}\"?")
            .setNegativeButton("Нет", null)
            .setPositiveButton("Да") { _, _ ->
                viewModel.deleteBrand(brand)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
