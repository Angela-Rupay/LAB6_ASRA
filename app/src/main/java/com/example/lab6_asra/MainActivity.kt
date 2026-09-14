package com.example.lab6_asra

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    private val PREFS_NAME = "user_preferences"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        // Recuperar el modo oscuro antes de cargar la interfaz
        val darkMode = preferences.getBoolean(
            "dark_mode",
            false
        )

        AppCompatDelegate.setDefaultNightMode(
            if (darkMode)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        setContentView(R.layout.activity_main)

        // Referencias a los elementos
        val tvWelcome = findViewById<TextView>(R.id.tv_welcome)
        val etUsername = findViewById<EditText>(R.id.et_username)
        val swDarkMode = findViewById<Switch>(R.id.sw_dark_mode)
        val swRemember = findViewById<Switch>(R.id.sw_remember)
        val tvLaunchCount = findViewById<TextView>(R.id.tv_launch_count)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val btnReset = findViewById<Button>(R.id.btn_reset)

        // -----------------------------------------
        // CONTADOR DE APERTURAS
        // -----------------------------------------

        val currentLaunches = preferences.getInt(
            "launch_count",
            0
        )

        /*
         * Solo aumentamos el contador cuando la Activity
         * se crea realmente por primera vez.
         *
         * Si Android recrea la Activity, por ejemplo al
         * cambiar entre modo claro y oscuro, no sumamos otra vez.
         */
        if (savedInstanceState == null) {

            val newLaunches = currentLaunches + 1

            preferences
                .edit()
                .putInt("launch_count", newLaunches)
                .apply()

            tvLaunchCount.text =
                "Veces abierta: $newLaunches"

        } else {

            tvLaunchCount.text =
                "Veces abierta: $currentLaunches"
        }

        // -----------------------------------------
        // RECUPERAR DATOS GUARDADOS
        // -----------------------------------------

        val username = preferences.getString(
            "username",
            ""
        )

        val remember = preferences.getBoolean(
            "remember_session",
            false
        )

        etUsername.setText(username)

        swDarkMode.isChecked =
            darkMode

        swRemember.isChecked =
            remember

        // -----------------------------------------
        // MENSAJE DE BIENVENIDA
        // -----------------------------------------

        if (remember && !username.isNullOrBlank()) {

            tvWelcome.text =
                "¡Bienvenido de nuevo, $username!"

            tvWelcome.visibility =
                View.VISIBLE

        } else {

            tvWelcome.visibility =
                View.GONE
        }

        // -----------------------------------------
        // BOTÓN GUARDAR
        // -----------------------------------------

        btnSave.setOnClickListener {

            val usernameToSave =
                etUsername.text.toString().trim()

            val rememberSession =
                swRemember.isChecked

            val darkModeSelected =
                swDarkMode.isChecked

            preferences
                .edit()
                .putString(
                    "username",
                    usernameToSave
                )
                .putBoolean(
                    "dark_mode",
                    darkModeSelected
                )
                .putBoolean(
                    "remember_session",
                    rememberSession
                )
                .apply()

            // Aplicar el modo seleccionado
            AppCompatDelegate.setDefaultNightMode(
                if (darkModeSelected)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )

            // Actualizar mensaje de bienvenida
            if (
                rememberSession &&
                usernameToSave.isNotBlank()
            ) {

                tvWelcome.text =
                    "¡Bienvenido de nuevo, $usernameToSave!"

                tvWelcome.visibility =
                    View.VISIBLE

            } else {

                tvWelcome.visibility =
                    View.GONE
            }

            Toast.makeText(
                this,
                "Preferencias guardadas exitosamente",
                Toast.LENGTH_SHORT
            ).show()
        }

        // -----------------------------------------
        // BOTÓN RESTABLECER
        // -----------------------------------------

        btnReset.setOnClickListener {

            preferences
                .edit()
                .clear()
                .apply()

            etUsername.text.clear()

            swDarkMode.isChecked = false

            swRemember.isChecked = false

            tvLaunchCount.text =
                "Veces abierta: 0"

            tvWelcome.visibility =
                View.GONE

            // Volver inmediatamente al modo claro
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )

            Toast.makeText(
                this,
                "Datos eliminados",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

