package com.codeliner.bulletinboard.dialogs

import android.app.AlertDialog
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
        if (index == DialogConst.SIGN_UP_STATE) {
            binding.tvSignTitle.text =
                activity.resources.getString(R.string.sign_up) //если регистрация
            binding.buttonSignUp.text = activity.resources.getString(R.string.sign_up_action)
        } else {
            binding.tvSignTitle.text = activity.resources.getString(R.string.sign_in) //если вход
            binding.buttonSignUp.text = activity.resources.getString(R.string.sign_in_action)
        }
        binding.buttonSignUp.setOnClickListener {
            if (index == DialogConst.SIGN_UP_STATE) {
                accountHelper.signUpWithEmail(
                    binding.editSignEmail.text.toString(),
                    binding.editSignPassword.text.toString()
                ) // проверить пустые или нет
            } else {

            } //проверить зашли для регистрации или для входа
        }
        builder.setView(view)
        builder.show() //показать диалог на экране

    }
}