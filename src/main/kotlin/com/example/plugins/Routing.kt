package com.example.plugins

import com.example.routes.managerGetLoadingBay
import com.example.routes.managerRouting
import com.example.routes.observerRouting
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        observerRouting();
        managerRouting();
        managerGetLoadingBay();
    }
}
