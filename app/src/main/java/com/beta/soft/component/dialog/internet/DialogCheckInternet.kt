package com.beta.soft.component.dialog.internet

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.beta.soft.R

@Composable
fun DialogCheckInternet(
    onDismiss: () -> Unit,
    onGoToSetting: () -> Unit,
) {
    val message = "${stringResource(R.string.your_device_is)} " +
            "${stringResource(R.string.not_connected)} " +
            stringResource(R.string.to_the_network_please_check_again)

    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(onClick = onGoToSetting) {
                Text(text = stringResource(R.string.go_to_setting))
            }
        }
    )
}

@Preview
@Composable
fun DialogCheckInternetP() {
    DialogCheckInternet(
        onDismiss = {},
        onGoToSetting = {}
    )
}
