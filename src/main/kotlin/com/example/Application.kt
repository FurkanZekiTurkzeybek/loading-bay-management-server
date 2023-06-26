package com.example

import com.example.models.loadingBay
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*

fun main() {
    println("Server is running");
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    )
        .start(wait = true)
    println("Server is shutting down");
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
