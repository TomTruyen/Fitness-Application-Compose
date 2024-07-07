package com.tomtruyen.fitnessapplication.ui.shared

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.tomtruyen.core.designsystem.Dimens
import com.tomtruyen.fitnessapplication.R
import com.tomtruyen.core.common.R as CommonR
import com.tomtruyen.data.firebase.auth.GoogleSignInHelper
import com.tomtruyen.core.designsystem.theme.LavenderMist
import com.tomtruyen.fitnessapplication.BuildConfig

object SocialButtons {
    @Composable
    fun Google(
        text: String,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        shape: Shape = MaterialTheme.shapes.small,
        onClick: () -> Unit = {},
        onSuccess: (String) -> Unit,
        onError: (String?) -> Unit
    ) {
        val context = LocalContext.current

        val client = remember { GoogleSignInHelper.getGoogleSignInClient(context) }
        val request = remember { GoogleSignInHelper.getGoogleSignInRequest(BuildConfig.GOOGLE_SERVER_CLIENT_ID) }

        val signInResultLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if(result.resultCode == Activity.RESULT_OK && result.data != null) {
                val credential = client.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken

                if(idToken != null) {
                    onSuccess(idToken)
                } else {
                    onError(context.getString(CommonR.string.error_google_sign_in_failed))
                }
            }
        }

        Button(
            enabled = enabled,
            onClick = {
                // Side effect on click
                onClick()

                // Sign in with Google
                client.beginSignIn(request).addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val intentSender = task.result.pendingIntent.intentSender
                        val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
                        signInResultLauncher.launch(intentSenderRequest)
                    } else {
                        onError(context.getString(CommonR.string.error_google_sign_in_failed))
                    }
                }
            },
            shape = shape,
            modifier = modifier
                .defaultMinSize(
                    minHeight = Dimens.MinButtonHeight,
                )
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            border = BorderStroke(
                width = 1.dp,
                color = LavenderMist
            )
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_logo_google),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )

            Spacer(modifier = Modifier.width(Dimens.Small))

            Text(text = text)
        }
    }
}