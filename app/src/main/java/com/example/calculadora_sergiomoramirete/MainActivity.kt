package com.example.calculadora_sergiomoramirete

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var pantallaResultado: TextView
    private var ultimoEsNumero: Boolean = false
    private var estadoDeError: Boolean = false
    private var ultimoEsPunto: Boolean = false

    private lateinit var historialButton: ImageButton
    private val history = ArrayList<String>()
    private val HISTORY_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pantallaResultado = findViewById(R.id.pantallaResultado)
        historialButton = findViewById(R.id.btnHistorial) // Encuentra el botón por su ID

        historialButton.setOnClickListener {
            val intentHistorial = Intent(this, HistorialActivity::class.java)
            intentHistorial.putStringArrayListExtra("history", history)
            startActivityForResult(intentHistorial, HISTORY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HISTORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data?.getBooleanExtra("history_cleared", false) == true) {
                history.clear()
            }
            data?.getStringExtra("result")?.let { result ->
                pantallaResultado.text = result
                ultimoEsPunto = result.contains(".")
                ultimoEsNumero = true
                estadoDeError = false
            }
        }
    }

    fun onBotonPulsado(view: View) {
        val boton = view as Button
        if (estadoDeError) {
            pantallaResultado.text = boton.text
            estadoDeError = false
        } else {
            if (pantallaResultado.text.toString() == "0") {
                pantallaResultado.text = boton.text
            } else {
                pantallaResultado.append(boton.text)
            }
        }
        ultimoEsNumero = true
    }

    fun onOperador(view: View) {
        val boton = view as Button
        val op = boton.text.toString()

        if (estadoDeError) {
            return
        }

        val opActual = pantallaResultado.text.toString()

        if (op == "+" || op == "-") {
            if (opActual.isNotEmpty()) {
                val ultimoCaracter = opActual.last().toString()
                if (ultimoCaracter == "+" || ultimoCaracter == "-") {
                    return
                }
            }

            if (pantallaResultado.text.toString() == "0") {
                pantallaResultado.text = op
            } else {
                pantallaResultado.append(op)
            }
            ultimoEsNumero = false
            ultimoEsPunto = false
        } else {
            if (ultimoEsNumero) {
                pantallaResultado.append(op)
                ultimoEsNumero = false
                ultimoEsPunto = false
            }
        }
    }

    fun onIgual(view: View) {
        if (ultimoEsNumero && !estadoDeError) {
            val texto = pantallaResultado.text.toString()
            try {
                val expresion = ExpressionBuilder(texto).build()
                val resultado = expresion.evaluate()
                val resultadoString = if (resultado == resultado.toLong().toDouble()) {
                    resultado.toLong().toString()
                } else {
                    resultado.toString()
                }
                history.add("$texto = $resultadoString")
                pantallaResultado.text = resultadoString
                ultimoEsPunto = pantallaResultado.text.contains(".")
            } catch (ex: Exception) {
                pantallaResultado.text = "Error"
                estadoDeError = true
                ultimoEsNumero = false
            }
        }
    }

    fun onClear(view: View) {
        pantallaResultado.text = "0"
        ultimoEsNumero = false
        estadoDeError = false
        ultimoEsPunto = false
    }

    fun onDecimal(view: View) {
        if (ultimoEsNumero && !estadoDeError && !ultimoEsPunto) {
            pantallaResultado.append(".")
            ultimoEsNumero = false
            ultimoEsPunto = true
        }
    }
}
