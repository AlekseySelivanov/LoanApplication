package com.alexs.common

import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun <T> Flow<T>.launchWhenStarted(lifeCycleScope: LifecycleCoroutineScope) = lifeCycleScope
    .launchWhenStarted {
        this@launchWhenStarted.collect()
    }

fun TextInputEditText.updateText(newText: String) {
    if (newText != text.toString()) {
        setText(newText)
        setSelection(length())
    }
}

