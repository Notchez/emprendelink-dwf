DROP DATABASE IF EXISTS emprendelink_dwf;

CREATE DATABASE emprendelink_dwf
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE emprendelink_dwf;

-- =========================================================
-- ROLES
-- =========================================================

CREATE TABLE roles (
                       id_rol INT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(30) NOT NULL UNIQUE
);

-- =========================================================
-- USUARIOS
-- =========================================================

CREATE TABLE usuarios (
                          id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                          id_rol INT NOT NULL,
                          nombre VARCHAR(80) NOT NULL,
                          apellido VARCHAR(80) NOT NULL,
                          correo VARCHAR(120) NOT NULL UNIQUE,
                          contrasena_hash VARCHAR(255) NOT NULL,
                          telefono VARCHAR(20),
                          direccion VARCHAR(255),
                          activo BOOLEAN NOT NULL DEFAULT TRUE,
                          fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_usuarios_rol
                              FOREIGN KEY (id_rol)
                                  REFERENCES roles(id_rol)
);

-- =========================================================
-- PLANES
-- =========================================================

CREATE TABLE planes (
                        id_plan INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(80) NOT NULL UNIQUE,
                        max_productos_activos INT NOT NULL,
                        tasa_comision DECIMAL(5,4) NOT NULL,
                        activo BOOLEAN NOT NULL DEFAULT TRUE,

                        CONSTRAINT chk_planes_max_productos
                            CHECK (max_productos_activos > 0),

                        CONSTRAINT chk_planes_tasa
                            CHECK (
                                tasa_comision >= 0
                                    AND tasa_comision <= 1
                                )
);

-- =========================================================
-- CATEGORÍAS
-- =========================================================

CREATE TABLE categorias (
                            id_categoria INT AUTO_INCREMENT PRIMARY KEY,
                            nombre VARCHAR(80) NOT NULL UNIQUE,
                            descripcion TEXT,
                            maneja_stock BOOLEAN NOT NULL DEFAULT TRUE,
                            activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- =========================================================
-- EMPRENDIMIENTOS
-- =========================================================

CREATE TABLE emprendimientos (
                                 id_emprendimiento INT AUTO_INCREMENT PRIMARY KEY,
                                 id_propietario INT NOT NULL,
                                 id_plan INT NOT NULL,
                                 id_categoria INT NOT NULL,
                                 nombre VARCHAR(120) NOT NULL,
                                 descripcion TEXT,
                                 contacto VARCHAR(120),
                                 logo_url VARCHAR(1000),
                                 activo BOOLEAN NOT NULL DEFAULT TRUE,
                                 fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT uq_emprendimientos_propietario
                                     UNIQUE (id_propietario),

                                 CONSTRAINT fk_emprendimientos_propietario
                                     FOREIGN KEY (id_propietario)
                                         REFERENCES usuarios(id_usuario),

                                 CONSTRAINT fk_emprendimientos_plan
                                     FOREIGN KEY (id_plan)
                                         REFERENCES planes(id_plan),

                                 CONSTRAINT fk_emprendimientos_categoria
                                     FOREIGN KEY (id_categoria)
                                         REFERENCES categorias(id_categoria)
);

-- =========================================================
-- PUBLICACIONES
-- =========================================================

CREATE TABLE publicaciones (
                               id_publicacion INT AUTO_INCREMENT PRIMARY KEY,
                               id_emprendimiento INT NOT NULL,
                               id_categoria INT NOT NULL,

                               tipo ENUM(
        'PRODUCTO',
        'SERVICIO'
    ) NOT NULL,

                               nombre VARCHAR(150) NOT NULL,
                               descripcion TEXT,
                               imagen_url VARCHAR(1000),
                               precio DECIMAL(10,2) NOT NULL,
                               stock INT NOT NULL DEFAULT 0,
                               activo BOOLEAN NOT NULL DEFAULT TRUE,
                               fecha_publicacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_publicaciones_emprendimiento
                                   FOREIGN KEY (id_emprendimiento)
                                       REFERENCES emprendimientos(id_emprendimiento),

                               CONSTRAINT fk_publicaciones_categoria
                                   FOREIGN KEY (id_categoria)
                                       REFERENCES categorias(id_categoria),

                               CONSTRAINT chk_publicaciones_precio
                                   CHECK (precio >= 0),

                               CONSTRAINT chk_publicaciones_stock
                                   CHECK (stock >= 0)
);

-- =========================================================
-- PEDIDOS
-- =========================================================

CREATE TABLE pedidos (
                         id_pedido INT AUTO_INCREMENT PRIMARY KEY,
                         id_cliente INT NOT NULL,
                         id_emprendimiento INT NOT NULL,

                         id_plan_aplicado INT NOT NULL,
                         nombre_plan_aplicado VARCHAR(80) NOT NULL,

                         estado ENUM(
        'PENDIENTE',
        'CONFIRMADO',
        'EN_PROCESO',
        'COMPLETADO',
        'CANCELADO'
    ) NOT NULL DEFAULT 'PENDIENTE',

                         total DECIMAL(10,2) NOT NULL DEFAULT 0,

                         tasa_comision DECIMAL(5,4) NOT NULL,
                         monto_comision DECIMAL(10,2) NOT NULL,

                         direccion_entrega VARCHAR(255),
                         observaciones TEXT,

                         fecha_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         fecha_completado DATETIME NULL,

                         CONSTRAINT fk_pedidos_cliente
                             FOREIGN KEY (id_cliente)
                                 REFERENCES usuarios(id_usuario),

                         CONSTRAINT fk_pedidos_emprendimiento
                             FOREIGN KEY (id_emprendimiento)
                                 REFERENCES emprendimientos(id_emprendimiento),

                         CONSTRAINT fk_pedidos_plan_aplicado
                             FOREIGN KEY (id_plan_aplicado)
                                 REFERENCES planes(id_plan),

                         CONSTRAINT chk_pedidos_total
                             CHECK (total >= 0),

                         CONSTRAINT chk_pedidos_tasa
                             CHECK (
                                 tasa_comision >= 0
                                     AND tasa_comision <= 1
                                 ),

                         CONSTRAINT chk_pedidos_comision
                             CHECK (monto_comision >= 0)
);

-- =========================================================
-- DETALLE DE PEDIDO
-- =========================================================

CREATE TABLE detalle_pedido (
                                id_detalle_pedido INT AUTO_INCREMENT PRIMARY KEY,
                                id_pedido INT NOT NULL,
                                id_publicacion INT NOT NULL,
                                cantidad INT NOT NULL,
                                precio_unitario DECIMAL(10,2) NOT NULL,
                                subtotal DECIMAL(10,2) NOT NULL,

                                CONSTRAINT fk_detalle_pedido
                                    FOREIGN KEY (id_pedido)
                                        REFERENCES pedidos(id_pedido)
                                        ON DELETE CASCADE,

                                CONSTRAINT fk_detalle_publicacion
                                    FOREIGN KEY (id_publicacion)
                                        REFERENCES publicaciones(id_publicacion),

                                CONSTRAINT chk_detalle_cantidad
                                    CHECK (cantidad > 0),

                                CONSTRAINT chk_detalle_precio
                                    CHECK (precio_unitario >= 0),

                                CONSTRAINT chk_detalle_subtotal
                                    CHECK (subtotal >= 0)
);