package com.pratham.pradigikids.custom.fluid_keyboard

data class KeyboardVisibilityChanged(
        val visible: Boolean,
        val contentHeight: Int,
        val contentHeightBeforeResize: Int
)
