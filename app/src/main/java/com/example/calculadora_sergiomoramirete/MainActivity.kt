package com.example.calculadora_sergiomoramirete

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
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
    private var esResultado: Boolean = false

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

        // Configurar el detector de gestos para borrar caracteres
        val detectorMovimiento = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 != null) {
                    val diffX = e2.x - e1.x
                    // Gesto de deslizar de izquierda a derecha
                    if (diffX > 100 && kotlin.math.abs(velocityX) > 100) {
                        onBorrarDigito()
                        return true // Gesto consumido
                    }
                }
                return false
            }
        })

        pantallaResultado.setOnTouchListener { _, event ->
            // Devuelve el resultado del detector de gestos.
            // De esta forma, solo se consume el evento si es un gesto de deslizamiento.
            detectorMovimiento.onTouchEvent(event)
            true
        }
    }

    //Al recibir el resultado - se actualiza la pantalla con el resultado del historial
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
                esResultado = true
            }
        }
    }

    private fun onBorrarDigito() {
        esResultado = false
        if (estadoDeError) {
            pantallaResultado.text = "0"
            ultimoEsNumero = false
            estadoDeError = false
            ultimoEsPunto = false
            return
        }

        val currentText = pantallaResultado.text.toString()
        if (currentText.isEmpty() || currentText == "0") {
            return
        }

        val newText = if (currentText.length > 1) {
            currentText.dropLast(1)
        } else {
            "0"
        }
        pantallaResultado.text = newText

        if (newText == "0") {
            ultimoEsNumero = false
            ultimoEsPunto = false
            return
        }

        val lastChar = newText.last()
        if (lastChar.isDigit()) {
            ultimoEsNumero = true
            val lastOperatorIndex = newText.indexOfLast { it == '+' || it == '-' || it == '*' || it == '/' }
            val numberSegment = if (lastOperatorIndex == -1) newText else newText.substring(lastOperatorIndex + 1)
            ultimoEsPunto = numberSegment.contains('.')
        } else if (lastChar == '.') {
            ultimoEsNumero = false
            ultimoEsPunto = true
        } else { // Operator
            ultimoEsNumero = false
            ultimoEsPunto = false
        }
    }

    fun onBotonPulsado(view: View) {
        val boton = view as Button
        if (estadoDeError) {
            pantallaResultado.text = boton.text
            estadoDeError = false
        } else if (esResultado) {
            pantallaResultado.text = boton.text
            esResultado = false
        }
        else {
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
        
        if (ultimoEsNumero || esResultado) {
            pantallaResultado.append(op)
            ultimoEsNumero = false
            ultimoEsPunto = false
            esResultado = false
        } else {
            val currentText = pantallaResultado.text.toString()
            if (currentText.isNotEmpty()) {
                val lastChar = currentText.last()
                if (lastChar == '+' || lastChar == '-' || lastChar == '*' || lastChar == '/') {
                    pantallaResultado.text = currentText.dropLast(1) + op
                }
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
                esResultado = true
                ultimoEsNumero = true
            } catch (ex: Exception) {
                pantallaResultado.text = "Error"
                estadoDeError = true
                ultimoEsNumero = false
                esResultado = false
            }
        }
    }

    fun onClear(view: View) {
        pantallaResultado.text = "0"
        ultimoEsNumero = false
        estadoDeError = false
        ultimoEsPunto = false
        esResultado = false
    }

    fun onDecimal(view: View) {
        if (esResultado) {
            pantallaResultado.text = "0."
            esResultado = false
            ultimoEsNumero = false
            ultimoEsPunto = true
            return
        }

        if (ultimoEsNumero && !estadoDeError && !ultimoEsPunto) {
            pantallaResultado.append(".")
            ultimoEsNumero = false
            ultimoEsPunto = true
        }
    }
}
