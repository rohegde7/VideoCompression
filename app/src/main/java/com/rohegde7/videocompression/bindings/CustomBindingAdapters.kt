package com.rohegde7.videocompression.bindings

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import com.rohegde7.videocompression.R
import com.rohegde7.videocompression.application.VideoCompressionApplication


class CustomBindingAdapters {

    companion object {

        @JvmStatic
        @BindingAdapter(value = ["entries"])
        fun setEntriesAndSelectedPosition(
            spinner: Spinner,
            entries: List<String>
        ) {

            spinner.adapter = getSpinnerAdapter(entries)

        }

        private fun getSpinnerAdapter(entries: List<String>): ArrayAdapter<String> {

            return ArrayAdapter<String>(
                VideoCompressionApplication.mAppContext,
                R.layout.support_simple_spinner_dropdown_item,
                entries
            )
        }

        private fun setOnItemSelectedListener(
            spinner: Spinner,
            entries: List<String>,
            selectedEntry: ObservableField<String>
        ) {

            spinner.onItemSelectedListener = object : OnItemSelectedListener {

                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedEntry.set(entries.get(position))
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {

                }
            }
        }
    }
}