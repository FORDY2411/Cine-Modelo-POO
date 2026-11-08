# AI SAFETY RULES - PROTOCOLO DE SEGURIDAD CONTRA REGRESIONES

Este documento establece directivas obligatorias para cualquier Inteligencia Artificial o desarrollador automatizado que modifique el código de este repositorio.

---

## 🛑 PRINCIPIOS INVIOLABLES DE SEGURIDAD

### 1. Principio de Preservación de la Taquilla
- La aplicación Cine es un sistema completo con múltiples módulos: **Películas (Cartelera), Salas, Funciones, Personal y Taquilla**.
- Ninguna tarea escolar o módulo nuevo puede reemplazar, sobreescribir o aislar la vista principal `cines.fxml`.
- El flujo de arranque estándar en `App.java` debe ser **`login`** (1120x720) y de ahí pasar a **`cines`**.

### 2. Prohibición de Pantallas Aisladas o Huérfanas
- Si se crea una nueva vista (ej. `nueva_actividad.fxml`), es obligatorio:
  1. Agregar un botón de navegación en la barra lateral de `cines.fxml`.
  2. Agregar en el encabezado de la nueva vista un botón visible para regresar: `⬅ Volver a Taquilla (cines.fxml)` y `🚪 Salir al Login`.
- Nunca dejar al usuario sin salida visual en una pantalla.

### 3. Notificación Previa Obligatoria de Alteraciones de Flujo
- Si por una necesidad explícita (como grabar un video demostrativo de un CRUD específico) se debe modificar el arranque en `App.java`, la IA **DEBE avisar en texto visible antes de hacer el cambio**:
  > *"⚠️ ADVERTENCIA: Se ajustará temporalmente el inicio en App.java para abrir directamente la vista [X]. Para volver a la Taquilla principal, puedes usar el botón de retorno integrado o indicarme que restaure el Login inicial."*

### 4. Gestión de Archivos e Imágenes (Actividad 3.9)
- Cualquier selección de imagen con `FileChooser` debe copiarse a una carpeta del proyecto (`imagenes/<modulo>/`) y guardarse con ruta relativa en MySQL.
- Al eliminar un registro de la base de datos, el archivo físico debe eliminarse del disco (`GestorImagenes.eliminarImagenDelProyecto`), cuidando no borrar imágenes por defecto.

### 5. Validación Continua
- No se considerará terminada ninguna tarea si se rompe alguna de las 62 pruebas unitarias automatizadas o si el proyecto no compila.

---

## 📋 CHECKLIST PRE-ENTREGA PARA LA IA
- [ ] 1. Compilación limpia (código de salida 0).
- [ ] 2. 62 pruebas unitarias pasando sin fallos.
- [ ] 3. `App.java` inicia en `login.fxml` -> `cines.fxml` (Taquilla).
- [ ] 4. Navegación bidireccional completa y botones de retorno presentes.
- [ ] 5. Sin archivos huérfanos ni rutas absolutas locales en base de datos.
