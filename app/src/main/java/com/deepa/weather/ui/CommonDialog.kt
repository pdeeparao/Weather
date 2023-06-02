package com.deepa.weather.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.deepa.weather.R

class CommonDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = arguments?.getString("message") ?: getString(R.string.common_error)
        return AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .create()
    }

    companion object {
        const val TAG = "CommonErrorDialog"
    }
}