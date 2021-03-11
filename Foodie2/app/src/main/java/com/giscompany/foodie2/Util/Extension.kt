package com.giscompany.foodie2.Util

import android.app.AlertDialog
import android.app.Dialog
import androidx.fragment.app.Fragment
import com.giscompany.foodie2.MainActivity
import com.giscompany.foodie2.R

fun Fragment.getLoading(): Dialog {
    val builder = AlertDialog.Builder((activity as MainActivity))
    builder.setView(R.layout.progress)
    builder.setCancelable(false)
    return builder.create()
}