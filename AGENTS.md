# AGENTS.md - Proyecto Cine (JavaFX + Maven + MySQL)

Este archivo guía a cualquier IA (Antigravity, Cursor, Copilot, ChatGPT, Claude) que trabaje en este repositorio.
Cualquier asistente de IA DEBE leer y obedecer estrictamente estas reglas antes de proponer o aplicar cambios.

---

## 1. Qué es este proyecto
Aplicación de escritorio para la gestión integral de un complejo de cine y taquillas.
- Lenguaje: Java 11
- UI: JavaFX 13 (FXML)
- Base de datos: MySQL / MariaDB (`cine_db` en localhost:3306) con JDBC
- Build: Maven (`pom.xml` en la raíz)
- Punto de entrada: `com.mycompany.cine.App` (extiende `javafx.application.Application`)
- Flujo inicial: Inicia en `login.fxml` (1120x720). Al pulsar "Entrar", abre la **Taquilla Principal** (`cines.fxml`).

---

## 2. Estructura de Paquetes y Vistas
```
src/main/java/com/mycompany/cine/
  App.java                              -> Punto de entrada JavaFX
  module-info.java                      -> Configuración modular (requires java.sql, javafx, etc.)
  controladores/
    LoginController.java                -> Controlador de login.fxml
    CinesController.java                -> Controlador de cines.fxml (Taquilla, Cartelera, Salas, Funciones, Personal)
    AdministradoresController.java      -> Controlador de administradores.fxml (Actividad 3.2 - memoria)
    AdministradorController.java        -> Controlador de administrador.fxml (Actividad 3.8/3.9 - DAO MySQL)
  modelo/
    Persona.java                        -> Base abstracta de todos los usuarios
    Administrador.java                  -> Entidad que mapea con tabla MySQL 'administradores'
    Gerente.java, Supervisor.java       -> Personal operativo
    Pelicula.java, Sala.java, Funcion.java, Boleto.java -> Entidades de Cartelera y Taquilla
    ClienteFan.java, ClienteMegaFan.java
    ERoles.java, EEstados.java          -> Enumeradores de base de datos
  daos/
    ConexionBD.java                     -> Conexión JDBC Singleton con cine_db
    AdministradorDAO.java               -> DAO con CRUD completo y contrato Listable<Administrador>
    GestorImagenes.java                 -> Copia física a imagenes/ y borrado de disco (Actividad 3.9)
  persistencia/
    Listable.java                       -> Interfaz base de persistencia (NO crear interfaces nuevas)
    CineArrayList.java                  -> Implementación en memoria
src/main/resources/com/mycompany/cine/
  login.fxml                            -> Pantalla de acceso
  cines.fxml                            -> Pantalla principal de Taquilla y módulos
  administradores.fxml                  -> Pantalla de validación en memoria (Act. 3.2)
  administrador.fxml                    -> Pantalla de gestión DAO en MySQL (Act. 3.8 / 3.9)
src/test/java/com/mycompany/cine/       -> 62 pruebas unitarias automatizadas
imagenes/                               -> Almacenamiento local de fotos y carteles
```

---

## 3. REGLAS DE ORO SUPREMAS: PREVENCIÓN DE REGRESIONES Y PROTECCIÓN DE FLUJOS (INVIOLABLES)

> [!CAUTION]
> **ESTÁ ESTRICTAMENTE PROHIBIDO:**
> 1. **OCULTAR O DESCONECTAR LA TAQUILLA:** `cines.fxml` y `login.fxml` son el núcleo del proyecto. Bajo ninguna circunstancia se debe cambiar el inicio de la app a una pantalla aislada que no permita al usuario ver o volver a la Taquilla.
> 2. **CAMBIAR EL PUNTO DE ENTRADA SIN AVISO:** El método `App.start` DEBE iniciar en `login.fxml`. Si por una tarea o video escolar se requiere probar otra pantalla, la IA DEBE advertir primero al usuario y proporcionar botones de retorno.
> 3. **BORRAR CÓDIGO O MÓDULOS EXISTENTES:** Nunca borres clases, métodos, controladores ni vistas previas (Películas, Salas, Funciones, Personal, etc.). Todo cambio debe ser aditivo o compatible hacia atrás.
> 4. **PANTALLAS HUÉRFANAS:** Cualquier nueva vista `.fxml` DEBE:
>    - Tener un botón de acceso en el menú lateral de `cines.fxml`.
>    - Tener en su cabecera un botón `⬅ Volver a Taquilla (cines.fxml)` y `🚪 Salir al Login`.
> 5. **IGNORAR LAS PRUEBAS UNITARIAS:** Existen 62 pruebas unitarias. Ningún cambio puede darse por terminado si alguna prueba falla.

---

## 4. Convenciones de Dominio y Persistencia

### A. Persistencia y DAO
- NO agregues interfaces nuevas. Usa y extiende la existente `Listable<T>` en `persistencia/` o impleméntala en tus DAOs (`AdministradorDAO implements Listable<Administrador>`).
- Las operaciones DAO deben ejecutar consultas preparadas reales (`PreparedStatement`) sobre MySQL `cine_db`.

### B. Gestión de Imágenes (Actividad 3.9)
- Las imágenes seleccionadas mediante `FileChooser` NO se deben guardar con su ruta absoluta de Windows.
- Deben copiarse físicamente a la carpeta del proyecto `imagenes/<subcarpeta>/` usando `GestorImagenes.copiarImagenAlProyecto(...)`.
- Al eliminar un registro de la base de datos, el archivo físico debe borrarse del disco con `GestorImagenes.eliminarImagenDelProyecto(...)`.
- Nunca borrar placeholders por defecto (`default_user.png`, `default_movie.png`).

### C. Dinero
- Usa siempre `java.math.BigDecimal` para precios, montos, porcentajes y descuentos. NUNCA `double` para dinero.

### D. Módulos y JavaFX
- `module-info.java` debe `opens` y `exports` cada paquete usado por FXML o reflexión:
  - `com.mycompany.cine`
  - `com.mycompany.cine.controladores`
  - `com.mycompany.cine.modelo`
  - `com.mycompany.cine.persistencia`
  - `com.mycompany.cine.daos`

---

## 5. Checklist Obligatorio para la IA antes de finalizar su respuesta
Antes de responder al usuario diciendo que una tarea está lista, la IA debe verificar:
- [ ] ¿Compila todo el proyecto sin errores (`javac` / `mvn compile`)?
- [ ] ¿Pasan todas las 62+ pruebas unitarias?
- [ ] ¿La aplicación sigue iniciando en `login.fxml` y abre `cines.fxml` (Taquilla)?
- [ ] ¿Todas las vistas tienen botones para volver a la pantalla principal?
- [ ] Si se modificó la navegación o algún archivo sensible, ¿se le explicó con claridad al usuario?
