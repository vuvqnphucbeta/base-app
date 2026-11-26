package com.beta.soft.component.bottom_sheet_common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.beta.soft.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun BottomSheetCommon(
    onDismissRequest: () -> Unit,
    isShowDivider: Boolean = true,
    isExpanded: Boolean = false,
    content: @Composable () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = isExpanded)
    val context = LocalContext.current

    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismissRequest,

        dragHandle = {},
        contentWindowInsets = {
            WindowInsets(
                left = 0,
                top = 0,
                right = 0,
                bottom = BottomSheetDefaults.windowInsets.getBottom(Density(context))
            )
        },
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            GlideImage(
                model = R.drawable.bg_app,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.heightIn(8.dp))
                if (isShowDivider) {
                    Box(
                        modifier = Modifier
                            .clip(shape = ShapeDefaults.ExtraLarge)
                            .size(height = 6.dp, width = 48.dp)
                            .background(color = Color.White)
                    )
                    Spacer(modifier = Modifier.heightIn(8.dp))
                }
                content()
                Spacer(modifier = Modifier.heightIn(16.dp))
            }
        }
    }
}