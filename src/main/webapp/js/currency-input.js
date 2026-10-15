(() => {
    "use strict";

    const selector = ".currency-cents-input";

    function obtenerCentavos(input) {
        if (input.dataset.cents !== undefined) {
            return Number.parseInt(
                input.dataset.cents,
                10
            ) || 0;
        }

        const numero = Number.parseFloat(
            String(input.value)
                .replace(/,/g, "")
        );

        const centavos =
            Number.isFinite(numero)
                ? Math.round(numero * 100)
                : 0;

        input.dataset.cents =
            String(centavos);

        return centavos;
    }

    function mostrar(input, centavos) {
        const seguro =
            Math.max(
                0,
                centavos
            );

        input.dataset.cents =
            String(seguro);

        input.value =
            (seguro / 100)
                .toFixed(2);
    }

    function preparar(input) {
        const centavos =
            obtenerCentavos(
                input
            );

        mostrar(
            input,
            centavos
        );
    }

    document.addEventListener(
        "focusin",
        (event) => {
            const input =
                event.target.closest(
                    selector
                );

            if (!input) {
                return;
            }

            preparar(
                input
            );

            input.dataset.replaceNext =
                "true";

            requestAnimationFrame(
                () => input.select()
            );
        }
    );

    document.addEventListener(
        "keydown",
        (event) => {
            const input =
                event.target.closest(
                    selector
                );

            if (!input) {
                return;
            }

            if (/^\d$/.test(event.key)) {
                event.preventDefault();

                const reemplazar =
                    input.dataset.replaceNext
                    === "true";

                const actual =
                    reemplazar
                        ? 0
                        : obtenerCentavos(
                            input
                        );

                const nuevo =
                    actual * 10
                    + Number.parseInt(
                        event.key,
                        10
                    );

                input.dataset.replaceNext =
                    "false";

                mostrar(
                    input,
                    nuevo
                );

                return;
            }

            if (event.key === "Backspace") {
                event.preventDefault();

                const reemplazar =
                    input.dataset.replaceNext
                    === "true";

                const actual =
                    reemplazar
                        ? 0
                        : obtenerCentavos(
                            input
                        );

                input.dataset.replaceNext =
                    "false";

                mostrar(
                    input,
                    Math.floor(
                        actual / 10
                    )
                );

                return;
            }

            if (event.key === "Delete") {
                event.preventDefault();

                input.dataset.replaceNext =
                    "false";

                mostrar(
                    input,
                    0
                );
            }
        }
    );

    document.addEventListener(
        "paste",
        (event) => {
            const input =
                event.target.closest(
                    selector
                );

            if (!input) {
                return;
            }

            event.preventDefault();

            const texto =
                event.clipboardData
                    ?.getData("text")
                ?? "";

            const digitos =
                texto.replace(
                    /\D/g,
                    ""
                );

            const centavos =
                digitos.length === 0
                    ? 0
                    : Number.parseInt(
                        digitos,
                        10
                    );

            input.dataset.replaceNext =
                "false";

            mostrar(
                input,
                centavos
            );
        }
    );

    document.addEventListener(
        "DOMContentLoaded",
        () => {
            document
                .querySelectorAll(
                    selector
                )
                .forEach(
                    preparar
                );
        }
    );
})();