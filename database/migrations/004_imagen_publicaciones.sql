USE emprendelink_dwf;

ALTER TABLE publicaciones
    ADD COLUMN imagen_url VARCHAR(1000) NULL
    AFTER descripcion;