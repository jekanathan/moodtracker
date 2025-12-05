import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MoodReactionCircle(label: String, emoji: String, bg: Color, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        val scope = rememberCoroutineScope()

        // Emoji animation states
        val scale = remember { Animatable(1f) }
        val floatingOffsetY = remember { Animatable(0f) }
        val floatingAlpha = remember { Animatable(0f) }

        fun startAnimation() {
            onClick() // Save mood immediately

            scope.launch {
                // Pop / Blink effect
                scale.animateTo(1.5f, tween(150, easing = FastOutSlowInEasing))
                scale.animateTo(1f, tween(150, easing = FastOutSlowInEasing))
            }

            scope.launch {
                // Floating emoji
                floatingOffsetY.snapTo(0f)
                floatingAlpha.snapTo(1f)

                launch { floatingOffsetY.animateTo(-150f, tween(800, easing = FastOutSlowInEasing)) }
                launch { floatingAlpha.animateTo(0f, tween(800)) }
            }
        }

        // Column with main emoji
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(bg)
                    .clickable { startAnimation() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.scale(scale.value)
                )
            }

            Spacer(Modifier.height(5.dp))
            Text(label)
        }

        // Floating reaction emoji
        Text(
            text = emoji,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = floatingOffsetY.value.dp)
                .alpha(floatingAlpha.value),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
