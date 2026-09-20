<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro - EmprendeLink</title>
</head>
<body>

<jsp:include page="/WEB-INF/views/comun/menu.jsp" />

<h1>Crear cuenta</h1>

<c:if test="${not empty error}">
    <p role="alert"><c:out value="${error}" /></p>
</c:if>

<form action="${pageContext.request.contextPath}/registro"
      method="post">

    <label for="nombre">Nombre</label>
    <input id="nombre" name="nombre" required>

    <br><br>

    <label for="apellido">Apellido</label>
    <input id="apellido" name="apellido" required>

    <br><br>

    <label for="correo">Correo</label>
    <input id="correo"
           name="correo"
           type="email"
           required>

    <br><br>

    <label for="contrasena">Contraseña (mínimo 8 caracteres)</label>
    <input id="contrasena"
           name="contrasena"
           type="password"
           minlength="8"
           required>

    <br><br>

    <label for="telefono">Teléfono</label>
    <input id="telefono" name="telefono">

    <br><br>

    <label for="rol">Tipo de cuenta</label>
    <select id="rol" name="rol" required>
        <option value="ROLE_CLIENTE">Cliente</option>
        <option value="ROLE_EMPRENDEDOR">Emprendedor</option>
    </select>

    <br><br>

    <button type="submit">Registrarme</button>
</form>

<p>
    <a href="${pageContext.request.contextPath}/auth">
        Volver al inicio de sesión
    </a>
</p>

</body>
</html>