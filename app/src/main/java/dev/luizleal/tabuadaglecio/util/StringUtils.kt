package dev.luizleal.tabuadaglecio.util

import kotlin.random.Random

class StringUtils private constructor(){
    companion object {
        fun generateUserId(): String {
            val characters = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            var id = ""

            for (i in 1..12) {
                id += characters[Random.nextInt(0, 61)]
            }

            return id
        }
    }

}