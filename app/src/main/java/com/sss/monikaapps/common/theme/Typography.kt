package com.sss.monikaapps.common.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sss.monikaapps.R
// ========================
// Font Family
// ========================
val PopBold = FontFamily(Font(R.font.pop_bold))
val PopSemiBold = FontFamily(Font(R.font.pop_semi_bold))
val PopMedium = FontFamily(Font(R.font.pop_medium))
val PopRegular = FontFamily(Font(R.font.pop_reg))

val BitterBold = FontFamily(Font(R.font.bitter_bold))
val BitterSemiBold = FontFamily(Font(R.font.bitter_semi_bold))
val BitterMedium = FontFamily(Font(R.font.bitter_medium))
val BitterRegular = FontFamily(Font(R.font.bitter_reguler))

// ========================
// Induk Typography
// ========================

// Title induk
val Title = TextStyle(
    fontFamily = PopBold,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp,
    color = Color.Black
)

// Body induk
val Body = TextStyle(
    fontFamily = PopRegular,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    color = Color.Black
)

// ========================
// Turunan Title
// ========================
val TitlePopBold = Title.copy(fontFamily = PopBold)
val TitlePopSemiBold = Title.copy(fontFamily = PopSemiBold)
val TitleBitterBold = Title.copy(fontFamily = BitterBold)
val TitleBitterSemiBold = Title.copy(fontFamily = BitterSemiBold)

// ========================
// Turunan Body
// ========================
val BodyPopBold = Body.copy(fontFamily = PopBold, fontWeight = FontWeight.Bold)
val BodyPopSemiBold = Body.copy(fontFamily = PopSemiBold, fontWeight = FontWeight.SemiBold)
val BodyPopMedium = Body.copy(fontFamily = PopMedium, fontWeight = FontWeight.Medium)
val BodyPopRegular = Body.copy(fontFamily = PopRegular, fontWeight = FontWeight.Normal)

val BodyBitterBold = Body.copy(fontFamily = BitterBold, fontWeight = FontWeight.Bold)
val BodyBitterSemiBold = Body.copy(fontFamily = BitterSemiBold, fontWeight = FontWeight.SemiBold)
val BodyBitterMedium = Body.copy(fontFamily = BitterMedium, fontWeight = FontWeight.Medium)
val BodyBitterRegular = Body.copy(fontFamily = BitterRegular, fontWeight = FontWeight.Normal)