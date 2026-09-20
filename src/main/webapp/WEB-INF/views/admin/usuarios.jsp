<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administrar usuarios - EmprendeLink</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<main>
    <h1>Administrar usuarios</h1>
    <p>Consulta los usuarios registrados y administra el estado de sus cuentas.</p>

    <c:choose>
        <c:when test="${empty usuarios}">
            <article>
                <p>No hay usuarios registrados.</p>
            </article>
        </c:when>

        <c:otherwise>
            <c:forEach var="usuario" items="${usuarios}">
                <article>
                    <h2>
                        <c:out value="${usuario.nombre}" />
                        <c:out value="${usuario.apellido}" />
                    </h2>

                    <p>
                        <strong>ID:</strong>
                        <c:out value="${usuario.idUsuario}" />
                    </p>

                    <p>
                        <strong>Correo:</strong>
                        <c:out value="${usuario.correo}" />
                    </p>

                    <p>
                        <strong>Rol:</strong>
                        <c:out value="${usuario.rol.nombre}" />
                    </p>

                    <p>
                        <strong>Estado:</strong>
                        <c:choose>
                            <c:when test="${usuario.activo}">
                                Activo
                            </c:when>
                            <c:otherwise>
                                Inactivo
                            </c:otherwise>
                        </c:choose>
                    </p>

                    <%-- No mostrar la opción de desactivar
                         la cuenta que está usando el administrador. --%>
                    <c:choose>
                        <c:when test="${usuario.idUsuario eq sessionScope.usuarioAutenticado.idUsuario}">
                            <p><em>Esta es tu cuenta actual.</em></p>
                        </c:when>

                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/admin/usuarios"
                                  method="post">

                                <input type="hidden"
                                       name="id"
                                       value="${usuario.idUsuario}">

                                <c:choose>
                                    <c:when test="${usuario.activo}">
                                        <input type="hidden"
                                               name="activo"
                                               value="false">

                                        <button type="submit">
                                            Desactivar usuario
                                        </button>
                                    </c:when>

                                    <c:otherwise>
                                        <input type="hidden"
                                               name="activo"
                                               value="true">

                                        <button type="submit">
                                            Activar usuario
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </article>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</main>

</body>
</html>