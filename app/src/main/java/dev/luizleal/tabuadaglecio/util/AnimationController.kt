package dev.luizleal.tabuadaglecio.util

import android.content.Context
import android.view.View
import dev.luizleal.tabuadaglecio.content.SecurityPreferences
import dev.luizleal.tabuadaglecio.util.ViewUtils.Companion.setButtonPressedAnimation
import dev.luizleal.tabuadaglecio.util.ViewUtils.Companion.setButtonPressedAnimationToAll
import dev.luizleal.tabuadaglecio.util.ViewUtils.Companion.setScaleAnimation

class AnimationController private constructor() {
    companion object {
        fun View.setScaleAnimation(context: Context) {
            val securityPreferences = SecurityPreferences(context)

            if (securityPreferences.getString("animations").toBoolean()) {
                this.setScaleAnimation()
            }
        }

        fun View.setButtonPressedAnimation(context: Context) {
            val securityPreferences = SecurityPreferences(context)

            if (securityPreferences.getString("animations").toBoolean()) {
                this.setButtonPressedAnimation()
            }
        }

        fun setButtonPressedAnimationToAll(context: Context, buttons: List<View>) {
            val securityPreferences = SecurityPreferences(context)

            if (securityPreferences.getString("animations").toBoolean()) {
                ViewUtils.setButtonPressedAnimationToAll(buttons)
            }
        }
    }
}