package com.tomtruyen.feature.auth

import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.tomtruyen.core.common.utils.GoogleSignInHelper
import com.tomtruyen.core.designsystem.Dimens
import kotlinx.coroutines.launch
import com.tomtruyen.core.common.R as CommonR

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

        val scope = rememberCoroutineScope()

        val credentialManager = remember { GoogleSignInHelper.getCredentialManager(context) }
        val request = remember { GoogleSignInHelper.getGoogleSignInRequest() }

        Button(
            enabled = enabled,
            onClick = {
                // Side effect on click
                onClick()


                // Sign in with Google
                scope.launch {
                    try {
                        val result = credentialManager.getCredential(
                            request = request,
                            context = context
                        )

                        when (val credential = result.credential) {
                            is CustomCredential -> {
                                if (credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                    throw Exception("Invalid credential type for CustomCredential: ${credential.type}")
                                }

                                val googleIdTokenCredential =
                                    GoogleIdTokenCredential.createFrom(credential.data)

                                onSuccess(googleIdTokenCredential.idToken)
                            }

                            else -> throw Exception("Invalid credential: $credential")
                        }
                    } catch (e: Exception) {
                        Log.e("GoogleSignIn", "Failed to sign in with Google", e)
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
                color = MaterialTheme.colorScheme.surface
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