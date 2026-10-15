-- =====================================================================
-- EmprendeLink DWF — seed.sql
--
-- Contraseña de TODOS los usuarios demo:
-- Demo1234!
-- =====================================================================

USE emprendelink_dwf;

-- =========================================================
-- ROLES
-- =========================================================

INSERT INTO roles (nombre)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_EMPRENDEDOR'),
    ('ROLE_CLIENTE');

-- =========================================================
-- USUARIOS
-- =========================================================

INSERT INTO usuarios (
    id_rol,
    nombre,
    apellido,
    correo,
    contrasena_hash,
    telefono
)
VALUES
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

-- =========================================================
-- PLANES
-- =========================================================

INSERT INTO planes (
    nombre,
    max_productos_activos,
    tasa_comision,
    activo
)
VALUES
    ('Básico', 10, 0.0700, TRUE),
    ('Estándar', 30, 0.0500, TRUE),
    ('Premium', 60, 0.0300, TRUE),
    ('Max', 100, 0.0100, TRUE);

-- =========================================================
-- CATEGORÍAS
-- =========================================================

INSERT INTO categorias (
    nombre,
    descripcion,
    maneja_stock,
    activo
)
VALUES
    (
        'Alimentos',
        'Productos comestibles y preparados.',
        TRUE,
        TRUE
    ),
    (
        'Diseño',
        'Servicios de diseño, creatividad y branding.',
        FALSE,
        TRUE
    );

-- =========================================================
-- NEGOCIOS
-- =========================================================

INSERT INTO emprendimientos (
    id_propietario,
    id_plan,
    id_categoria,
    nombre,
    descripcion,
    contacto,
    logo_url
)
VALUES
    (
        2,
        1,
        1,
        'Panadería Ruiz',
        'Pan artesanal y repostería preparada diariamente.',
        '7000-1001',
        NULL
    ),
    (
        3,
        2,
        2,
        'Estudio Flores',
        'Diseño gráfico, identidad visual y branding.',
        '7000-1002',
        NULL
    );

-- =========================================================
-- PUBLICACIONES
-- =========================================================

INSERT INTO publicaciones (
    id_emprendimiento,
    id_categoria,
    tipo,
    nombre,
    descripcion,
    imagen_url,
    precio,
    stock
)
VALUES
    (
        1,
        1,
        'PRODUCTO',
        'Pan francés (docena)',
        'Pan fresco horneado diariamente.',
        NULL,
        2.50,
        40
    ),
    (
        1,
        1,
        'PRODUCTO',
        'Pastel de chocolate',
        'Pastel mediano para aproximadamente 8 porciones.',
        NULL,
        15.00,
        5
    ),
    (
        2,
        2,
        'SERVICIO',
        'Diseño de logo',
        'Incluye tres propuestas iniciales y ajustes.',
        NULL,
        60.00,
        0
    );

-- =========================================================
-- PEDIDOS
-- =========================================================

INSERT INTO pedidos (
    id_cliente,
    id_emprendimiento,
    id_plan_aplicado,
    nombre_plan_aplicado,
    estado,
    total,
    tasa_comision,
    monto_comision,
    observaciones,
    fecha_pedido,
    fecha_completado
)
VALUES
    (
        4,
        1,
        1,
        'Básico',
        'PENDIENTE',
        17.50,
        0.0700,
        1.23,
        'Entregar en la tarde',
        CURRENT_TIMESTAMP,
        NULL
    ),
    (
        5,
        2,
        2,
        'Estándar',
        'CONFIRMADO',
        60.00,
        0.0500,
        3.00,
        NULL,
        CURRENT_TIMESTAMP,
        NULL
    ),
    (
        5,
        1,
        1,
        'Básico',
        'COMPLETADO',
        30.00,
        0.0700,
        2.10,
        'Pedido entregado correctamente',
        DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY),
        CURRENT_TIMESTAMP
    ),
    (
        4,
        1,
        1,
        'Básico',
        'CANCELADO',
        2.50,
        0.0700,
        0.18,
        NULL,
        DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY),
        NULL
    );

-- =========================================================
-- DETALLE DE PEDIDOS
-- =========================================================

INSERT INTO detalle_pedido (
    id_pedido,
    id_publicacion,
    cantidad,
    precio_unitario,
    subtotal
)
VALUES
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
    ),
    (
        3,
        2,
        2,
        15.00,
        30.00
    ),
    (
        4,
        1,
        1,
        2.50,
        2.50
    );