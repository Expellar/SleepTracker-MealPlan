package com.example.sleeptrackermealplan

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.tan

class DiagonalLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val path = Path()
    private var angle = 10f
    private var isBottom = true

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DiagonalLayout,
            0, 0
        ).apply {
            try {
                angle = getFloat(R.styleable.DiagonalLayout_diagonal_angle, 10f)
                isBottom = getInt(R.styleable.DiagonalLayout_diagonal_position, 1) == 1
            } finally {
                recycle()
            }
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()

        val width = w.toFloat()
        val height = h.toFloat()
        val slant = (width * tan(Math.toRadians(angle.toDouble()))).toFloat()

        if (isBottom) {
            path.moveTo(0f, 0f)
            path.lineTo(width, 0f)
            path.lineTo(width, height - slant)
            path.lineTo(0f, height)
            path.close()
        } else { // Top position
            path.moveTo(0f, slant)
            path.lineTo(width, 0f)
            path.lineTo(width, height)
            path.lineTo(0f, height)
            path.close()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
    }
}

