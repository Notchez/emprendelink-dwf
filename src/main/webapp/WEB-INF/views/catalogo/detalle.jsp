<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle de publicación - EmprendeLink</title>
</head>
<body>

    <header>
        <h1>Detalle de la publicación</h1>

        <a href="${pageContext.request.contextPath}/catalogo">
            Volver al catálogo
        </a>
    </header>

    <main>
        <c:choose>

            <c:when test="${empty publicacion}">
                <p>No se encontró la publicación solicitada.</p>
            </c:when>

            <c:otherwise>
                <article>

                    <h2>
                        <c:out value="${publicacion.nombre}" />
                    </h2>

                    <p>
                        <strong>Emprendimiento:</strong>
                        <c:out value="${publicacion.emprendimiento.nombre}" />
                    </p>

                    <p>
                        <strong>Categoría:</strong>
                        <c:out value="${publicacion.categoria.nombre}" />
                    </p>

                    <p>
                        <strong>Tipo:</strong>
                        <c:out value="${publicacion.tipo}" />
                    </p>

                    <h3>Descripción</h3>
                    <p>
                        <c:out value="${publicacion.descripcion}" />
                    </p>

                    <p>
                        <strong>Precio:</strong>
                        $<c:out value="${publicacion.precio}" />
                    </p>

                    <c:if test="${not empty publicacion.stock}">
                        <p>
                            <strong>Cantidad disponible:</strong>
                            <c:out value="${publicacion.stock}" />
                        </p>
                    </c:if>

                    <c:if test="${not empty publicacion.emprendimiento.contacto}">
                        <p>
                            <strong>Contacto del emprendimiento:</strong>
                            <c:out value="${publicacion.emprendimiento.contacto}" />
                        </p>
                    </c:if>

                </article>
            </c:otherwise>

        </c:choose>
    </main>

</body>
</html>