<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav aria-label="Navegación principal"
     style="display: flex; flex-wrap: wrap; align-items: center; gap: 16px;">

    <%-- 1. Páginas públicas --%>
    <%-- Navegación pública: visitantes y clientes --%>
    <c:if test="${empty sessionScope.usuarioAutenticado
                  or sessionScope.usuarioAutenticado.rol.nombre eq 'ROLE_CLIENTE'}">

        <a href="${pageContext.request.contextPath}/catalogo">
            Catálogo
        </a>

        <a href="${pageContext.request.contextPath}/emprendimientos">
            Emprendimientos
        </a>

    </c:if>

    <c:choose>

        <%-- 2. Opciones para visitantes --%>
        <c:when test="${empty sessionScope.usuarioAutenticado}">

            <a href="${pageContext.request.contextPath}/auth">
                Iniciar sesión
            </a>

            <a href="${pageContext.request.contextPath}/registro">
                Registrarse
            </a>

        </c:when>

        <%-- 3. Opciones para usuarios autenticados --%>
        <c:otherwise>

            <%-- Cliente --%>
            <c:if test="${sessionScope.usuarioAutenticado.rol.nombre eq 'ROLE_CLIENTE'}">

                <a href="${pageContext.request.contextPath}/pedidos">
                    Mis pedidos
                </a>

            </c:if>

            <%-- Emprendedor --%>
            <c:if test="${sessionScope.usuarioAutenticado.rol.nombre eq 'ROLE_EMPRENDEDOR'}">

                <a href="${pageContext.request.contextPath}/emprendimientos/gestion">
                    Mis emprendimientos
                </a>

                <a href="${pageContext.request.contextPath}/publicaciones">
                    Mis publicaciones
                </a>

                <a href="${pageContext.request.contextPath}/publicaciones/nueva">
                    Nueva publicación
                </a>

                <a href="${pageContext.request.contextPath}/pedidos/recibidos">
                    Pedidos recibidos
                </a>

            </c:if>

            <%-- Administrador --%>
            <c:if test="${sessionScope.usuarioAutenticado.rol.nombre eq 'ROLE_ADMIN'}">

                <a href="${pageContext.request.contextPath}/admin/categorias">
                    Administrar categorías
                </a>

                <a href="${pageContext.request.contextPath}/admin/usuarios">
                    Administrar usuarios
                </a>

            </c:if>

            <%-- 4. Cuenta y salida --%>
            <span>
                <c:out value="${sessionScope.usuarioAutenticado.nombre}" />
            </span>

            <a href="${pageContext.request.contextPath}/logout">
                Cerrar sesión
            </a>

        </c:otherwise>

    </c:choose>

</nav>