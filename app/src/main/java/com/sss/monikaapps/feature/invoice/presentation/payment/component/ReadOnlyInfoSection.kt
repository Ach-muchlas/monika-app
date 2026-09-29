package com.sss.monikaapps.feature.invoice.presentation.payment.component


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomTextField
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.Dimens

@Composable
fun ReadOnlyInfoSection(
    customerData: String,
    nomorNota: String,
    totalNominalNota: String,
    totalOutstanding: String,
) {
    val fields = listOf(
        stringResource(R.string.text_customer_data) to customerData,
        stringResource(R.string.text_nomor_nota) to nomorNota,
        stringResource(R.string.text_nominal_nota) to totalNominalNota,
        stringResource(R.string.text_outstanding_nota) to totalOutstanding,
    )
    fields.forEachIndexed { index, (label, value) ->
        LabeledReadOnlyField(label = label, value = value)
        if (index < fields.lastIndex) Spacer(Modifier.height(Dimens.MediumMargin))
    }
}

@Composable
private fun LabeledReadOnlyField(label: String, value: String) {
    Text(text = label, style = BodyPopBold, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(Dimens.ExtraExtraSmallMargin))
    CustomTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {},
        hint = label,
        readOnly = true,
    )
}