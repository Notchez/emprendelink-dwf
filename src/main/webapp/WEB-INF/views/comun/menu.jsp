<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav aria-label="Navegación principal"
     style="padding: 15px; margin-bottom: 20px; border-bottom: 1px solid #ccc;">

    <a href="${pageContext.request.contextPath}/catalogo">
        Catálogo
    </a>

    &nbsp; | &nbsp;

    <a href="${pageContext.request.contextPath}/emprendimientos">
        Emprendimientos
    </a>

    <c:choose>

        <c:when test="${empty sessionScope.usuarioAutenticado}">

            &nbsp; | &nbsp;

            <a href="${pageContext.request.contextPath}/auth">
                Iniciar sesión
            </a>

            &nbsp; | &nbsp;

            <a href="${pageContext.request.contextPath}/registro">
                Registrarse
            </a>

        </c:when>

        <c:otherwise>

            <c:if test="${sessionScope.usuarioAutenticado.rol.nombre eq 'ROLE_EMPRENDEDOR'}">

                &nbsp; | &nbsp;

                <a href="${pageContext.request.contextPath}/emprendimientos/gestion">
                    Mis emprendimientos
                </a>

                &nbsp; | &nbsp;

                <a href="${pageContext.request.contextPath}/publicaciones">
                    Mis publicaciones
                </a>

                &nbsp; | &nbsp;

                <a href="${pageContext.request.contextPath}/publicaciones/nueva">
                    Nueva publicación
                </a>

            </c:if>

            &nbsp; | &nbsp;

            <span>
                <c:out value="${sessionScope.usuarioAutenticado.nombre}" />
            </span>

            &nbsp; | &nbsp;

            <a href="${pageContext.request.contextPath}/logout">
                Cerrar sesión
            </a>

        </c:otherwise>

    </c:choose>

</nav>