<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle del pedido - EmprendeLink</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<main>
    <c:choose>
        <c:when test="${empty pedido}">
            <h1>Pedido no encontrado</h1>
            <p>No se pudo mostrar el pedido solicitado.</p>
        </c:when>

        <c:otherwise>
            <h1>Detalle del pedido #<c:out value="${pedido.idPedido}" /></h1>

            <article>
                <h2>Información general</h2>

                <p>
                    <strong>Emprendimiento:</strong>
                    <c:out value="${pedido.emprendimiento.nombre}" />
                </p>

                <p>
                    <strong>Cliente:</strong>
                    <c:out value="${pedido.cliente.nombre}" />
                </p>

                <p>
                    <strong>Estado:</strong>
                    <c:out value="${pedido.estado}" />
                </p>

                <c:if test="${not empty pedido.observaciones}">
                    <p>
                        <strong>Observaciones:</strong>
                        <c:out value="${pedido.observaciones}" />
                    </p>
                </c:if>
            </article>

            <article>
                <h2>Productos y servicios solicitados</h2>

                <table>
                    <thead>
                    <tr>
                        <th>Publicación</th>
                        <th>Cantidad</th>
                        <th>Precio unitario</th>
                        <th>Subtotal</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach var="detalle" items="${pedido.detalles}">
                        <tr>
                            <td>
                                <c:out value="${detalle.publicacion.nombre}" />
                            </td>
                            <td>
                                <c:out value="${detalle.cantidad}" />
                            </td>
                            <td>
                                $<c:out value="${detalle.precioUnitario}" />
                            </td>
                            <td>
                                $<c:out value="${detalle.subtotal}" />
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <h2>Total del pedido: $<c:out value="${pedido.total}" /></h2>
            </article>
        </c:otherwise>
    </c:choose>

    <p>
        <c:choose>
            <c:when test="${sessionScope.usuarioAutenticado.rol.nombre eq 'ROLE_EMPRENDEDOR'}">
                <a href="${pageContext.request.contextPath}/pedidos/recibidos">
                    Volver a pedidos recibidos
                </a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/pedidos">
                    Volver a mis pedidos
                </a>
            </c:otherwise>
        </c:choose>
    </p>
</main>
</body>
</html>