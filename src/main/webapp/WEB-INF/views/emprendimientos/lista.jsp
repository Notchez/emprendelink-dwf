<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Emprendimientos - EmprendeLink</title>
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<h1>Emprendimientos</h1>

<c:if test="${modoGestion}">
    <p>
        <a href="${pageContext.request.contextPath}/emprendimientos/nuevo">
            Crear emprendimiento
        </a>
    </p>
    <p>
        <a href="${pageContext.request.contextPath}/publicaciones">
            Mis publicaciones
        </a>
    </p>
</c:if>

<c:choose>
    <c:when test="${empty emprendimientos}">
        <p>No hay emprendimientos para mostrar.</p>
    </c:when>

    <c:otherwise>
        <c:forEach var="emprendimiento"
                   items="${emprendimientos}">

            <article>
                <h2>
                    <c:out value="${emprendimiento.nombre}" />
                </h2>

                <p>
                    <c:out value="${emprendimiento.descripcion}" />
                </p>

                <p>
                    Contacto:
                    <c:out value="${emprendimiento.contacto}" />
                </p>

                <c:if test="${modoGestion}">
                    <a href="${pageContext.request.contextPath}/emprendimientos/editar?id=${emprendimiento.idEmprendimiento}">
                        Editar
                    </a>
                </c:if>

                <c:if test="${modoAdmin}">
                    <p>Activo: ${emprendimiento.activo}</p>

                    <form action="${pageContext.request.contextPath}/admin/emprendimientos"
                          method="post">

                        <input type="hidden"
                               name="id"
                               value="${emprendimiento.idEmprendimiento}">

                        <input type="hidden"
                               name="activo"
                               value="${not emprendimiento.activo}">

                        <button type="submit">
                            Cambiar estado
                        </button>
                    </form>
                </c:if>
            </article>

            <hr>
        </c:forEach>
    </c:otherwise>
</c:choose>

<p>
    <a href="${pageContext.request.contextPath}/catalogo">
        Catálogo
    </a>
</p>

<p>
    <a href="${pageContext.request.contextPath}/logout">
        Cerrar sesión
    </a>
</p>

</body>
</html>