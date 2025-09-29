package com.example.sleeptrackermealplan

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.sleeptrackermealplan.R

class DiagonalLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var diagonalAngle = 0
    private var diagonalPosition = 0
    private val path = Path()

    init {
        // This is needed to allow clipping on a layout
        setWillNotDraw(false)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiagonalLayout)
        diagonalAngle = typedArray.getInt(R.styleable.DiagonalLayout_diagonal_angle, 0)
        // ✅ FIX: Changed to use the correct attribute name from attrs.xml
        diagonalPosition = typedArray.getInt(R.styleable.DiagonalLayout_diagonal_position, 0)
        typedArray.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            calculatePath()
        }
    }

    private fun calculatePath() {
        path.reset()
        val w = width.toFloat()
        val h = height.toFloat()
        val angle = diagonalAngle.toFloat()

        when (diagonalPosition) {
            4 -> { // Bottom
                path.moveTo(0f, 0f)
                path.lineTo(w, 0f)
                path.lineTo(w, h - angle)
                path.lineTo(0f, h)
                path.close()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (!path.isEmpty) {
            canvas.clipPath(path)
        }
        super.onDraw(canvas)
    }
}

