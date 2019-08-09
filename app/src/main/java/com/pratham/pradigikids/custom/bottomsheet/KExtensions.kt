package com.pratham.pradigikids.custom.bottomsheet

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorInt
import android.util.TypedValue
import android.view.View
import com.pratham.pradigikids.R

//region NULL

internal inline fun <T, R> T?.runIfNotNull(block: T.() -> R): R? = this?.block()
internal val INVALID_RESOURCE_ID = -1
internal val MAX_ALPHA = 255
internal fun hasMinimumSdk(minimumSdk: Int) = Build.VERSION.SDK_INT >= minimumSdk

internal fun hasMaximumSdk(maximumSdk: Int) = Build.VERSION.SDK_INT <= maximumSdk

@ColorInt
internal fun calculateColor(@ColorInt to: Int, ratio: Float): Int {
    val alpha = (MAX_ALPHA - (MAX_ALPHA * ratio)).toInt()
    return Color.argb(alpha, Color.red(to), Color.green(to), Color.blue(to))
}

//endregion

//region VIEW

@Suppress("DEPRECATION")
internal fun View.setBackgroundCompat(drawable: Drawable) = when {
    hasMinimumSdk(Build.VERSION_CODES.JELLY_BEAN) -> background = drawable
    else -> setBackgroundDrawable(drawable)
}

//endregion

//region CONTEXT

internal fun Context?.isTablet() = this?.resources?.getBoolean(R.bool.super_bottom_sheet_isTablet)
        ?: false

internal fun Context?.isInPortrait() = this?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT

internal fun Context.getAttrId(attrId: Int): Int {
    TypedValue().run {
        return when {
            !theme.resolveAttribute(attrId, this, true) -> INVALID_RESOURCE_ID
            else -> resourceId
        }
    }
}
