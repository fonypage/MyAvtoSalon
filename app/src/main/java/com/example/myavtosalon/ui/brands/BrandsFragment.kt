package com.example.myavtosalon.ui.brands

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
import com.example.myavtosalon.ui.models.ModelsFragment

/**
 * Первый экран: список марок автомобилей.
 */
class BrandsFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    // общая ViewModel на всё приложение (Activity scope)
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var adapter: BrandsAdapter

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

        adapter = BrandsAdapter { brand ->
            // 1) запоминаем выбранную марку во ViewModel
            viewModel.selectBrand(brand.id)

            // 2) переходим на экран моделей
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ModelsFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        // Подписываемся на список марок из ViewModel
        viewModel.brands.observe(viewLifecycleOwner) { brands ->
            adapter.submitList(brands)
        }

        // Обработчик кнопки "+"
        binding.fabAdd.setOnClickListener {
            showAddBrandDialog()
        }
    }

    private fun showAddBrandDialog() {
        val editText = EditText(requireContext()).apply {
            hint = "Введите название марки (например, Toyota)"
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Добавить марку")
            .setView(editText)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("ОК") { _, _ ->
                val name = editText.text.toString().trim()
                if (name.isNotEmpty()) {
                    viewModel.addBrand(name)
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
