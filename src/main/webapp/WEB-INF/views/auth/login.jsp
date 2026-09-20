<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <title>Iniciar sesión - EmprendeLink</title>
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<h1>Iniciar sesión</h1>

<c:if test="${not empty error}">
    <p role="alert"><c:out value="${error}" /></p>
</c:if>

<c:if test="${param.registro eq 'exitoso'}">
    <p>Registro completado. Ya puedes iniciar sesión.</p>
</c:if>

<form action="${pageContext.request.contextPath}/auth"
      method="post">

    <label for="correo">Correo</label>
    <input id="correo"
           name="correo"
           type="email"
           value="${fn:escapeXml(correo)}"
           required>

    <br><br>

    <label for="contrasena">Contraseña</label>
    <input id="contrasena"
           name="contrasena"
           type="password"
           required>

    <br><br>

    <button type="submit">Ingresar</button>
</form>

<p>
    <a href="${pageContext.request.contextPath}/registro">
        Crear cuenta
    </a>
</p>

<p>
    <a href="${pageContext.request.contextPath}/catalogo">
        Ver catálogo público
    </a>
</p>

</body>
</html>