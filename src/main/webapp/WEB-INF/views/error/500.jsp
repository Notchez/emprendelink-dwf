<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - Error del servidor | EmprendeLink</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<main>
    <article>
        <h1>500 - Error interno del servidor</h1>

        <p>
            Ocurrió un problema al procesar tu solicitud.
        </p>

        <p>
            Inténtalo nuevamente más tarde. Si el problema continúa,
            comunícate con el administrador del sitio.
        </p>

        <a href="${pageContext.request.contextPath}/catalogo">
            Volver al catálogo
        </a>
    </article>
</main>

</body>
</html>