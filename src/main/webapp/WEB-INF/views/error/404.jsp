<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Página no encontrada | EmprendeLink</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<main>
    <article>
        <h1>404 - Página no encontrada</h1>

        <p>
            La página que buscas no existe o ya no está disponible.
        </p>

        <p>
            Revisa la dirección e inténtalo nuevamente.
        </p>

        <a href="${pageContext.request.contextPath}/catalogo">
            Volver al catálogo
        </a>
    </article>
</main>

</body>
</html>