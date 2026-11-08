-- =============================================================================
-- SISTEMA DE GESTIÓN DE CINE Y TAQUILLA (JavaFX + MySQL / MariaDB XAMPP)
-- Materia: Programación Orientada a Objetos - DSM 3°B TM
-- Actividad 3.5: Base de Datos
-- Autor: Leonardo David Estrada Amador
-- =============================================================================

-- 1. CREACIÓN DE LA BASE DE DATOS
DROP DATABASE IF EXISTS cine_db;
CREATE DATABASE cine_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cine_db;

-- =============================================================================
-- 2. CREACIÓN DE TABLAS INDEPENDIENTES (Catálogos y Usuarios)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabla: administradores (Personal Administrativo: Admin, Gerente, Supervisor)
-- Cumple con el esquema de 11 columnas de la Actividad 3.2
-- -----------------------------------------------------------------------------
CREATE TABLE administradores (
    id INT AUTO_INCREMENT,
    usuario VARCHAR(50) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol ENUM('Administrador', 'Gerente', 'Supervisor') NOT NULL,
    ruta_imagen VARCHAR(255) DEFAULT 'default_user.png',
    estado ENUM('Activo', 'Inactivo', 'Bloqueado') NOT NULL DEFAULT 'Activo',
    creado DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editado DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    
    -- Llaves y Restricciones
    CONSTRAINT pk_administradores PRIMARY KEY (id),
    CONSTRAINT uq_administradores_usuario UNIQUE (usuario),
    CONSTRAINT uq_administradores_correo UNIQUE (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabla: clientes (Clientes Generales, Cliente Fan y Cliente MegaFan)
-- -----------------------------------------------------------------------------
CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    tipo_cliente ENUM('General', 'Fan', 'MegaFan') NOT NULL DEFAULT 'General',
    puntos INT NOT NULL DEFAULT 0,
    porcentaje_descuento DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    pase_prioritario BOOLEAN NOT NULL DEFAULT FALSE,
    estado ENUM('Activo', 'Inactivo', 'Suspendido') NOT NULL DEFAULT 'Activo',
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Llaves y Restricciones
    CONSTRAINT pk_clientes PRIMARY KEY (id_cliente),
    CONSTRAINT uq_clientes_correo UNIQUE (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabla: peliculas (Catálogo de Películas)
-- -----------------------------------------------------------------------------
CREATE TABLE peliculas (
    id_pelicula INT AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    duracion_min INT NOT NULL,
    clasificacion VARCHAR(10) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    estado ENUM('En Cartelera', 'Próximamente', 'Retirada') NOT NULL DEFAULT 'En Cartelera',
    ruta_imagen VARCHAR(255) NULL,
    
    -- Llaves y Restricciones
    CONSTRAINT pk_peliculas PRIMARY KEY (id_pelicula),
    CONSTRAINT chk_pelicula_duracion CHECK (duracion_min > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Tabla: salas (Salas del Complejo Cinematográfico)
-- -----------------------------------------------------------------------------
CREATE TABLE salas (
    id_sala INT AUTO_INCREMENT,
    numero INT NOT NULL,
    capacidad INT NOT NULL,
    tecnologia VARCHAR(30) NOT NULL DEFAULT '2D',
    estado ENUM('Disponible', 'Mantenimiento', 'Limpieza') NOT NULL DEFAULT 'Disponible',
    
    -- Llaves y Restricciones
    CONSTRAINT pk_salas PRIMARY KEY (id_sala),
    CONSTRAINT uq_salas_numero UNIQUE (numero),
    CONSTRAINT chk_sala_capacidad CHECK (capacidad > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- 3. CREACIÓN DE TABLAS DEPENDIENTES (Operaciones, Funciones y Boletos)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabla: funciones (Programación de Película en una Sala específica)
-- -----------------------------------------------------------------------------
CREATE TABLE funciones (
    id_funcion INT AUTO_INCREMENT,
    id_pelicula INT NOT NULL,
    id_sala INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    precio_base DECIMAL(10,2) NOT NULL,
    estado ENUM('Programada', 'En Proyección', 'Finalizada', 'Cancelada', 'Agotada') NOT NULL DEFAULT 'Programada',
    
    -- Llaves Primarias y Foráneas
    CONSTRAINT pk_funciones PRIMARY KEY (id_funcion),
    CONSTRAINT fk_funciones_peliculas FOREIGN KEY (id_pelicula) 
        REFERENCES peliculas (id_pelicula) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
    CONSTRAINT fk_funciones_salas FOREIGN KEY (id_sala) 
        REFERENCES salas (id_sala) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
    CONSTRAINT chk_precio_base CHECK (precio_base > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para optimizar búsquedas por película, sala y horario
CREATE INDEX idx_funciones_pelicula ON funciones (id_pelicula);
CREATE INDEX idx_funciones_sala ON funciones (id_sala);
CREATE INDEX idx_funciones_fecha ON funciones (fecha_hora);

-- -----------------------------------------------------------------------------
-- Tabla: boletos (Venta y Emisión de Entradas)
-- -----------------------------------------------------------------------------
CREATE TABLE boletos (
    id_boleto INT AUTO_INCREMENT,
    id_funcion INT NOT NULL,
    id_cliente INT NULL,
    id_administrador INT NULL,
    numero_asiento VARCHAR(10) NOT NULL,
    precio_final DECIMAL(10,2) NOT NULL,
    metodo_pago ENUM('Efectivo', 'Tarjeta', 'Puntos', 'Transferencia') NOT NULL DEFAULT 'Efectivo',
    estado ENUM('Emitido', 'Usado', 'Cancelado', 'Reembolsado') NOT NULL DEFAULT 'Emitido',
    fecha_emision DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Llaves Primarias y Foráneas
    CONSTRAINT pk_boletos PRIMARY KEY (id_boleto),
    CONSTRAINT fk_boletos_funciones FOREIGN KEY (id_funcion) 
        REFERENCES funciones (id_funcion) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
    CONSTRAINT fk_boletos_clientes FOREIGN KEY (id_cliente) 
        REFERENCES clientes (id_cliente) 
        ON UPDATE CASCADE 
        ON DELETE SET NULL,
    CONSTRAINT fk_boletos_administradores FOREIGN KEY (id_administrador) 
        REFERENCES administradores (id) 
        ON UPDATE CASCADE 
        ON DELETE SET NULL,
    CONSTRAINT uq_asiento_funcion UNIQUE (id_funcion, numero_asiento),
    CONSTRAINT chk_precio_final CHECK (precio_final >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para transacciones y reportes de taquilla
CREATE INDEX idx_boletos_funcion ON boletos (id_funcion);
CREATE INDEX idx_boletos_cliente ON boletos (id_cliente);
CREATE INDEX idx_boletos_administrador ON boletos (id_administrador);
CREATE INDEX idx_boletos_fecha ON boletos (fecha_emision);

-- =============================================================================
-- 4. INSERCIÓN DE DATOS INICIALES DE PRUEBA
-- =============================================================================

-- Administradores / Personal de Taquilla
INSERT INTO administradores (usuario, nombres, apellidos, correo, contrasena, rol, ruta_imagen, estado) VALUES
('admin_leo', 'Leonardo David', 'Estrada Amador', 'leo.estrada@cine.com', 'admin123', 'Administrador', 'default_user.png', 'Activo'),
('gerente_ana', 'Ana Gabriela', 'Rojas Soto', 'ana.rojas@cine.com', 'gerente123', 'Gerente', 'default_user.png', 'Activo'),
('super_carlos', 'Carlos Eduardo', 'Mendoza Peña', 'carlos.m@cine.com', 'super123', 'Supervisor', 'default_user.png', 'Activo');

-- Clientes
INSERT INTO clientes (nombres, apellidos, correo, contrasena, tipo_cliente, puntos, porcentaje_descuento, pase_prioritario, estado) VALUES
('Juan Pablo', 'Gómez Luna', 'juan.gomez@gmail.com', 'fan123', 'Fan', 120, 10.00, FALSE, 'Activo'),
('Valeria', 'Navarro Torres', 'valeria.navarro@gmail.com', 'vip123', 'MegaFan', 450, 20.00, TRUE, 'Activo'),
('Roberto', 'Hernández Paz', 'roberto.hdez@outlook.com', 'general123', 'General', 0, 0.00, FALSE, 'Activo');

-- Salas
INSERT INTO salas (numero, capacidad, tecnologia, estado) VALUES
(1, 120, '2D Estándar', 'Disponible'),
(2, 100, '3D Digital', 'Disponible'),
(3, 80, 'IMAX Laser', 'Disponible'),
(4, 50, '4DX Motion', 'Mantenimiento');

-- Películas
INSERT INTO peliculas (titulo, duracion_min, clasificacion, genero, estado, ruta_imagen) VALUES
('Avatar: El Sentido del Agua', 192, 'B', 'Ciencia Ficción', 'En Cartelera', 'avatar.jpg'),
('Super Mario Bros. La Película', 92, 'AA', 'Animación / Aventura', 'En Cartelera', 'mario.jpg'),
('Oppenheimer', 180, 'B15', 'Drama Histórico', 'En Cartelera', 'oppenheimer.jpg'),
('Dune: Parte 2', 166, 'B', 'Ciencia Ficción', 'Próximamente', 'dune2.jpg');

-- Funciones
INSERT INTO funciones (id_pelicula, id_sala, fecha_hora, precio_base, estado) VALUES
(1, 3, '2026-10-28 16:00:00', 140.00, 'Programada'),
(1, 3, '2026-10-28 20:00:00', 140.00, 'Programada'),
(2, 1, '2026-10-28 15:30:00', 80.00, 'Programada'),
(2, 2, '2026-10-28 18:00:00', 95.00, 'Programada'),
(3, 1, '2026-10-28 21:00:00', 85.00, 'Programada');

-- Boletos emitidos
INSERT INTO boletos (id_funcion, id_cliente, id_administrador, numero_asiento, precio_final, metodo_pago, estado) VALUES
(1, 2, 2, 'F10', 112.00, 'Tarjeta', 'Emitido'),
(1, 2, 2, 'F11', 112.00, 'Tarjeta', 'Emitido'),
(3, 1, 3, 'C05', 72.00, 'Efectivo', 'Emitido'),
(4, 3, 2, 'D08', 95.00, 'Transferencia', 'Emitido');
