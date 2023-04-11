package com.innaval.agendaeventos.presentation.utils

import android.view.View

fun View.setVisibility(isVisible: Boolean){
    visibility = if (isVisible) View.VISIBLE else View.GONE
}