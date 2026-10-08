-- =====================================================================
-- EmprendeLink DWF — seed.sql
-- Datos de prueba controlados.
-- Ejecutar DESPUÉS de schema.sql.
--
-- Contraseña de TODOS los usuarios de prueba:
-- Demo1234!
-- =====================================================================

USE emprendelink_dwf;

-- roles
INSERT INTO roles (nombre) VALUES
                               ('ROLE_ADMIN'),
                               ('ROLE_EMPRENDEDOR'),
                               ('ROLE_CLIENTE');

-- usuarios
INSERT INTO usuarios (
    id_rol,
    nombre,
    apellido,
    correo,
    contrasena_hash,
    telefono
) VALUES
      (
          1,
          'Ana',
          'Martínez',
          'admin@emprendelink.com',
          'pbkdf2_sha256$210000$2UIPWJ23PkRwzgQpI2V75A==$snEd3L3mu26O2YpvU1QxLWvb3Ht7ngHXLssXWCBKXtQ=',
          '7000-0001'
      ),
      (
          2,
          'Carlos',
          'Ruiz',
          'carlos@emprendelink.com',
          'pbkdf2_sha256$210000$n+AJaAv1sN4RmUJWpZa9cg==$XShbLlttnkcUfeM0ye8x4Wo7HQnyGHAQcKJW2N/jN0Q=',
          '7000-0002'
      ),
      (
          2,
          'Lucía',
          'Flores',
          'lucia@emprendelink.com',
          'pbkdf2_sha256$210000$RKT87ZpFCq8KKND5i1du7A==$FjIPbDOjTwfiN+otzBvf/Mi6ji4P7D9HYghYVCDObn0=',
          '7000-0003'
      ),
      (
          3,
          'David',
          'Gómez',
          'david@emprendelink.com',
          'pbkdf2_sha256$210000$xRgVTE7jJ2sAyYch5/pa/A==$xAYBbMctyD4tOmK6JLpj5DoiMu51TMEnxyvLAQ/aTJw=',
          '7000-0004'
      ),
      (
          3,
          'Mariana',
          'López',
          'mariana@emprendelink.com',
          'pbkdf2_sha256$210000$0BfQnGVUnCbDjy/FWFpviw==$ciimcEPFerzOf3O4JZdxMzJ+HKdFpqAMYF3wl/VMtwo=',
          '7000-0005'
      );

-- emprendimientos
INSERT INTO emprendimientos (
    id_propietario,
    nombre,
    descripcion,
    contacto
) VALUES
      (
          2,
          'Panadería Ruiz',
          'Pan artesanal y repostería',
          'IG: @panaderiaruiz'
      ),
      (
          3,
          'Estudio Flores',
          'Diseño gráfico y branding',
          'IG: @estudioflores'
      );

-- categorias
INSERT INTO categorias (
    nombre,
    descripcion
) VALUES
      (
          'Alimentos',
          'Productos comestibles'
      ),
      (
          'Diseño',
          'Servicios de diseño y creatividad'
      );

-- publicaciones
INSERT INTO publicaciones (
    id_emprendimiento,
    id_categoria,
    tipo,
    nombre,
    descripcion,
    precio,
    stock
) VALUES
      (
          1,
          1,
          'PRODUCTO',
          'Pan francés (docena)',
          'Pan fresco horneado diario',
          2.50,
          40
      ),
      (
          1,
          1,
          'PRODUCTO',
          'Pastel de chocolate',
          'Pastel mediano, 8 porciones',
          15.00,
          5
      ),
      (
          2,
          2,
          'SERVICIO',
          'Diseño de logo',
          'Incluye 3 propuestas y ajustes',
          60.00,
          0
      );

-- pedidos
INSERT INTO pedidos (
    id_cliente,
    id_emprendimiento,
    estado,
    total,
    observaciones
) VALUES
      (
          4,
          1,
          'PENDIENTE',
          17.50,
          'Entregar en la tarde'
      ),
      (
          5,
          2,
          'CONFIRMADO',
          60.00,
          NULL
      );

-- detalle de pedidos
INSERT INTO detalle_pedido (
    id_pedido,
    id_publicacion,
    cantidad,
    precio_unitario,
    subtotal
) VALUES
      (
          1,
          1,
          1,
          2.50,
          2.50
      ),
      (
          1,
          2,
          1,
          15.00,
          15.00
      ),
      (
          2,
          3,
          1,
          60.00,
          60.00
      );