-- Crear la base de datos
CREATE DATABASE BDUniversidad;

-- Usar la base de datos recién creada
USE BDUniversidad;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id_usuario INT PRIMARY KEY IDENTITY(1,1),
    nombre_usuario VARCHAR(50) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(10) CHECK (rol IN ('docente', 'admin')) NOT NULL,
    fecha_creacion DATETIME DEFAULT GETDATE(),
    fecha_actualizacion DATETIME NULL
);

-- Tabla de programas académicos
CREATE TABLE programas_academicos (
    id_programa INT PRIMARY KEY IDENTITY(1,1),
    nombre_programa VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

-- Tabla de estudiantes
CREATE TABLE estudiantes (
    id_estudiante INT PRIMARY KEY IDENTITY(1,1),
    carnet VARCHAR(20) UNIQUE NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    carrera VARCHAR(100),
    fecha_ingreso DATETIME,
    promedio_notas DECIMAL(5,2) DEFAULT 0.00,
    modalidad_estudio VARCHAR(50),
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE SET NULL
);

-- Tabla de docentes
CREATE TABLE docentes (
    id_docente INT PRIMARY KEY IDENTITY(1,1),
    id_usuario INT NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    titulo VARCHAR(100),
    experiencia_anios DECIMAL(5,2),
    fecha_contratacion DATETIME,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- Tabla de materias
CREATE TABLE materias (
    id_materia INT PRIMARY KEY IDENTITY(1,1),
    nombre_materia VARCHAR(100) NOT NULL,
    codigo_materia VARCHAR(20) UNIQUE NOT NULL,
    uv INT NOT NULL,
    id_docente INT,
    FOREIGN KEY (id_docente) REFERENCES docentes(id_docente) ON DELETE SET NULL
);

-- Tabla de aulas
CREATE TABLE aulas (
    id_aula INT PRIMARY KEY IDENTITY(1,1),
    nombre_aula VARCHAR(20) NOT NULL UNIQUE,
    capacidad INT NOT NULL
);

-- Tabla de ciclos
CREATE TABLE ciclos (
    id_ciclo INT PRIMARY KEY IDENTITY(1,1),
    nombre_ciclo VARCHAR(10) UNIQUE NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE
);

-- Tabla de horarios
CREATE TABLE horarios (
    id_horario INT PRIMARY KEY IDENTITY(1,1),
    nombre_horario VARCHAR(100) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    dias VARCHAR(100)
);

-- Tabla intermedia para asignar horarios a estudiantes
CREATE TABLE estudiante_horarios (
    id_estudiante INT NOT NULL,
    id_horario INT NOT NULL,
    PRIMARY KEY (id_estudiante, id_horario),
    FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id_estudiante) ON DELETE CASCADE,
    FOREIGN KEY (id_horario) REFERENCES horarios(id_horario) ON DELETE CASCADE
);

--Se modifico la tabla ciclos
ALTER TABLE ciclos
ALTER COLUMN nombre_ciclo VARCHAR(50); -- o más si necesitas


