package com.codeliner.bulletinboard.dialogs

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.codeliner.bulletinboard.MainActivity
import com.codeliner.bulletinboard.R
import com.codeliner.bulletinboard.accounts.AccountHelper
import com.codeliner.bulletinboard.databinding.SignDialogBinding

class DialogHelper(activity: MainActivity) {
    private val activity = activity
    private val accountHelper =
        AccountHelper(activity) //объект, с помощью которого можно регистрироваться

    fun createSignDialog(index: Int) {
        val builder = AlertDialog.Builder(activity)
        val binding = SignDialogBinding.inflate(activity.layoutInflater)
        val view = binding.root
        builder.setView(view)
        setDialogState(index, binding)

        val dialog = builder.create()
        binding.buttonSignUp.setOnClickListener {
            setOnClickSign(index, binding, dialog)
        }
        binding.buttonForgetPassword.setOnClickListener {
            setOnClickResetPassword(binding, dialog)
        }
        dialog.show() //показать диалог на экране

    }

    private fun setOnClickResetPassword(binding: SignDialogBinding, dialog: AlertDialog?) {
        if (binding.editSignEmail.text.isNotEmpty()) {
            activity.authentification.sendPasswordResetEmail(binding.editSignEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, R.string.reset_password_was_sent_to_email,
                            Toast.LENGTH_LONG).show()
                    }
                }
            dialog?.dismiss()
        } else {
            binding.textSignMessage.visibility = View.VISIBLE
        }
    }

    private fun setOnClickSign(index: Int, binding: SignDialogBinding, dialog: AlertDialog?) {
        dialog?.dismiss() //автоматически закрыть диалог после нажатия на кнопку Зарегистрироваться/Войти
        if (index == DialogConst.SIGN_UP_STATE) {
            accountHelper.signUpWithEmail(
                binding.editSignEmail.text.toString(),
                binding.editSignPassword.text.toString()
            ) // проверить пустые или нет
        } else {
            accountHelper.signInWithEmail(
                binding.editSignEmail.text.toString(),
                binding.editSignPassword.text.toString()
            )
        } //проверить зашли для регистрации или для входа
    }

    private fun setDialogState(index: Int, binding: SignDialogBinding) {
        if (index == DialogConst.SIGN_UP_STATE) {
            binding.tvSignTitle.text =
                activity.resources.getString(R.string.sign_up) //если регистрация
            binding.buttonSignUp.text = activity.resources.getString(R.string.sign_up_action)
        } else {
            binding.tvSignTitle.text = activity.resources.getString(R.string.sign_in) //если вход
            binding.buttonSignUp.text = activity.resources.getString(R.string.sign_in_action)
            binding.buttonForgetPassword.visibility = View.VISIBLE
        }
    }
}