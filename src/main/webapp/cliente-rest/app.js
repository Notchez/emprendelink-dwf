
"use strict";

const API_URL = "/emprendelink/api/v1";

let csrfToken = null;
let publicaciones = [];
let carrito = [];

const elemento = (id) => document.getElementById(id);

function mostrarMensaje(texto, tipo = "") {
    const mensaje = elemento("mensaje-general");
    mensaje.textContent = texto;
    mensaje.className = tipo;
}

function obtenerLista(datos) {
    if (Array.isArray(datos)) {
        return datos;
    }

    if (datos && typeof datos === "object") {
        for (const clave of ["items", "content", "data", "resultados"]) {
            if (Array.isArray(datos[clave])) {
                return datos[clave];
            }
        }
    }

    throw new Error("La API devolvió un formato de lista inesperado.");
}

async function solicitarAPI(ruta, opciones = {}) {
    const metodo = opciones.method || "GET";

    const headers = {
        Accept: "application/json",
        ...opciones.headers
    };

    if (metodo !== "GET" && metodo !== "HEAD" && csrfToken) {
        headers["X-CSRF-Token"] = csrfToken;
    }

    let respuesta;

    try {
        respuesta = await fetch(`${API_URL}${ruta}`, {
            ...opciones,
            method: metodo,
            credentials: "same-origin",
            headers
        });
    } catch (error) {
        throw new Error(
            "No fue posible conectar con el servidor. " +
            "Comprueba que GlassFish esté ejecutándose."
        );
    }

    const contenido = await respuesta.text();
    let datos = null;

    if (contenido) {
        try {
            datos = JSON.parse(contenido);
        } catch {
            if (respuesta.ok) {
                throw new Error("El servidor no devolvió JSON válido.");
            }
        }
    }

    if (!respuesta.ok) {
        const detalle = datos?.message
            || datos?.mensaje
            || datos?.error
            || `Error HTTP ${respuesta.status}`;

        throw new Error(
            typeof detalle === "string"
                ? detalle
                : `Error HTTP ${respuesta.status}`
        );
    }

    return datos;
}

async function recuperarSesion() {
    try {
        const sesion = await solicitarAPI("/auth/me");

        csrfToken = sesion?.csrfToken || null;

        if (!csrfToken) {
            return;
        }

        elemento("estado-sesion").textContent = "Sesión iniciada.";
        elemento("form-login").hidden = true;
        elemento("btn-logout").hidden = false;

        await cargarPedidos();

    } catch (error) {
        csrfToken = null;
        elemento("estado-sesion").textContent = "No has iniciado sesión.";
        elemento("form-login").hidden = false;
        elemento("btn-logout").hidden = true;
    }
}

function crearTarjeta(titulo, descripcion) {
    const tarjeta = document.createElement("article");
    tarjeta.className = "tarjeta";

    const encabezado = document.createElement("h3");
    encabezado.textContent = titulo;

    const parrafo = document.createElement("p");
    parrafo.textContent = descripcion;

    tarjeta.append(encabezado, parrafo);
    return tarjeta;
}

async function cargarEmprendimientos() {
    const contenedor = elemento("lista-emprendimientos");
    const estado = elemento("estado-emprendimientos");

    contenedor.replaceChildren();
    estado.textContent = "Cargando emprendimientos...";

    try {
        const datos = await solicitarAPI("/emprendimientos");
        const lista = obtenerLista(datos);

        if (lista.length === 0) {
            estado.textContent = "No hay emprendimientos disponibles.";
            return;
        }

        for (const negocio of lista) {
            const nombre = negocio.nombre || "Emprendimiento";
            const descripcion = negocio.descripcion || "Sin descripción";

            const tarjeta = crearTarjeta(nombre, descripcion);
            contenedor.appendChild(tarjeta);
        }

        estado.textContent = `${lista.length} emprendimiento(s) encontrado(s).`;
    } catch (error) {
        estado.textContent = error.message;
        estado.className = "error";
    }
}

async function cargarPublicaciones() {
    const contenedor = elemento("lista-publicaciones");
    const estado = elemento("estado-publicaciones");

    contenedor.replaceChildren();
    estado.textContent = "Cargando publicaciones...";

    try {
        const datos = await solicitarAPI("/publicaciones");
        publicaciones = obtenerLista(datos);

        if (publicaciones.length === 0) {
            estado.textContent = "No hay publicaciones disponibles.";
            return;
        }

        for (const publicacion of publicaciones) {
            const nombre = publicacion.titulo
                || publicacion.nombre
                || "Publicación";

            const descripcion = publicacion.descripcion || "Sin descripción";

            const tarjeta = crearTarjeta(nombre, descripcion);

            if (publicacion.precio != null) {
                const precio = document.createElement("p");
                precio.textContent = `Precio: $${Number(publicacion.precio).toFixed(2)}`;
                tarjeta.appendChild(precio);
            }

            const boton = document.createElement("button");
            boton.type = "button";

            if (publicacion.stock <= 0) {
                boton.textContent = "Agotado";
                boton.disabled = true;
            } else {
                boton.textContent = "Agregar al carrito";

                boton.addEventListener("click", () => {
                    agregarAlCarrito(publicacion);
                });
            }

            tarjeta.appendChild(boton);
            contenedor.appendChild(tarjeta);
        }

        estado.textContent = `${publicaciones.length} publicación(es) encontrada(s).`;
        estado.className = "";
    } catch (error) {
        estado.textContent = error.message;
        estado.className = "error";
    }
}

function agregarAlCarrito(publicacion) {
    const id = publicacion.idPublicacion ?? publicacion.id;

    if (id == null) {
        mostrarMensaje(
            "Esta publicación no tiene un identificador válido.",
            "error"
        );
        return;
    }

    const existente = carrito.find(item => item.idPublicacion === id);

    if (existente) {
        existente.cantidad++;
    } else {
        carrito.push({
            idPublicacion: id,
            cantidad: 1,
            nombre: publicacion.titulo || publicacion.nombre || "Publicación",
            precio: Number(publicacion.precio) || 0
        });
    }

    mostrarCarrito();
    mostrarMensaje("Publicación agregada al carrito.", "exito");
}

function mostrarCarrito() {
    const contenedor = elemento("lista-carrito");
    contenedor.replaceChildren();

    if (carrito.length === 0) {
        contenedor.textContent = "Tu carrito está vacío.";
        elemento("total-carrito").textContent = "";
        return;
    }

    let total = 0;

    carrito.forEach((item, indice) => {
        const fila = document.createElement("div");
        fila.className = "item-carrito";

        const subtotal = item.precio * item.cantidad;
        total += subtotal;

        const descripcion = document.createElement("span");
        descripcion.textContent =
            `${item.nombre} - Cantidad: ${item.cantidad} - $${subtotal.toFixed(2)} `;

        const eliminar = document.createElement("button");
        eliminar.type = "button";
        eliminar.textContent = "Eliminar";

        eliminar.addEventListener("click", () => {
            carrito.splice(indice, 1);
            mostrarCarrito();
        });

        fila.append(descripcion, eliminar);
        contenedor.appendChild(fila);
    });

    elemento("total-carrito").textContent =
        `Total estimado: $${total.toFixed(2)}`;
}

async function iniciarSesion(evento) {
    evento.preventDefault();

    const correo = elemento("correo").value.trim();
    const contrasena = elemento("contrasena").value;

    if (!correo || !contrasena) {
        mostrarMensaje("Completa correo y contraseña.", "error");
        return;
    }

    try {
        const sesion = await solicitarAPI("/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ correo, contrasena })
        });

        csrfToken = sesion?.csrfToken || null;

        elemento("estado-sesion").textContent = "Sesión iniciada.";
        elemento("form-login").hidden = true;
        elemento("btn-logout").hidden = false;
        elemento("contrasena").value = "";

        mostrarMensaje("Inicio de sesión exitoso.", "exito");
    } catch (error) {
        mostrarMensaje(`No se pudo iniciar sesión: ${error.message}`, "error");
    }
}

async function cerrarSesion() {
    try {
        await solicitarAPI("/auth/logout", {
            method: "POST"
        });

        csrfToken = null;
        elemento("estado-sesion").textContent = "No has iniciado sesión.";
        elemento("form-login").hidden = false;
        elemento("btn-logout").hidden = true;
        elemento("lista-pedidos").replaceChildren();

        mostrarMensaje("Sesión cerrada.", "exito");
    } catch (error) {
        mostrarMensaje(error.message, "error");
    }
}

async function realizarCheckout(evento) {
    evento.preventDefault();

    if (!csrfToken) {
        mostrarMensaje(
            "Debes iniciar sesión como cliente antes de comprar.",
            "error"
        );
        return;
    }

    if (carrito.length === 0) {
        mostrarMensaje("Agrega publicaciones al carrito.", "error");
        return;
    }

    const direccionEntrega = elemento("direccion").value.trim();
    const observaciones = elemento("observaciones").value.trim();

    if (direccionEntrega.length < 5) {
        mostrarMensaje("Escribe una dirección de entrega válida.", "error");
        return;
    }

    const boton = elemento("form-checkout").querySelector("button");
    boton.disabled = true;

    try {
        await solicitarAPI("/pedidos/checkout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                items: carrito.map(item => ({
                    idPublicacion: item.idPublicacion,
                    cantidad: item.cantidad
                })),
                direccionEntrega,
                observaciones: observaciones || null
            })
        });

        carrito = [];
        mostrarCarrito();
        elemento("form-checkout").reset();

        mostrarMensaje("¡Pedido realizado correctamente!", "exito");
        await cargarPedidos();
    } catch (error) {
        mostrarMensaje(`No se pudo realizar el pedido: ${error.message}`, "error");
    } finally {
        boton.disabled = false;
    }
}

async function cargarPedidos() {
    const contenedor = elemento("lista-pedidos");
    contenedor.textContent = "Cargando pedidos...";

    try {
        const datos = await solicitarAPI("/pedidos");
        const pedidos = obtenerLista(datos);

        contenedor.replaceChildren();

        if (pedidos.length === 0) {
            contenedor.textContent = "No tienes pedidos registrados.";
            return;
        }

        for (const pedido of pedidos) {
            const id = pedido.idPedido ?? pedido.id ?? "Sin número";
            const estado = pedido.estado ?? "Sin estado";

            const tarjeta = crearTarjeta(
                `Pedido #${id}`,
                `Estado: ${estado}`
            );

            contenedor.appendChild(tarjeta);
        }
    } catch (error) {
        contenedor.textContent = error.message;
        contenedor.className = "error";
    }
}

elemento("form-login").addEventListener("submit", iniciarSesion);
elemento("btn-logout").addEventListener("click", cerrarSesion);
elemento("form-checkout").addEventListener("submit", realizarCheckout);

elemento("btn-emprendimientos")
    .addEventListener("click", cargarEmprendimientos);

elemento("btn-publicaciones")
    .addEventListener("click", cargarPublicaciones);

elemento("btn-pedidos")
    .addEventListener("click", cargarPedidos);

mostrarCarrito();
cargarEmprendimientos();
cargarPublicaciones();
recuperarSesion();