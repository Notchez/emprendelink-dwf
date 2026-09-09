-- =========================================================
-- EmprendeLink DWF
-- Base de datos: emprendelink_dwf
-- Archivo: schema.sql
-- =========================================================

CREATE DATABASE IF NOT EXISTS emprendelink_dwf
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE emprendelink_dwf;

-- =========================================================
-- TABLA: roles
-- =========================================================

CREATE TABLE IF NOT EXISTS roles (
                                     id_rol INT UNSIGNED AUTO_INCREMENT,
                                     nombre VARCHAR(50) NOT NULL,

    CONSTRAINT pk_roles
    PRIMARY KEY (id_rol),

    CONSTRAINT uq_roles_nombre
    UNIQUE (nombre)
    );

-- =========================================================
-- TABLA: usuarios
-- =========================================================

CREATE TABLE IF NOT EXISTS usuarios (
                                        id_usuario INT UNSIGNED AUTO_INCREMENT,
                                        id_rol INT UNSIGNED NOT NULL,
                                        nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    contrasena_hash VARCHAR(255) NOT NULL,
    telefono VARCHAR(25),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_usuarios
    PRIMARY KEY (id_usuario),

    CONSTRAINT uq_usuarios_correo
    UNIQUE (correo),

    CONSTRAINT fk_usuarios_roles
    FOREIGN KEY (id_rol)
    REFERENCES roles(id_rol)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT
    );

-- =========================================================
-- TABLA: emprendimientos
-- =========================================================

CREATE TABLE IF NOT EXISTS emprendimientos (
                                               id_emprendimiento INT UNSIGNED AUTO_INCREMENT,
                                               id_propietario INT UNSIGNED NOT NULL,
                                               nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    contacto VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_emprendimientos
    PRIMARY KEY (id_emprendimiento),

    CONSTRAINT fk_emprendimientos_propietario
    FOREIGN KEY (id_propietario)
    REFERENCES usuarios(id_usuario)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT
    );

-- =========================================================
-- TABLA: categorias
-- =========================================================

CREATE TABLE IF NOT EXISTS categorias (
                                          id_categoria INT UNSIGNED AUTO_INCREMENT,
                                          nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_categorias
    PRIMARY KEY (id_categoria),

    CONSTRAINT uq_categorias_nombre
    UNIQUE (nombre)
    );

-- =========================================================
-- TABLA: publicaciones
-- =========================================================

CREATE TABLE IF NOT EXISTS publicaciones (
                                             id_publicacion INT UNSIGNED AUTO_INCREMENT,
                                             id_emprendimiento INT UNSIGNED NOT NULL,
                                             id_categoria INT UNSIGNED NOT NULL,
                                             tipo VARCHAR(20) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT UNSIGNED,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_publicacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_publicaciones
    PRIMARY KEY (id_publicacion),

    CONSTRAINT fk_publicaciones_emprendimiento
    FOREIGN KEY (id_emprendimiento)
    REFERENCES emprendimientos(id_emprendimiento)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT,

    CONSTRAINT fk_publicaciones_categoria
    FOREIGN KEY (id_categoria)
    REFERENCES categorias(id_categoria)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT,

    CONSTRAINT chk_publicaciones_tipo
    CHECK (tipo IN ('PRODUCTO', 'SERVICIO')),

    CONSTRAINT chk_publicaciones_precio
    CHECK (precio >= 0),

    CONSTRAINT chk_publicaciones_stock
    CHECK (stock IS NULL OR stock >= 0)
    );

-- =========================================================
-- TABLA: pedidos
-- =========================================================

CREATE TABLE IF NOT EXISTS pedidos (
                                       id_pedido INT UNSIGNED AUTO_INCREMENT,
                                       id_cliente INT UNSIGNED NOT NULL,
                                       id_emprendimiento INT UNSIGNED NOT NULL,
                                       estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    total DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    observaciones TEXT,
    fecha_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_pedidos
    PRIMARY KEY (id_pedido),

    CONSTRAINT fk_pedidos_cliente
    FOREIGN KEY (id_cliente)
    REFERENCES usuarios(id_usuario)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT,

    CONSTRAINT fk_pedidos_emprendimiento
    FOREIGN KEY (id_emprendimiento)
    REFERENCES emprendimientos(id_emprendimiento)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT,

    CONSTRAINT chk_pedidos_estado
    CHECK (
              estado IN (
              'PENDIENTE',
              'CONFIRMADO',
              'EN_PROCESO',
              'COMPLETADO',
              'CANCELADO'
                        )
    ),

    CONSTRAINT chk_pedidos_total
    CHECK (total >= 0)
    );

-- =========================================================
-- TABLA: detalle_pedido
-- =========================================================

CREATE TABLE IF NOT EXISTS detalle_pedido (
                                              id_detalle_pedido INT UNSIGNED AUTO_INCREMENT,
                                              id_pedido INT UNSIGNED NOT NULL,
                                              id_publicacion INT UNSIGNED NOT NULL,
                                              cantidad INT UNSIGNED NOT NULL,
                                              precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,

    CONSTRAINT pk_detalle_pedido
    PRIMARY KEY (id_detalle_pedido),

    CONSTRAINT fk_detalle_pedido_pedido
    FOREIGN KEY (id_pedido)
    REFERENCES pedidos(id_pedido)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT,

    CONSTRAINT fk_detalle_pedido_publicacion
    FOREIGN KEY (id_publicacion)
    REFERENCES publicaciones(id_publicacion)
    ON UPDATE RESTRICT
    ON DELETE RESTRICT,

    CONSTRAINT chk_detalle_pedido_cantidad
    CHECK (cantidad > 0),

    CONSTRAINT chk_detalle_pedido_precio
    CHECK (precio_unitario >= 0),

    CONSTRAINT chk_detalle_pedido_subtotal
    CHECK (subtotal >= 0)
    );

-- =========================================================
-- ÍNDICES
-- =========================================================

CREATE INDEX idx_usuarios_id_rol
    ON usuarios(id_rol);

CREATE INDEX idx_emprendimientos_propietario
    ON emprendimientos(id_propietario);

CREATE INDEX idx_publicaciones_emprendimiento
    ON publicaciones(id_emprendimiento);

CREATE INDEX idx_publicaciones_categoria
    ON publicaciones(id_categoria);

CREATE INDEX idx_publicaciones_tipo
    ON publicaciones(tipo);

CREATE INDEX idx_publicaciones_activo
    ON publicaciones(activo);

CREATE INDEX idx_pedidos_cliente
    ON pedidos(id_cliente);

CREATE INDEX idx_pedidos_emprendimiento
    ON pedidos(id_emprendimiento);

CREATE INDEX idx_pedidos_estado
    ON pedidos(estado);

CREATE INDEX idx_detalle_pedido_pedido
    ON detalle_pedido(id_pedido);

CREATE INDEX idx_detalle_pedido_publicacion
    ON detalle_pedido(id_publicacion);