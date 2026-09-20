<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>
        <c:choose>
            <c:when test="${modo == 'editar'}">Editar publicación</c:when>
            <c:otherwise>Nueva publicación</c:otherwise>
        </c:choose>
    </title>
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

    <h1>
        <c:choose>
            <c:when test="${modo == 'editar'}">Editar publicación</c:when>
            <c:otherwise>Nueva publicación</c:otherwise>
        </c:choose>
    </h1>

    <%-- Seleccionar la ruta según el modo --%>
    <c:choose>
        <c:when test="${modo == 'editar'}">
            <c:set var="accionFormulario" value="/publicaciones/editar" />
        </c:when>
        <c:otherwise>
            <c:set var="accionFormulario" value="/publicaciones/nueva" />
        </c:otherwise>
    </c:choose>

    <form action="${pageContext.request.contextPath}${accionFormulario}"
          method="post">

        <%-- Identificador necesario al editar --%>
        <c:if test="${modo == 'editar'}">
            <input type="hidden"
                   name="id"
                   value="${publicacion.idPublicacion}">
        </c:if>

        <%-- Emprendimiento --%>
        <div>
            <label for="idEmprendimiento">Emprendimiento:</label>

            <select id="idEmprendimiento"
                    name="idEmprendimiento"
                    required>

                <option value="">Seleccione un emprendimiento</option>

                <c:forEach var="emprendimiento" items="${emprendimientos}">
                    <option value="${emprendimiento.idEmprendimiento}"
                        <c:if test="${modo == 'editar' && publicacion.emprendimiento.idEmprendimiento == emprendimiento.idEmprendimiento}">
                            selected
                        </c:if>>
                        <c:out value="${emprendimiento.nombre}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <%-- Categoría --%>
        <div>
            <label for="idCategoria">Categoría:</label>

            <select id="idCategoria"
                    name="idCategoria"
                    required>

                <option value="">Seleccione una categoría</option>

                <c:forEach var="categoria" items="${categorias}">
                    <option value="${categoria.idCategoria}"
                        <c:if test="${modo == 'editar' && publicacion.categoria.idCategoria == categoria.idCategoria}">
                            selected
                        </c:if>>
                        <c:out value="${categoria.nombre}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <%-- Tipo de publicación --%>
        <div>
            <label for="tipo">Tipo de publicación:</label>

            <select id="tipo"
                    name="tipo"
                    required>

                <option value="">Seleccione un tipo</option>

                <option value="PRODUCTO"
                    <c:if test="${modo == 'editar' && publicacion.tipo == 'PRODUCTO'}">
                        selected
                    </c:if>>
                    Producto
                </option>

                <option value="SERVICIO"
                    <c:if test="${modo == 'editar' && publicacion.tipo == 'SERVICIO'}">
                        selected
                    </c:if>>
                    Servicio
                </option>
            </select>
        </div>

        <%-- Nombre --%>
        <div>
            <label for="nombre">Nombre de la publicación:</label>

            <input type="text"
                   id="nombre"
                   name="nombre"
                   value="${fn:escapeXml(publicacion.nombre)}"
                   required>
        </div>

        <%-- Descripción --%>
        <div>
            <label for="descripcion">Descripción:</label>

            <textarea id="descripcion"
                      name="descripcion"
                      rows="4"><c:out value="${publicacion.descripcion}" /></textarea>
        </div>

        <%-- Precio --%>
        <div>
            <label for="precio">Precio ($):</label>

            <input type="number"
                   id="precio"
                   name="precio"
                   value="${publicacion.precio}"
                   min="0"
                   step="0.01"
                   required>
        </div>

        <%-- Existencias --%>
        <div>
            <label for="stock">Cantidad disponible (stock):</label>

            <input type="number"
                   id="stock"
                   name="stock"
                   value="${publicacion.stock}"
                   min="0"
                   step="1">
        </div>

        <%-- Botones --%>
        <div>
            <button type="submit">
                <c:choose>
                    <c:when test="${modo == 'editar'}">Guardar cambios</c:when>
                    <c:otherwise>Crear publicación</c:otherwise>
                </c:choose>
            </button>

            <a href="${pageContext.request.contextPath}/publicaciones">
                Cancelar
            </a>
        </div>

    </form>

</body>
</html>