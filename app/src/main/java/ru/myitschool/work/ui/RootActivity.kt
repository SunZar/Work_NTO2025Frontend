package ru.myitschool.work.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.myitschool.work.R
import ru.myitschool.work.ui.login.LoginDestination
import ru.myitschool.work.ui.login.LoginFragment
import ru.myitschool.work.ui.qr.scan.QrScanDestination
import ru.myitschool.work.ui.qr.scan.QrScanFragment

// НЕ ИЗМЕНЯЙТЕ НАЗВАНИЕ КЛАССА!
@AndroidEntryPoint
class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        if (navHostFragment != null) {
            val navController = navHostFragment.navController
            navController.graph = navController.createGraph(
                startDestination = LoginDestination
            ) {
                fragment<LoginFragment, LoginDestination>()
                fragment<QrScanFragment, QrScanDestination>()
                fragment<ru.myitschool.work.ui.LoginFragment, LoginDestination>()
                fragment<ResultFragment, LoginDestination>()
            }
        }

        val sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        val login = sharedPreferences.getString(Constants.KEY_LOGIN, "")
        //Toast.makeText(baseContext, login, Toast.LENGTH_LONG).show()
        if (login != "") {
            val fragment = ru.myitschool.work.ui.InformationFragment() // Create an instance of your fragment
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment) // Add the fragment to the container
            transaction.commit()
        } else {
            val fragment = ru.myitschool.work.ui.LoginFragment() // Create an instance of your fragment
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment) // Add the fragment to the container
            transaction.commit()
        }
        /*if (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) == null) {
            Toast.makeText(baseContext, "hhgjgg", Toast.LENGTH_LONG).show()
            val fragment = ru.myitschool.work.ui.LoginFragment() // Create an instance of your fragment
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment) // Add the fragment to the container
            transaction.commit()
        }*/

        /*onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onSupportNavigateUp()
                }
            }
        )*/
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        val popBackResult = if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
        } else {
            false
        }
        return popBackResult || super.onSupportNavigateUp()
    }
}