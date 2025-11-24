
# Calculadora hecha en Kotlin

Calculadora hecha para un trabajo de clase de la FP de Desarrollo de Aplicaciones Multiplataforma (DAM)

# 📱 Calculadora-SergioMoraMirete

Una aplicación de calculadora móvil simple e intuitiva desarrollada para Android. Permite realizar operaciones aritméticas básicas de forma rápida y confiable.

## 🌟 Características Principales

* **Operaciones Básicas:** Soporte para suma (`+`), resta (`-`), multiplicación (`*`) y división (`/`).
* **Manejo de Decimales:** Permite realizar cálculos con números decimales.
* **Limpieza de Pantalla:** Botón `C` para borrar la entrada actual y resetear el estado.
* **Interfaz Clara:** Diseño optimizado para una fácil interacción en dispositivos móviles.

## 🛠️ Tecnologías Utilizadas

El proyecto está desarrollado completamente bajo el ecosistema de Android moderno.

* **Lenguaje:** Kotlin
* **Plataforma:** Android

## 🚀 Instalación y Ejecución

Para clonar y ejecutar este proyecto en tu entorno local, sigue estos pasos.

### Requisitos

* [Android Studio](https://developer.android.com/studio)
* SDK de Android: **Target SDK 36** (API 36), **Min SDK 28** (Android 9.0 Pie)

### Pasos

1.  **Clonar el Repositorio**
    ```bash
    git clone [https://www.youtube.com/watch?v=44ziZ12rJwU](https://www.youtube.com/watch?v=44ziZ12rJwU)
    cd Calculadora-SergioMoraMirete
    ```

2.  **Abrir en Android Studio**
    Abre Android Studio y selecciona `Open an existing Android Studio project`. Navega y selecciona el directorio raíz de la carpeta que acabas de clonar.

3.  **Sincronizar Gradle**
    Android Studio debería sincronizar automáticamente las dependencias. Si no es así, haz clic en **File > Sync Project with Gradle Files**.

4.  **Ejecutar la Aplicación**
    Selecciona un dispositivo o emulador con **API 28 o superior** y haz clic en el botón de `Run 'app'` (el ícono de flecha verde).

## 📝 Estructura del Código

La lógica principal de la calculadora se encuentra en dos archivos:

* `app/src/main/java/com/example/calculadora_sergiomoramirete/MainActivity.kt`: Contiene toda la lógica de la aplicación. Maneja la entrada de datos, el control de estado de la operación y el cálculo del resultado final utilizando la librería `exp4j`.
* `app/src/main/res/layout/activity_main.xml`: Define la interfaz de usuario con un diseño de cuadrícula (`GridLayout`) para los botones y un `TextView` para mostrar la entrada y el resultado.

## 👨‍💻 Autor

Este proyecto fue desarrollado por:

**Sergio Mora Mirete**
