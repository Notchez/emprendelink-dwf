<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catálogo de publicaciones - EmprendeLink</title>
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

    <header>
        <h1>Catálogo de publicaciones</h1>
        <p>Explora los productos y servicios de nuestros emprendedores.</p>
    </header>

    <main>
        <c:choose>

            <%-- Mensaje cuando no hay publicaciones disponibles --%>
            <c:when test="${empty publicaciones}">
                <p>No hay publicaciones disponibles por el momento.</p>
            </c:when>

            <%-- Mostrar las publicaciones disponibles --%>
            <c:otherwise>

                <section>
                    <c:forEach var="publicacion" items="${publicaciones}">

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

                            <p>
                                <strong>Descripción:</strong>
                                <c:out value="${publicacion.descripcion}" />
                            </p>

                            <p>
                                <strong>Precio:</strong>
                                $<c:out value="${publicacion.precio}" />
                            </p>

                            <a href="${pageContext.request.contextPath}/catalogo/publicacion?id=${publicacion.idPublicacion}">
                                Ver detalles
                            </a>
                        </article>

                        <hr>

                    </c:forEach>
                </section>

            </c:otherwise>
        </c:choose>
    </main>

</body>
</html>