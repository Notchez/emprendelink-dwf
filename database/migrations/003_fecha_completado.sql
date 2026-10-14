USE emprendelink_dwf;

ALTER TABLE pedidos
    ADD COLUMN fecha_completado DATETIME NULL
    AFTER fecha_pedido;

SET SQL_SAFE_UPDATES = 0;

UPDATE pedidos
SET fecha_completado = fecha_pedido
WHERE estado = 'COMPLETADO'
  AND fecha_completado IS NULL;

SET SQL_SAFE_UPDATES = 1;