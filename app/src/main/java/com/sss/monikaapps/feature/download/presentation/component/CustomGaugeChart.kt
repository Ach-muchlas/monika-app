package com.sss.monikaapps.feature.download.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GaugeChart(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 80.dp,
) {
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        val maxWidth = constraints.maxWidth.toFloat()
        val gaugeHeight = maxWidth / 2f

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(density) { (gaugeHeight / this.density).dp })
        ) {
            val stroke = strokeWidth.toPx()
            val width = size.width
            val height = size.height * 2 // Double height untuk full circle calculation
            val center = Offset(width / 2, height / 2)
            val radius = (width.coerceAtMost(height) - stroke) / 2

            // ===== Background Arc =====
            drawArc(
                color = Color(0xFFE0E0E0),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(stroke, cap = StrokeCap.Butt),
                topLeft = Offset(stroke / 2, stroke / 2),
                size = Size(width - stroke, height - stroke)
            )

            // ===== Green Zone =====
            drawArc(
                color = Color(0xFF6CC04A),
                startAngle = 180f,
                sweepAngle = 60f,
                useCenter = false,
                style = Stroke(stroke, cap = StrokeCap.Butt),
                topLeft = Offset(stroke / 2, stroke / 2),
                size = Size(width - stroke, height - stroke)
            )

            // ===== Yellow Zone =====
            drawArc(
                color = Color(0xFFFFC107),
                startAngle = 240f,
                sweepAngle = 60f,
                useCenter = false,
                style = Stroke(stroke, cap = StrokeCap.Butt),
                topLeft = Offset(stroke / 2, stroke / 2),
                size = Size(width - stroke, height - stroke)
            )

            // ===== Red Zone =====
            drawArc(
                color = Color(0xFFE53935),
                startAngle = 300f,
                sweepAngle = 60f,
                useCenter = false,
                style = Stroke(stroke, cap = StrokeCap.Butt),
                topLeft = Offset(stroke / 2, stroke / 2),
                size = Size(width - stroke, height - stroke)
            )

            // ===== NEEDLE =====
            val angle = -90f + (progress * 180f)
            rotate(angle, pivot = center) {
                val needleLength = radius * 0.85f
                val needleWidth = stroke * 0.15f

                val path = Path().apply {
                    moveTo(center.x, center.y)
                    lineTo(center.x - needleWidth, center.y + needleWidth * 1.2f)
                    lineTo(center.x, center.y - needleLength)
                    lineTo(center.x + needleWidth, center.y + needleWidth * 1.2f)
                    close()
                }

                drawPath(path, Color.Black)
            }

            // ===== Center Circle =====
            val circleRadius = stroke * 0.25f
            drawCircle(Color.Black, radius = circleRadius, center = center)
            drawCircle(Color.White, radius = circleRadius * 0.45f, center = center)
        }
    }
}
