package com.example.calculadora_sergiomoramirete

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var pantallaResultado: TextView
    private var ultimoEsNumero: Boolean = false
    private var estadoDeError: Boolean = false
    private var ultimoEsPunto: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pantallaResultado = findViewById(R.id.pantallaResultado)
    }

    //Funcion para que se muestre el boton pulsado en la pantallaResultado
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

    //Funcion para que se muestre el operador pulsado en la pantallaResultado
    fun onOperador(view: View) {
        val boton = view as Button
        val op = boton.text.toString()

        if (estadoDeError) {
            return
        }

        //Para que se pueda operar con numero negativos o positivos
        if (op == "+" || op == "-") {
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

    //Funcion para que calcule el resultado y se muestre en la pantallaResultado
    fun onIgual(view: View) {
        if (ultimoEsNumero && !estadoDeError) {
            val texto = pantallaResultado.text.toString()
            try {
                val expresion = ExpressionBuilder(texto).build()
                val resultado = expresion.evaluate()
                if (resultado == resultado.toLong().toDouble()) {
                    pantallaResultado.text = resultado.toLong().toString()
                    ultimoEsPunto = false
                } else {
                    pantallaResultado.text = resultado.toString()
                    ultimoEsPunto = true
                }
            } catch (ex: Exception) {
                pantallaResultado.text = "Error"
                estadoDeError = true
                ultimoEsNumero = false
            }
        }
    }

    //Funcion para el C, que borra el texto de la pantallaResultado
    fun onClear(view: View) {
        pantallaResultado.text = "0"
        ultimoEsNumero = false
        estadoDeError = false
        ultimoEsPunto = false
    }

    //Funcion para el decimal, que añade un punto a la pantallaResultado
    fun onDecimal(view: View) {
        if (ultimoEsNumero && !estadoDeError && !ultimoEsPunto) {
            pantallaResultado.append(".")
            ultimoEsNumero = false
            ultimoEsPunto = true
        }
    }
}