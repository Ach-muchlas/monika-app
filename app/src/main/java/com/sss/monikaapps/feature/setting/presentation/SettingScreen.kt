package com.sss.monikaapps.feature.setting.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTopBar
import com.sss.monikaapps.common.component.DashedDivider
import com.sss.monikaapps.common.model.SnackbarData
import com.sss.monikaapps.common.snackbar.SnackbarManager
import com.sss.monikaapps.common.theme.BackgroundLayout
import com.sss.monikaapps.common.theme.BodyBitterMedium
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.BodyPopMedium
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.feature.home.presentasi.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
    onClickDownload: () -> Unit,
    onClickConnection: () -> Unit,
    onClickLogout: () -> Unit,
) {
    val user by viewModel.user.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLayout)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumMargin)
        ) {
            CustomTopBar("Pengaturan", onBackClick = { navController.popBackStack() })
            Spacer(modifier = Modifier.height(Dimens.LargeMargin))

            // Card Header Profile
            ProfileCard(
                name = "${user.employeeName} - ${user.roleName}",
                location = user.namaDepo.toString(),
                imeiCode = user.imei.toString()
            )

            Spacer(modifier = Modifier.height(Dimens.MediumMargin))

            FeatureCard(
                onClickDownload = onClickDownload,
                onClickConnection = onClickConnection
            )

            Spacer(modifier = Modifier.height(Dimens.ExtraLargeMargin))

            CustomPrimaryButton(
                text = "Keluar Aplikasi",
                colors = listOf(
                    Color(0xFFFF493A),
                    Color(0xFFD94646),
                    Color(0xFF8B2424)
                )
            ) {
                viewModel.clearUserSession()
                onClickLogout()
            }
        }
    }
}

@Composable
fun ProfileCard(
    name: String,
    location: String,
    imeiCode: String,
) {
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.MediumCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Profile Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.man_avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF4E0))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = name,
                        style = BodyPopBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = location,
                        style = BodyBitterMedium.copy(fontSize = 14.sp),
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Divider
            DashedDivider(
                showText = false,
                color = Color.Gray.copy(alpha = 0.35f),
                textColor = Primary,
                thickness = 2.dp,
                dashLength = 12.dp,
                gapLength = 0.dp
            )


            Spacer(modifier = Modifier.height(12.dp))

            // IMEI Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.icon_imei),
                        contentDescription = "IMEI",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = imeiCode,
                        style = BodyPopMedium
                    )
                }

                // Copy Icon
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(imeiCode))
                        scope.launch {
                            SnackbarManager.showSnackbar(
                                SnackbarData("Data berhasil disalin")
                            )
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_copy), // Ganti dengan icon copy
                        contentDescription = "Copy",
                        tint = Color(0xFFD4A574),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FeatureCard(
    onClickDownload: () -> Unit,
    onClickConnection: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.MediumCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            FeatureItem(
                icon = R.drawable.icon_backup_data,
                title = "Hasil Download",
                onClick = onClickDownload
            )

            DashedDivider(
                showText = false,
                modifier = Modifier.padding(horizontal = Dimens.MediumMargin),
                color = Color.Black.copy(alpha = 0.35f),
                textColor = Primary,
                thickness = 2.dp,
                dashLength = 10.dp,
                gapLength = 8.dp
            )

            FeatureItem(
                icon = R.drawable.icon_connection,
                title = "Konfigurasi Koneksi",
                onClick = onClickConnection
            )
        }
    }
}

@Composable
fun FeatureItem(
    icon: Int,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(40.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            style = BodyPopMedium,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Arrow",
            tint = Color.Gray.copy(alpha = 0.6f),
            modifier = Modifier.size(30.dp)
        )
    }
}
