package com.codeliner.bulletinboard.accounts

import android.widget.Toast
import com.codeliner.bulletinboard.MainActivity
import com.codeliner.bulletinboard.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

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
                        Toast.makeText(activity, activity.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
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
                        Toast.makeText(activity, activity.resources.getString(R.string.sign_in_error), Toast.LENGTH_LONG).show()
                    }
                }
        }
    } //вход пользователя

    private fun getSignInClient(): GoogleSignInClient {
        val googleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id)).build()
        return GoogleSignIn.getClient(activity, googleSignInOptions)
    } // регистрация через гугл аккаунт

    fun signInWithGoogle() {
        googleSignInClient = getSignInClient()
        val intent = googleSignInClient.signInIntent //запустить SignInClient
        activity.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    } // дальше пишем в Main Activity в onActivityResult

    fun signInFirebaseWithGoogle(token:String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        activity.authentification.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Sign in done", Toast.LENGTH_LONG).show()
            }
        }
    } //запустить функцию из MainActivity

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, activity.resources.getString(R.string.send_verification_done), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity, activity.resources.getString(R.string.send_verification_error), Toast.LENGTH_LONG).show()
            }
        } // успешно прошло или нет
    } // для отправки подтверждающего письма со ссылкой

}