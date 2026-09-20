<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <title>Pedidos - EmprendeLink</title>
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<c:choose>
    <c:when test="${modoRecibidos}">
        <h1>Pedidos recibidos</h1>
    </c:when>
    <c:otherwise>
        <h1>Mis pedidos</h1>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${empty pedidos}">
        <p>Todavía no hay pedidos para mostrar.</p>
    </c:when>

    <c:otherwise>
        <c:forEach var="pedido" items="${pedidos}">
            <article class="pedido">
                <h2>Pedido #<c:out value="${pedido.idPedido}" /></h2>

                <p>
                    <strong>Emprendimiento:</strong>
                    <c:out value="${pedido.emprendimiento.nombre}" />
                </p>

                <c:if test="${modoRecibidos}">
                    <p>
                        <strong>Cliente:</strong>
                        <c:out value="${pedido.cliente.nombre}" />
                    </p>
                </c:if>

                <p>
                    <strong>Estado:</strong>
                    <span class="estado">
                        <c:out value="${pedido.estado}" />
                    </span>
                </p>

                <p>
                    <strong>Total:</strong>
                    $<c:out value="${pedido.total}" />
                </p>

                <a href="${pageContext.request.contextPath}/pedidos/detalle?id=${pedido.idPedido}">
                    Ver detalle del pedido
                </a>
                <c:if test="${modoRecibidos}">
                    <form action="${pageContext.request.contextPath}/pedidos/estado"
                          method="post">

                        <input type="hidden"
                               name="id"
                               value="${pedido.idPedido}">

                        <label for="estado-${pedido.idPedido}">
                            Actualizar estado:
                        </label>

                        <select id="estado-${pedido.idPedido}"
                                name="estado"
                                required>
                            <option value="">Selecciona un estado</option>
                            <option value="CONFIRMADO">Confirmado</option>
                            <option value="EN_PROCESO">En proceso</option>
                            <option value="COMPLETADO">Completado</option>
                            <option value="CANCELADO">Cancelado</option>
                        </select>

                        <p>
                            <button type="submit">Guardar estado</button>
                        </p>
                    </form>
                </c:if>
            </article>
        </c:forEach>
    </c:otherwise>
</c:choose>

<p>
    <a href="${pageContext.request.contextPath}/catalogo">
        Volver al catálogo
    </a>
</p>

</body>
</html>