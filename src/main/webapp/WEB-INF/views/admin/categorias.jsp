<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administrar categorías - EmprendeLink</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<main>
    <h1>Administrar categorías</h1>
    <p>Crea categorías y administra las que ya existen.</p>

    <article>
        <h2>Nueva categoría</h2>

        <form action="${pageContext.request.contextPath}/admin/categorias"
              method="post">

            <input type="hidden" name="accion" value="crear">

            <p>
                <label for="nombre-nuevo">Nombre</label>
                <input id="nombre-nuevo"
                       type="text"
                       name="nombre"
                       maxlength="80"
                       required>
            </p>

            <p>
                <label for="descripcion-nueva">Descripción</label>
                <textarea id="descripcion-nueva"
                          name="descripcion"
                          rows="3"></textarea>
            </p>

            <button type="submit">Crear categoría</button>
        </form>
    </article>

    <h2>Categorías registradas</h2>

    <c:choose>
        <c:when test="${empty categorias}">
            <p>No hay categorías registradas.</p>
        </c:when>

        <c:otherwise>
            <c:forEach var="categoria" items="${categorias}">
                <article>
                    <h3>
                        <c:out value="${categoria.nombre}" />
                    </h3>

                    <p>
                        <strong>ID:</strong>
                        <c:out value="${categoria.idCategoria}" />
                    </p>

                    <p>
                        <strong>Estado:</strong>
                        <c:choose>
                            <c:when test="${categoria.activo}">
                                Activa
                            </c:when>
                            <c:otherwise>
                                Inactiva
                            </c:otherwise>
                        </c:choose>
                    </p>

                    <form action="${pageContext.request.contextPath}/admin/categorias"
                          method="post">

                        <input type="hidden" name="accion" value="editar">
                        <input type="hidden"
                               name="id"
                               value="${categoria.idCategoria}">

                        <p>
                            <label for="nombre-${categoria.idCategoria}">
                                Nombre
                            </label>
                            <input id="nombre-${categoria.idCategoria}"
                                   type="text"
                                   name="nombre"
                                   value="<c:out value='${categoria.nombre}' />"
                                   maxlength="80"
                                   required>
                        </p>

                        <p>
                            <label for="descripcion-${categoria.idCategoria}">
                                Descripción
                            </label>
                            <textarea id="descripcion-${categoria.idCategoria}"
                                      name="descripcion"
                                      rows="3"><c:out value="${categoria.descripcion}" /></textarea>
                        </p>

                        <button type="submit">Guardar cambios</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/admin/categorias"
                          method="post">

                        <input type="hidden" name="accion" value="estado">
                        <input type="hidden"
                               name="id"
                               value="${categoria.idCategoria}">

                        <c:choose>
                            <c:when test="${categoria.activo}">
                                <input type="hidden"
                                       name="activo"
                                       value="false">
                                <button type="submit">Desactivar categoría</button>
                            </c:when>

                            <c:otherwise>
                                <input type="hidden"
                                       name="activo"
                                       value="true">
                                <button type="submit">Activar categoría</button>
                            </c:otherwise>
                        </c:choose>
                    </form>
                </article>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</main>

</body>
</html>