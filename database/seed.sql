-- =========================================================
-- EmprendeLink DWF
-- Archivo: seed.sql
-- Datos iniciales de desarrollo
-- =========================================================

USE emprendelink_dwf;

-- =========================================================
-- ROLES
-- =========================================================

INSERT INTO roles (nombre)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_EMPRENDEDOR'),
    ('ROLE_CLIENTE')
    ON DUPLICATE KEY UPDATE
                         nombre = VALUES(nombre);

-- =========================================================
-- CATEGORÍAS INICIALES
-- =========================================================

INSERT INTO categorias (nombre, descripcion, activo)
VALUES
    (
        'Alimentos',
        'Productos alimenticios y bebidas ofrecidos por emprendedores.',
        TRUE
    ),
    (
        'Tecnología',
        'Productos y servicios relacionados con tecnología.',
        TRUE
    ),
    (
        'Artesanías',
        'Productos elaborados de forma artesanal.',
        TRUE
    ),
    (
        'Moda',
        'Ropa, accesorios y productos relacionados con moda.',
        TRUE
    ),
    (
        'Servicios Profesionales',
        'Servicios prestados por profesionales o emprendedores especializados.',
        TRUE
    )
    ON DUPLICATE KEY UPDATE
                         descripcion = VALUES(descripcion),
                         activo = VALUES(activo);