package com.carlos.repository

import com.carlos.models.Project
import com.carlos.models.ProjectIcon
import com.carlos.models.ProjectLink
import com.carlos.models.Screenshot

interface ProjectRepository {
    fun findAll(): List<Project>
}

class InMemoryProjectRepository : ProjectRepository {

    private val projects = listOf(
        Project(
            id = 1,
            slug = "pokedex",
            name = "Pokedex",
            description = "App Android tipo Pokédex para explorar Pokémon, ver detalles como altura y peso, " +
                "y guardar favoritos con navegación entre dashboard y detalle.",
            descriptionEn = "Pokédex-style Android app to explore Pokémon, view details like height and weight, " +
                "and save favorites with navigation between dashboard and detail screens.",
            tags = listOf("Kotlin", "Android", "MVVM", "Compose"),
            icon = ProjectIcon.POKEDEX,
            imageUrl = "static/images/pokedex.jpg",
            links = listOf(
                ProjectLink(
                    label = "Ver en GitHub",
                    labelEn = "View on GitHub",
                    url = "https://github.com/BestCatDadEver/pokedex",
                ),
            ),
        ),
        Project(
            id = 2,
            slug = "todopro",
            name = "TodoPro",
            description = "Aplicación móvil que conecta a profesionales que ofrecen servicios " +
                "con los clientes que los necesitan.",
            descriptionEn = "Mobile app that connects professionals offering services " +
                "with the clients who need them.",
            detail = "Una sola app con dos modos: como cliente buscás el servicio que " +
                "necesitás y elegís dónde lo querés, y solo aparecen los profesionales que " +
                "cubren esa zona; como profesional recibís las reservas y las aceptás o " +
                "rechazás. La dirección exacta y el contacto del cliente se revelan recién " +
                "cuando aceptás la reserva.",
            detailEn = "One app with two modes: as a client you search for the service you " +
                "need and pick where you need it, and only the professionals covering that " +
                "area show up; as a professional you receive bookings and accept or decline " +
                "them. The exact address and the client's contact details are revealed only " +
                "once you accept.",
            icon = ProjectIcon.SERVICES,
            screenshots = listOf(
                Screenshot(
                    src = "static/images/projects/todopro/login.jpg",
                    alt = "Pantalla de acceso de TodoPro",
                    altEn = "TodoPro sign-in screen",
                ),
                Screenshot(
                    src = "static/images/projects/todopro/client-home.jpg",
                    alt = "Inicio del cliente, con buscador de servicios y accesos rápidos",
                    altEn = "Client home, with a service search box and quick-access categories",
                ),
                Screenshot(
                    src = "static/images/projects/todopro/location.jpg",
                    alt = "Selección de la zona donde se necesita el servicio",
                    altEn = "Choosing the area where the service is needed",
                ),
                Screenshot(
                    src = "static/images/projects/todopro/professional-bookings.jpg",
                    alt = "Reservas recibidas en el modo profesional, para aceptar o rechazar",
                    altEn = "Incoming bookings in professional mode, to accept or decline",
                ),
            ),
        ),
    )

    override fun findAll(): List<Project> = projects
}
