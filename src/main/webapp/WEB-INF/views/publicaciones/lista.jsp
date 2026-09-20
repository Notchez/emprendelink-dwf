<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Publicaciones - EmprendeLink</title>
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<h1>Publicaciones</h1>

<c:if test="${not modoAdmin}">
    <p>
        <a href="${pageContext.request.contextPath}/publicaciones/nueva">
            Crear publicación
        </a>
    </p>
</c:if>

<c:choose>
    <c:when test="${empty publicaciones}">
        <p>No hay publicaciones para mostrar.</p>
    </c:when>

    <c:otherwise>
        <c:forEach var="publicacion"
                   items="${publicaciones}">

            <article>
                <h2>
                    <c:out value="${publicacion.nombre}" />
                </h2>

                <p>
                    <c:out value="${publicacion.descripcion}" />
                </p>

                <p>
                    Precio:
                    $<c:out value="${publicacion.precio}" />
                </p>

                <p>
                    Emprendimiento:
                    <c:out value="${publicacion.emprendimiento.nombre}" />
                </p>

                <c:choose>
                    <c:when test="${modoAdmin}">

                        <p>Activo: ${publicacion.activo}</p>

                        <form action="${pageContext.request.contextPath}/admin/publicaciones"
                              method="post">

                            <input type="hidden"
                                   name="id"
                                   value="${publicacion.idPublicacion}">

                            <input type="hidden"
                                   name="activo"
                                   value="${not publicacion.activo}">

                            <button type="submit">
                                Cambiar estado
                            </button>
                        </form>

                    </c:when>

                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/publicaciones/editar?id=${publicacion.idPublicacion}">
                            Editar publicación
                        </a>
                    </c:otherwise>
                </c:choose>
            </article>

            <hr>
        </c:forEach>
    </c:otherwise>
</c:choose>

<p>
    <a href="${pageContext.request.contextPath}/emprendimientos/gestion">
        Mis emprendimientos
    </a>
</p>

<p>
    <a href="${pageContext.request.contextPath}/catalogo">
        Catálogo público
    </a>
</p>

</body>
</html>