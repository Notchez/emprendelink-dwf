<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <title>Detalle de publicación - EmprendeLink</title>
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

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
                    <c:if test="${not empty sessionScope.usuarioAutenticado
                                  and sessionScope.usuarioAutenticado.rol.nombre eq 'ROLE_CLIENTE'
                                  and publicacion.activo
                                  and publicacion.emprendimiento.activo
                                  and publicacion.categoria.activo}">

                        <c:choose>
                            <c:when test="${publicacion.tipo eq 'PRODUCTO' and publicacion.stock le 0}">
                                <p>Este producto no tiene existencias disponibles.</p>
                            </c:when>

                            <c:otherwise>
                                <section>
                                    <h3>Realizar pedido</h3>

                                    <form action="${pageContext.request.contextPath}/pedidos/crear"
                                          method="post">

                                        <input type="hidden"
                                               name="idEmprendimiento"
                                               value="${publicacion.emprendimiento.idEmprendimiento}">

                                        <input type="hidden"
                                               name="idPublicacion"
                                               value="${publicacion.idPublicacion}">

                                        <p>
                                            <label for="cantidad">Cantidad:</label>
                                            <input type="number"
                                                   id="cantidad"
                                                   name="cantidad"
                                                   min="1"
                                                   value="1"
                                                   required
                                                   <c:if test="${publicacion.tipo eq 'PRODUCTO'}">
                                                       max="${publicacion.stock}"
                                                   </c:if>>
                                        </p>

                                        <p>
                                            <label for="observaciones">Observaciones (opcional):</label><br>
                                            <textarea id="observaciones"
                                                      name="observaciones"
                                                      rows="3"
                                                      cols="40"></textarea>
                                        </p>

                                        <button type="submit">Realizar pedido</button>
                                    </form>
                                </section>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </article>
            </c:otherwise>

        </c:choose>
    </main>

</body>
</html>