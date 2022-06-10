package com.truesightid.ui.prediction

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.truesightid.R
import com.truesightid.data.source.remote.response.NewsPredictionResponse
import com.truesightid.databinding.FragmentPredictionBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.utils.Prefs

class NewsPredictFragment : Fragment() {

    private var _binding: FragmentPredictionBinding? = null
    private val binding get() = _binding!!

    private lateinit var apiKey: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory.getInstance(
            requireContext()
        )
        if (Prefs.isLogin) {
            apiKey = Prefs.getUser()?.apiKey as String
            val viewModel = ViewModelProvider(this, factory)[NewsPredictViewModel::class.java]

            observingViewModel(viewModel)

            binding.btnPredict.setOnClickListener {
                val predict =
                    "${binding.titleNews.editText?.text} ${binding.authorNews.editText?.text} ${binding.contentNews.editText?.text}"
                Toast.makeText(requireContext(), "QWEQWE: $predict", Toast.LENGTH_LONG).show()
                viewModel.getNewsPrediction(apiKey, predict)
            }
        }

    }

    private fun observingViewModel(viewModel: NewsPredictViewModel) {
        viewModel.predictViewModel.observe(viewLifecycleOwner) { predict ->
            if (predict != null) {
                showPrediction(predict)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun showPrediction(prediction: NewsPredictionResponse) {
        binding.tvResultPrediction.text = resources.getString(R.string.prediction_result_with_value, prediction.prediction)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showDialog(isResponse: Boolean) {
        val possitiveButtonClick = { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }
        if (!isResponse) {
            AlertDialog.Builder(requireContext())
                .setTitle(resources.getString(R.string.failed_to_get_data))
                .setMessage(resources.getString(R.string.something_is_wrong_check_internet))
                .setPositiveButton(
                    resources.getString(R.string.dialog_ok),
                    DialogInterface.OnClickListener(function = possitiveButtonClick)
                )
                .show()
        }
    }
}