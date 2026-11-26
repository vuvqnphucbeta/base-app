package com.beta.soft.component.dialog.permission


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.beta.soft.R

@Composable
fun DialogPermission(
    onDismissRequest: () -> Unit,
    onGoToSettings: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.request_access))
        },
        text = {
            Text(
                text = stringResource(
                    R.string.the_app_needs_file_management_permissions_to_scan_and_display_all_documents_on_your_device
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onGoToSettings) {
                Text(text = stringResource(R.string.file_access_permissions))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.may_be_later))
            }
        }
    )
}

@Preview
@Composable
fun DialogPermissionP() {
    DialogPermission(
        onDismissRequest = {},
        onGoToSettings = {}
    )
}