package com.example.lab8danp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// @HiltAndroidApp inicializa Hilt en la aplicación y genera el contenedor base.
@HiltAndroidApp
class VideoCardApp : Application()