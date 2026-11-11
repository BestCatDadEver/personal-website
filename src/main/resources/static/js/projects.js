(function () {
    var cards = document.querySelectorAll("[data-project-open]");
    if (!cards.length) {
        return;
    }

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
        document.body.classList.add("dialog-open");
    }

    function closeDialog(dialog) {
        if (typeof dialog.close === "function") {
            dialog.close();
        } else {
            dialog.removeAttribute("open");
            document.body.classList.remove("dialog-open");
        }
    }

    cards.forEach(function (card) {
        var dialog = dialogFor(card);
        if (!dialog) {
            return;
        }

        card.addEventListener("click", function () {
            openDialog(dialog);
        });

        // La tarjeta es un div con role="button", asi que el teclado no la activa
        // sola: hay que reproducir lo que un <button> hace con Enter y Espacio.
        card.addEventListener("keydown", function (event) {
            if (event.key === "Enter" || event.key === " " || event.key === "Spacebar") {
                event.preventDefault();
                openDialog(dialog);
            }
        });
    });

    document.querySelectorAll(".project-dialog").forEach(function (dialog) {
        dialog.addEventListener("close", function () {
            document.body.classList.remove("dialog-open");
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
})();
