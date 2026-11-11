(function () {
    var STORAGE_KEY = "lang";
    var translatable = document.querySelectorAll("[data-en]");
    // Los textos alternativos de las capturas no son contenido del elemento, asi
    // que se traducen aparte: el atributo alt no lo toca textContent.
    var translatableAlt = document.querySelectorAll("[data-en-alt]");

    translatable.forEach(function (el) {
        if (el.dataset.es === undefined) {
            el.dataset.es = el.textContent;
        }
    });

    translatableAlt.forEach(function (el) {
        if (el.dataset.esAlt === undefined) {
            el.dataset.esAlt = el.alt;
        }
    });

    function applyLanguage(lang) {
        document.documentElement.lang = lang;
        document.body.classList.toggle("lang-en", lang === "en");

        translatable.forEach(function (el) {
            el.textContent = lang === "en" ? el.dataset.en : el.dataset.es;
        });

        translatableAlt.forEach(function (el) {
            el.alt = lang === "en" ? el.dataset.enAlt : el.dataset.esAlt;
        });

        document.querySelectorAll("[data-lang-btn]").forEach(function (btn) {
            var isActive = btn.getAttribute("data-lang-btn") === lang;
            btn.classList.toggle("active", isActive);
            btn.setAttribute("aria-pressed", isActive ? "true" : "false");
        });

        try {
            localStorage.setItem(STORAGE_KEY, lang);
        } catch (e) {
            // localStorage unavailable, ignore
        }
    }

    document.querySelectorAll("[data-lang-btn]").forEach(function (btn) {
        btn.addEventListener("click", function () {
            applyLanguage(btn.getAttribute("data-lang-btn"));
        });
    });

    var savedLang = null;
    try {
        savedLang = localStorage.getItem(STORAGE_KEY);
    } catch (e) {
        // localStorage unavailable, ignore
    }

    if (savedLang === "en") {
        applyLanguage("en");
    }
})();
