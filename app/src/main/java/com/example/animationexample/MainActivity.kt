package com.example.animationexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animationexample.ui.theme.AnimationExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimationEx()
                }
            }
        }
    }
}

enum class BoxScale{
    Small,
    Large
}
@Composable
fun AnimationEx() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        var boxScale by remember {
            mutableStateOf(BoxScale.Small)
        }
        var isOpen by remember { mutableStateOf(false) }
        val animateScale by animateFloatAsState(
            targetValue = when (boxScale){
                BoxScale.Small -> .5f
                BoxScale.Large -> 2f
            },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy
            ),
            label = "scale Box"
        )

        val angle by animateFloatAsState(
            targetValue = if (isOpen) 360f else 0f,
            label = "rotation",
            animationSpec = tween(
                durationMillis = 2000,
                easing = EaseInCubic
            )
        )

        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopCenter)
                .drawBehind {
                    drawArc(
                        color = Color.Green,
                        startAngle = 0f,
                        sweepAngle = angle,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx())
                    )
                }
        ){
            Text(
                text = "${angle.toInt()}",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 24.sp
            )
        }

        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
                .scale(animateScale)
                .background(Color.Green, shape = CircleShape)
                .clip(CircleShape)
                .clickable {
                    boxScale = when (boxScale) {
                        BoxScale.Small -> BoxScale.Large
                        BoxScale.Large -> BoxScale.Small
                    }
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {

            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { isOpen = !isOpen }) {
                Text(
                    text = "Toggle Info",
                    fontFamily = FontFamily(Font(R.font.black_han_sans))
                )
            }
            AnimatedVisibility(
                visible = isOpen,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 1000)
                ) + expandIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                exit = shrinkHorizontally()
            ) {
                Text(text = "Motivational quotes can help you reach your potential each day. Sure, they’re just words. But they’re positive words. And if you’re on the verge of giving up or struggling to push yourself to the next level, sometimes that’s just what you need. ")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    AnimationExampleTheme {
        AnimationEx()
    }
}