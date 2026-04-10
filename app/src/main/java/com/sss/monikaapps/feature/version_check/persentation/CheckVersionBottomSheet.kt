package com.sss.monikaapps.feature.version_check.persentation

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.theme.BodyBitterMedium
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.common.theme.TitlePopBold
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckVersionBottomSheet(
    viewModel: VersionViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.update_version_animation)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LaunchedEffect(Unit) {
        viewModel.openUrlEvent.collect { url ->
            val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            // 3. Biarkan kosong agar klik di luar (scrim) tidak menutup sheet
        },
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false // Sudah ditangani oleh BackHandler di atas
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(250.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.text_update_version),
                style = TitlePopBold.copy(color = Primary),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(Dimens.SmallMargin))

            Text(
                text = stringResource(R.string.text_update_version_desc),
                style = BodyBitterMedium,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            CustomPrimaryButton(
                text = stringResource(R.string.text_download),
                onClick = { viewModel.onDownloadClicked() }
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}