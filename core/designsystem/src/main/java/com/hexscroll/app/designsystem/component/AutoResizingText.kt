package com.hexscroll.app.designsystem.component

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp

@Composable
fun AutoResizingText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    minFontSize: TextUnit = 10.sp,
    maxFontSize: TextUnit = style.fontSize.takeUnless { it.isUnspecified } ?: 16.sp,
    textAlign: TextAlign? = null,
) {
    var fontSizeValue by remember { mutableFloatStateOf(maxFontSize.value) }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Clip,
        softWrap = false,
        style = style.copy(fontSize = fontSizeValue.sp),
        textAlign = textAlign,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                val next = fontSizeValue - 1f
                if (next >= minFontSize.value) {
                    fontSizeValue = next
                } else {
                    readyToDraw = true
                }
            } else {
                readyToDraw = true
            }
        },
        modifier = modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
    )
}
