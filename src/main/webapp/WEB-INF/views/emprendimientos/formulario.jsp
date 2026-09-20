<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <title>
        <c:choose>
            <c:when test="${modo eq 'editar'}">Editar emprendimiento</c:when>
            <c:otherwise>Nuevo emprendimiento</c:otherwise>
        </c:choose>
    </title>
</head>

<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<main>

    <c:choose>
        <c:when test="${modo eq 'editar'}">
            <h1>Editar emprendimiento</h1>
        </c:when>
        <c:otherwise>
            <h1>Registrar emprendimiento</h1>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${modo eq 'editar'}">
            <c:set var="rutaFormulario"
                   value="/emprendimientos/editar" />
        </c:when>

        <c:otherwise>
            <c:set var="rutaFormulario"
                   value="/emprendimientos/nuevo" />
        </c:otherwise>
    </c:choose>

    <form action="${pageContext.request.contextPath}${rutaFormulario}"
          method="post"
          accept-charset="UTF-8">

        <c:if test="${modo eq 'editar'}">
            <input type="hidden"
                   name="id"
                   value="${emprendimiento.idEmprendimiento}">
        </c:if>

        <div>
            <label for="nombre">Nombre del emprendimiento *</label>

            <input type="text"
                   id="nombre"
                   name="nombre"
                   value="${fn:escapeXml(emprendimiento.nombre)}"
                   required>

        </div>

        <div>
            <label for="descripcion">Descripción</label>

            <textarea id="descripcion"
                      name="descripcion"
                      rows="5"><c:out value="${emprendimiento.descripcion}" /></textarea>
        </div>

        <div>
            <label for="contacto">Contacto</label>

            <input type="text"
                   id="contacto"
                   name="contacto"
                   value="${fn:escapeXml(emprendimiento.contacto)}">
        </div>

        <div>
            <button type="submit">
                <c:choose>
                    <c:when test="${modo eq 'editar'}">
                        Guardar cambios
                    </c:when>

                    <c:otherwise>
                        Registrar emprendimiento
                    </c:otherwise>
                </c:choose>
            </button>

            <a href="${pageContext.request.contextPath}/emprendimientos/gestion">
                Cancelar
            </a>
        </div>

    </form>

</main>

</body>
</html>