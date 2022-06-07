package com.codeliner.bulletinboard.accounts

import android.widget.Toast
import com.codeliner.bulletinboard.MainActivity
import com.codeliner.bulletinboard.R
import com.google.firebase.auth.FirebaseUser

class AccountHelper(activity: MainActivity) {
    private val activity = activity

    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            activity.authentification.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification(task.result?.user!!)
                    } else {
                        Toast.makeText(activity, activity.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
                    }
                }
        }//проверить, что поля не пустые
    } //регистрация пользователя

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