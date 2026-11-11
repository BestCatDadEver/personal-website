(function () {
    var cards = document.querySelectorAll("[data-project-open]");
    if (!cards.length) {
        return;
    }

    var lightbox = document.getElementById("lightbox");

    function dialogFor(card) {
        return document.getElementById("project-" + card.getAttribute("data-project-open"));
    }

    function openDialog(dialog) {
        // showModal da Escape, captura del foco y fondo inerte sin codigo propio.
        // El atributo "open" es el plan B en navegadores que no lo implementan:
        // la ficha se ve, aunque sin esas tres cosas.
        if (typeof dialog.showModal === "function") {
            dialog.showModal();
        } else {
            dialog.setAttribute("open", "");
        }
    }

    function closeDialog(dialog) {
        if (typeof dialog.close === "function") {
            dialog.close();
        } else {
            dialog.removeAttribute("open");
        }
        syncBodyLock();
    }

    /**
     * El bloqueo del scroll se deduce de si queda alguna ficha abierta, en vez de
     * ponerlo y quitarlo desde cada sitio que abre o cierra. Con varios dialogos
     * apilados es facil que una de esas llamadas sobre o falte y el fondo quede
     * bloqueado para siempre.
     */
    function syncBodyLock() {
        document.body.classList.toggle(
            "dialog-open",
            document.querySelector(".project-dialog[open]") !== null
        );
    }

    cards.forEach(function (card) {
        var dialog = dialogFor(card);
        if (!dialog) {
            return;
        }

        function open() {
            openDialog(dialog);
            syncBodyLock();
        }

        card.addEventListener("click", open);

        // La tarjeta es un div con role="button", asi que el teclado no la activa
        // sola: hay que reproducir lo que un <button> hace con Enter y Espacio.
        card.addEventListener("keydown", function (event) {
            if (event.key === "Enter" || event.key === " " || event.key === "Spacebar") {
                event.preventDefault();
                open();
            }
        });
    });

    document.querySelectorAll(".project-dialog").forEach(function (dialog) {
        dialog.addEventListener("close", syncBodyLock);

        // Con el visor abierto encima, Escape llega como "cancel" a los dos
        // dialogos y cerraria tambien la ficha. Solo debe cerrarse el de arriba.
        dialog.addEventListener("cancel", function (event) {
            if (lightbox && lightbox.open) {
                event.preventDefault();
            }
        });

        // Pulsar fuera de la ficha la cierra. El contenido vive en un hijo que
        // cubre todo el cuadro, asi que un click cuyo destino sea el <dialog>
        // mismo solo puede venir del area de fondo.
        dialog.addEventListener("click", function (event) {
            if (event.target === dialog) {
                closeDialog(dialog);
            }
        });

        dialog.querySelectorAll("[data-project-close]").forEach(function (button) {
            button.addEventListener("click", function () {
                closeDialog(dialog);
            });
        });
    });

    // --- Ampliar capturas --------------------------------------------------
    if (!lightbox) {
        return;
    }
    var lightboxImage = lightbox.querySelector(".lightbox-image");

    document.querySelectorAll(".project-shot").forEach(function (shot) {
        shot.addEventListener("click", function () {
            var source = shot.querySelector("img");
            if (!source) {
                return;
            }
            // La imagen se toma del propio <img> en vez de duplicar la ruta en un
            // atributo, asi no hay dos sitios que puedan quedar desalineados.
            lightboxImage.src = source.src;
            lightboxImage.alt = source.alt;
            // Segundo dialogo modal sobre la ficha: el navegador los apila.
            openDialog(lightbox);
        });
    });

    lightbox.addEventListener("click", function (event) {
        // Aqui no hay contenedor interior que cubra el cuadro: cualquier click
        // que no sea sobre la propia imagen cierra el visor. Eso cubre tanto el
        // fondo como el boton de cerrar, sin un listener aparte para el boton.
        if (event.target !== lightboxImage) {
            closeDialog(lightbox);
        }
    });
})();
