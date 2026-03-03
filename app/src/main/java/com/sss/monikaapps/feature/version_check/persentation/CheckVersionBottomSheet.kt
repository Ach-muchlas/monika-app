package com.sss.monikaapps.feature.version_check.persentation

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.theme.BodyBitterMedium
import com.sss.monikaapps.common.theme.Dimens
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
        confirmValueChange = { false }
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
        onDismissRequest = {},
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.3f),
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.icon_check_version),
                contentDescription = null
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.text_update_version),
                style = TitlePopBold,
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

            Spacer(Modifier.height(24.dp))
        }
    }
}
