package com.codeliner.bulletinboard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.codeliner.bulletinboard.R
import com.codeliner.bulletinboard.accounts.GoogleAccConst
import com.codeliner.bulletinboard.databinding.ActivityMainBinding
import com.codeliner.bulletinboard.dialogs.DialogConst
import com.codeliner.bulletinboard.dialogs.DialogHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var textAccount: TextView
    private lateinit var binding: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val authentification = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_new_ads) {
            val intent = Intent(this, EditAdsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE) {
            //Log.d("Log.d", "Sign in result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)//получить аккаунт
                if (account != null) {
                    Log.d("Log.d", "Api 0")
                    dialogHelper.accountHelper.signInFirebaseWithGoogle(account.idToken!!)
                } //достать токен
            } catch (e:ApiException) {
                Log.d("Log.d", "Api error : ${e.message}")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(authentification.currentUser) //показать юзера, если осуществлен вход
    }

    private fun init() {
        setSupportActionBar(binding.mainContent.toolbarContent) // выбор тулбара перед нажатием на кнопку
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayoutMain, binding.mainContent.toolbarContent,
            R.string.open,
            R.string.close
        ) //добавить кнопку для всплывающего меню
        binding.drawerLayoutMain.addDrawerListener(toggle)//указать, что дроуэр лэйаут будет открываться при нажатии на кнопку
        toggle.syncState()
        binding.navViewMain.setNavigationItemSelectedListener(this)
        textAccount = binding.navViewMain.getHeaderView(0).findViewById(R.id.tv_account_email_header)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_my_ads -> {
                Toast.makeText(this, "Pressed id_my_ads", Toast.LENGTH_LONG).show()
            }
            R.id.id_car -> {
                Toast.makeText(this, "Pressed id_car", Toast.LENGTH_LONG).show()
            }
            R.id.id_pc -> {
                Toast.makeText(this, "Pressed id_pc", Toast.LENGTH_LONG).show()
            }
            R.id.id_sign_up -> {
                dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE)
            }
            R.id.id_sign_in -> {
                dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)
            }
            R.id.id_sign_out -> {
                uiUpdate(null) //при нажатии на кнопку выход, обнулить юзера
                authentification.signOut() //выйти из аккаунта
                dialogHelper.accountHelper.signOutGoogle()
            }
        }
        binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
        return true
    } // при нажатии на элемент, получаем item на который нажали

    fun uiUpdate(user: FirebaseUser?) {

        textAccount.text = if (user == null) {
            resources.getString(R.string.no_registration)
        } else {
            user.email
        }
    }
}