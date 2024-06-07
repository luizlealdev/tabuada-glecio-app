package dev.luizleal.tabuadaglecio.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout

class ViewUtils { //Classe de utilidades relacionas a Views
    companion object {
        @SuppressLint("ClickableViewAccessibility") //Desativa Lint
        fun View.setButtonPressedAnimation() { //Função que seta animação de click 3D nos botões
            this.setOnTouchListener { v, event -> //"this" referencia o botão (View)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val downAnimation = ObjectAnimator.ofFloat(v, "translationY", 8f)
                        downAnimation.duration = 100
                        downAnimation.start()
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val upAnimation = ObjectAnimator.ofFloat(v, "translationY", 0f)
                        upAnimation.duration = 100
                        upAnimation.start()
                    }
                }
                false
            }
        }

        fun setButtonPressedAnimationToAll(buttons: List<View>) {
            buttons.forEach {
                it.setButtonPressedAnimation()
            }
        }

        fun View.setScaleAnimation() {
            val scaleUpX = PropertyValuesHolder.ofFloat("scaleX", 1.08f)
            val scaleUpY = PropertyValuesHolder.ofFloat("scaleY", 1.08f)
            val scaleDownX = PropertyValuesHolder.ofFloat("scaleX", 1.0f)
            val scaleDownY = PropertyValuesHolder.ofFloat("scaleY", 1.0f)

            val scaleUpAnimation = ObjectAnimator.ofPropertyValuesHolder(this, scaleUpX, scaleUpY)
            scaleUpAnimation.duration = 200

            val scaleDownAnimation = ObjectAnimator.ofPropertyValuesHolder(this, scaleDownX, scaleDownY)
            scaleDownAnimation.duration = 200

            val scaleAnimationsSet = AnimatorSet()
            scaleAnimationsSet.playSequentially(scaleUpAnimation, scaleDownAnimation)
            scaleAnimationsSet.start()
        }
    }
}