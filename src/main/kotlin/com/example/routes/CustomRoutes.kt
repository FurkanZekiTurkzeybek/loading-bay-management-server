package com.example.routes

import com.example.models.Truck
import com.example.models.loadingBay
import com.example.models.truckWaitingArea
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.observerRouting() {
    route("/observer") {
        getWaitingArea();

        getTruckByLicense();

        post {
            val truck = call.receive<Truck>();
            if (!truckWaitingArea.contains(truck)) {
                truckWaitingArea.add(truck);
                call.respondText(
                    "Truck added to list", status = HttpStatusCode.Created
                );
            } else {
                call.respondText(
                    "Truck is already at waiting area",
                    status = HttpStatusCode.Conflict
                );
            }

        }

        delete("{license?}") {
            val license =
                call.parameters["license"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest
                )

            if (truckWaitingArea.removeIf { it.getLicensePlate() == license }) {
                call.respondText(
                    "Truck removed correctly", status = HttpStatusCode.Accepted
                );
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound);
            }
        }
    }
}

fun Route.managerRouting() {
    route("/manager") {
        getWaitingArea();
        getTruckByLicense();

        post("{index?}") {
            val index =
                call.parameters["index"] ?: return@post call.respondText(
                    "no index given", status = HttpStatusCode.BadRequest
                );

            val truck = call.receive<Truck>().goToLoadingBay();
            if (loadingBay[index.toInt()].getLicensePlate() == "!Empty") {
                if (truckWaitingArea.removeIf {
                        it.getLicensePlate() == truck.getLicensePlate()
                    }) {
                    call.respondText(
                        "Truck is moved from waiting area",
                        status = HttpStatusCode.Accepted
                    );
                    loadingBay.set(index = index.toInt(), element = truck);
                } else {
                    call.respondText(
                        "The truck can't be moved",
                        status = HttpStatusCode.NotFound
                    );
                }
            } else {
                call.respondText(
                    "The given index is busy.",
                    status = HttpStatusCode.NotFound
                );
            }


        }
        delete("{index?}") {
            val listIndex =
                call.parameters["index"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest
                );
            loadingBay.set(index = listIndex.toInt(), element = Truck());
            call.respondText(
                "Truck removed from the bay", status = HttpStatusCode.Created
            );

        }
    }
}

private fun Route.getWaitingArea() {
    get {
        if (truckWaitingArea.isNotEmpty()) {
            call.respond(truckWaitingArea);
        } else {
            call.respondText(
                "there are no trucks.", status = HttpStatusCode.NotFound
            );
        }
    }
}


private fun Route.getTruckByLicense() {
    get("{license?}") {
        val license =
            call.parameters["license"] ?: return@get call.respondText(
                "no license given", status = HttpStatusCode.BadRequest
            )

        val truck = truckWaitingArea.find { it.getLicensePlate() == license }
            ?: return@get call.respondText(
                "no truck with given license $license",
                status = HttpStatusCode.NotFound
            );

        call.respond(truck);
    }
}

fun Route.managerGetLoadingBay() {

    route("/manager/list") {
        get {

            if (loadingBay.isNotEmpty()) {
                call.respond(loadingBay);
            } else {
                call.respondText(
                    "there are no trucks in loading bay.",
                    status = HttpStatusCode.OK
                );
            }
        }

    }
}
