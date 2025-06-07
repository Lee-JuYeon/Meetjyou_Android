package com.doggetdrunk.meetjyou_android.ui.custom.snackbar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.doggetdrunk.meetjyou_android.binding.setImageRes
import com.doggetdrunk.meetjyou_android.databinding.CustomSnackbarBinding
class CustomSnackBar(
    private val parentView: ViewGroup,
    private val message: String,
    private val drawableIcon : Int,
    private val drawableIconColour : Int,
    private val backgroundColour : Int
) {

    private val binding = CustomSnackbarBinding.inflate(
        LayoutInflater.from(parentView.context),
        parentView,
        false
    )

    private var isShowing = false
    private var dismissRunnable: Runnable? = null

    private var duration = 2000L

    fun show() {
        if (isShowing) return

        setupContent()
        setupPosition()
        parentView.addView(binding.root)

        animateIn()

        // 자동 dismiss 설정
        if (duration > 0) {
            dismissRunnable = Runnable { dismiss() }
            binding.root.postDelayed(dismissRunnable, duration)
        }

        isShowing = true
    }

    private fun setupContent() {
        binding.text.text = message
        binding.icon.setImageRes(drawableIcon)
        // 색상 수정: ContextCompat.getColor() 사용
        binding.icon.imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(parentView.context, drawableIconColour)
        )
        binding.root.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(parentView.context, backgroundColour)
        )
    }

    private fun setupPosition() {
        // 화면 하단 위치 수정
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM // 하단 정렬
            setMargins(16, 0, 16, 100) // 좌우 16dp, 하단 100dp 마진
        }
        binding.root.layoutParams = params
    }

    private fun animateIn() {
        // 애니메이션 수정: 하단에서 위로 올라오도록
        binding.root.post {
            val startY = binding.root.height.toFloat()

            binding.root.translationY = startY
            binding.root.alpha = 0f

            binding.root.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(300)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
    }

    private fun animateOut(onComplete: () -> Unit) {
        // 애니메이션 수정: 위에서 아래로 내려가도록
        val endY = binding.root.height.toFloat()

        binding.root.animate()
            .translationY(endY)
            .alpha(0f)
            .setDuration(250)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onComplete()
                }
            })
            .start()
    }

    fun dismiss() {
        if (!isShowing) return

        dismissRunnable?.let { binding.root.removeCallbacks(it) }

        animateOut {
            parentView.removeView(binding.root)
            isShowing = false
        }
    }
}