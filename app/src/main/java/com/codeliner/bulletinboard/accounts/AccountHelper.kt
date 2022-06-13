package com.codeliner.bulletinboard.accounts

import android.util.Log
import android.widget.Toast
import com.codeliner.bulletinboard.MainActivity
import com.codeliner.bulletinboard.R
import com.codeliner.bulletinboard.constants.FireBaseAuthConstants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*

class AccountHelper(activity: MainActivity) {
    private val activity = activity
    private lateinit var googleSignInClient: GoogleSignInClient

    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            activity.authentification.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification(task.result?.user!!)
                        activity.uiUpdate(task.result?.user) //user может быть null, когда не осуществлен вход
                    } else {
                        Log.d("Log.d", "Exception : ${task.exception}")
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            val exception = task.exception as FirebaseAuthUserCollisionException
                            if (exception.errorCode == FireBaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE) {
                                // Toast.makeText(activity, FireBaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE, Toast.LENGTH_LONG).show()
                                linkEmailToGoogle(email, password)
                            }
                        } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            val exception = task.exception as FirebaseAuthUserCollisionException
                            if (exception.errorCode == FireBaseAuthConstants.ERROR_INVALID_EMAIL) {
                                Toast.makeText(
                                    activity,
                                    FireBaseAuthConstants.ERROR_INVALID_EMAIL,
                                    Toast.LENGTH_LONG
                                ).show()
                                //Log.d("Log.d", "Exception : ${exception.errorCode}")
                            }
                        }
                        /*
                        if (task.exception is FirebaseAuthWeakPasswordException) {

                            val exception = task.exception as FirebaseAuthWeakPasswordException
                            Log.d("Log.d", "Exception : ${exception.errorCode}")
                            if (exception.errorCode == FireBaseAuthConstants.ERROR_WEAK_PASSWORD) {
                                Toast.makeText(activity, FireBaseAuthConstants.ERROR_WEAK_PASSWORD, Toast.LENGTH_LONG).show()
                                //Log.d("Log.d", "Exception : ${exception.errorCode}")
                            }
                        }
                        */
                    }
                }
        }//проверить, что поля не пустые
    } //регистрация пользователя

    fun signInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            activity.authentification.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        activity.uiUpdate(task.result?.user) //показать email юзера
                    } else {
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            Log.d("Log.d", "Exception : ${task.exception}")
                            val exception =
                                task.exception as FirebaseAuthInvalidCredentialsException
                            if (exception.errorCode == FireBaseAuthConstants.ERROR_INVALID_EMAIL) {
                                Toast.makeText(activity, FireBaseAuthConstants.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                            } else if (exception.errorCode == FireBaseAuthConstants.ERROR_WRONG_PASSWORD) {
                                Toast.makeText(activity, FireBaseAuthConstants.ERROR_WRONG_PASSWORD, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
        }
    } //вход пользователя

    private fun linkEmailToGoogle(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        if (activity.authentification.currentUser != null) {
            activity.authentification.currentUser?.linkWithCredential(credential)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, activity.resources.getString(R.string.accounts_link_done), Toast.LENGTH_LONG).show()
                    }

                }
        } else {
            Toast.makeText(activity, activity.resources.getString(R.string.double_email), Toast.LENGTH_LONG).show()
        }
    }

    private fun getSignInClient(): GoogleSignInClient {
        val googleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id)).requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, googleSignInOptions)
    } // регистрация через гугл аккаунт

    fun signInWithGoogle() {
        googleSignInClient = getSignInClient()
        val intent = googleSignInClient.signInIntent //запустить SignInClient
        activity.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    } // дальше пишем в Main Activity в onActivityResult

    fun signInFirebaseWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        activity.authentification.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Sign in done", Toast.LENGTH_LONG).show()
                activity.uiUpdate(task.result?.user)
            } else {
                Log.d("Log.d", "Google Sign In Exception : ${task.exception}")
            }
        }
    } //запустить функцию из MainActivity

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    activity,
                    activity.resources.getString(R.string.send_verification_done),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    activity,
                    activity.resources.getString(R.string.send_verification_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        } // успешно прошло или нет
    } // для отправки подтверждающего письма со ссылкой

}