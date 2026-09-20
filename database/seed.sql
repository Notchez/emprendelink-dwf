-- =====================================================================
-- EmprendeLink DWF — seed.sql
-- Datos de prueba controlados. Ejecutar DESPUÉS de schema.sql
-- =====================================================================

USE emprendelink_dwf;

-- roles
INSERT INTO roles (nombre) VALUES
                               ('ROLE_ADMIN'),
                               ('ROLE_EMPRENDEDOR'),
                               ('ROLE_CLIENTE');

-- usuarios (un admin, dos emprendedores, dos clientes)
INSERT INTO usuarios (id_rol, nombre, apellido, correo, contrasena_hash, telefono) VALUES
                                                                                       (1, 'Ana',    'Martínez', 'admin@emprendelink.com',   'hash_admin',   '7000-0001'),
                                                                                       (2, 'Carlos', 'Ruiz',     'carlos@emprendelink.com',  'hash_carlos',  '7000-0002'),
                                                                                       (2, 'Lucía',  'Flores',   'lucia@emprendelink.com',   'hash_lucia',   '7000-0003'),
                                                                                       (3, 'David',  'Gómez',    'david@emprendelink.com',   'hash_david',   '7000-0004'),
                                                                                       (3, 'Mariana','López',    'mariana@emprendelink.com', 'hash_mariana', '7000-0005');

-- emprendimientos (propiedad de Carlos y Lucía)
INSERT INTO emprendimientos (id_propietario, nombre, descripcion, contacto) VALUES
                                                                                (2, 'Panadería Ruiz',      'Pan artesanal y repostería', 'IG: @panaderiaruiz'),
                                                                                (3, 'Estudio Flores',      'Diseño gráfico y branding',  'IG: @estudioflores');

-- categorias
INSERT INTO categorias (nombre, descripcion) VALUES
                                                 ('Alimentos',  'Productos comestibles'),
                                                 ('Diseño',     'Servicios de diseño y creatividad');

-- publicaciones (productos y servicios)
INSERT INTO publicaciones (id_emprendimiento, id_categoria, tipo, nombre, descripcion, precio, stock) VALUES
                                                                                                          (1, 1, 'PRODUCTO', 'Pan francés (docena)',      'Pan fresco horneado diario', 2.50, 40),
                                                                                                          (1, 1, 'PRODUCTO', 'Pastel de chocolate',       'Pastel mediano, 8 porciones', 15.00, 5),
                                                                                                          (2, 2, 'SERVICIO', 'Diseño de logo',            'Incluye 3 propuestas y ajustes', 60.00, 0);

-- pedidos (David le compra a Carlos; Mariana le compra a Lucía)
INSERT INTO pedidos (id_cliente, id_emprendimiento, estado, total, observaciones) VALUES
                                                                                      (4, 1, 'PENDIENTE',  17.50, 'Entregar en la tarde'),
                                                                                      (5, 2, 'CONFIRMADO', 60.00, NULL);

-- detalle_pedido
INSERT INTO detalle_pedido (id_pedido, id_publicacion, cantidad, precio_unitario, subtotal) VALUES
                                                                                                (1, 1, 1, 2.50, 2.50),
                                                                                                (1, 2, 1, 15.00, 15.00),
                                                                                                (2, 3, 1, 60.00, 60.00);