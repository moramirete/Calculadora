package com.example.calculadora_sergiomoramirete

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class HistorialActivity : AppCompatActivity() {

    private lateinit var historyContainer: LinearLayout
    private lateinit var clearHistoryButton: ImageButton
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        historyContainer = findViewById(R.id.history_container)
        clearHistoryButton = findViewById(R.id.clear_history_button)
        backButton = findViewById(R.id.back_button)

        val history = intent.getStringArrayListExtra("history") ?: ArrayList()

        displayHistory(history)

        clearHistoryButton.setOnClickListener {
            confirmarBorrarHistorial()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun displayHistory(history: ArrayList<String>) {
        historyContainer.removeAllViews()
        for (operation in history) {
            val textView = TextView(this)
            textView.text = operation
            textView.textSize = 24f
            textView.setPadding(0, 16, 0, 16)
            textView.setTextColor(Color.parseColor("#E0E0E0"))
            textView.setOnClickListener {
                val result = operation.substringAfter("= ")
                val resultIntent = Intent()
                resultIntent.putExtra("result", result)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            historyContainer.addView(textView)

            val divider = View(this)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
            layoutParams.setMargins(0, 8, 0, 8)
            divider.layoutParams = layoutParams
            divider.setBackgroundColor(Color.parseColor("#E0E0E0"))
            historyContainer.addView(divider)
        }
    }

    private fun confirmarBorrarHistorial() {
        AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setTitle("Borrar historial")
            .setMessage("¿Estás seguro de que quieres borrar el historial?")
            .setPositiveButton("Sí") { _, _ ->
                limpiarHistorial()
            }
            .setNegativeButton("No", null)
            .create()
            .apply {
                setOnShowListener {
                    val positiveButton = getButton(AlertDialog.BUTTON_POSITIVE)
                    positiveButton?.setTextColor(Color.parseColor("#FF9500"))

                    val negativeButton = getButton(AlertDialog.BUTTON_NEGATIVE)
                    negativeButton?.setTextColor(Color.parseColor("#E0E0E0"))

                    val messageView = findViewById<TextView>(android.R.id.message)
                    messageView?.setTextColor(Color.parseColor("#E0E0E0"))
                }
            }
            .show()
    }



    private fun limpiarHistorial() {
        historyContainer.removeAllViews()
        val resultIntent = Intent()
        resultIntent.putExtra("history_cleared", true)
        setResult(Activity.RESULT_OK, resultIntent)
    }
}