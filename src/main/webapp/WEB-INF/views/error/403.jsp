<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>403 - Acceso denegado | EmprendeLink</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<main>
    <article>
        <h1>403 - Acceso denegado</h1>

        <p>
            No tienes permisos para acceder a esta página
            con tu cuenta actual.
        </p>

        <p>
            Si necesitas utilizar esta función, inicia sesión
            con una cuenta que tenga los permisos correspondientes.
        </p>

        <a href="${pageContext.request.contextPath}/catalogo">
            Volver al catálogo
        </a>
    </article>
</main>

</body>
</html>