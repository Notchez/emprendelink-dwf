USE emprendelink_dwf;

-- =========================================================
-- PLANES
-- =========================================================

CREATE TABLE IF NOT EXISTS planes (
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

INSERT INTO planes (
    nombre,
    max_productos_activos,
    tasa_comision,
    activo
) VALUES
      ('Básico', 10, 0.0700, TRUE),
      ('Estándar', 30, 0.0500, TRUE),
      ('Pro', 60, 0.0300, TRUE),
      ('Premium', 100, 0.0100, TRUE);

-- =========================================================
-- EMPRENDIMIENTOS -> PLAN
-- =========================================================

ALTER TABLE emprendimientos
    ADD COLUMN id_plan INT NULL
    AFTER id_propietario;

-- Los negocios existentes arrancan en Básico.

UPDATE emprendimientos
SET id_plan = (
    SELECT id_plan
    FROM planes
    WHERE nombre = 'Básico'
    LIMIT 1
    )
WHERE id_plan IS NULL;

ALTER TABLE emprendimientos
    MODIFY COLUMN id_plan INT NOT NULL;

ALTER TABLE emprendimientos
    ADD CONSTRAINT fk_emprendimientos_plan
        FOREIGN KEY (id_plan)
            REFERENCES planes(id_plan);

-- =========================================================
-- PEDIDOS -> COMISIÓN HISTÓRICA
-- =========================================================

ALTER TABLE pedidos
    ADD COLUMN tasa_comision DECIMAL(5,4) NULL
    AFTER total;

ALTER TABLE pedidos
    ADD COLUMN monto_comision DECIMAL(10,2) NULL
    AFTER tasa_comision;

ALTER TABLE pedidos
    ADD CONSTRAINT chk_pedidos_tasa_comision
        CHECK (
            tasa_comision IS NULL
                OR (
                tasa_comision >= 0
                    AND tasa_comision <= 1
                )
            );

ALTER TABLE pedidos
    ADD CONSTRAINT chk_pedidos_monto_comision
        CHECK (
            monto_comision IS NULL
                OR monto_comision >= 0
            );

-- =========================================================
-- BACKFILL
-- Pedidos ya completados antes de crear este módulo.
-- Como todavía no existía snapshot histórico, se utiliza
-- el plan actual del negocio para inicializarlos.
-- =========================================================

UPDATE pedidos pedido
    JOIN emprendimientos negocio
ON negocio.id_emprendimiento =
    pedido.id_emprendimiento
    JOIN planes plan
    ON plan.id_plan =
    negocio.id_plan
    SET pedido.tasa_comision =
        plan.tasa_comision,
        pedido.monto_comision =
        ROUND(
        pedido.total
        * plan.tasa_comision,
        2
        )
WHERE pedido.estado = 'COMPLETADO'
  AND pedido.monto_comision IS NULL;