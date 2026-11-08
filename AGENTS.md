# AGENTS.md - Proyecto Cine (JavaFX + Maven)

Este archivo guía a cualquier IA que trabaje en este repositorio.

## 1. Qué es este proyecto
Aplicación de escritorio de gestión de taquillas de cine.
- Lenguaje: Java 11
- UI: JavaFX (FXML)
- Build: Maven (pom.xml en la raíz)
- Punto de entrada: `com.mycompany.cine.App` (extiende `javafx.application.Application`)
- Vista principal: `src/main/resources/com/mycompany/cine/cines.fxml` (cargada por `App.start`)

## 2. Estructura de paquetes
```
src/main/java/com/mycompany/cine/
  App.java
  module-info.java
  PrimaryController.java
  SecondaryController.java
  controladores/
    CinesController.java          -> controlador de cines.fxml (implements Initializable)
  modelo/
    Persona.java                  -> abstracta, base de todos los usuarios
    Administrador.java            -> personal (bitacora, nivelAcceso, estadoTurno)
    Gerente.java                  -> taquilla (idCaja, montoApertura, estadoGerente)
    Supervisor.java               -> (codigoAutorizacion, limiteDescuento, estadoSupervisor)
    Pelicula.java, Sala.java, Funcion.java, Boleto.java
    ClienteFan.java, ClienteMegaFan.java
  persistencia/
    Listable.java                 -> interfaz de persistencia en memoria (NO crear interfaces nuevas)
    CineArrayList.java            -> implementación real de Listable<Administrador>
src/main/resources/com/mycompany/cine/
  cines.fxml, primary.fxml, secondary.fxml
```

## 3. Cómo compilar y ejecutar
Desde la raíz del repositorio (`pom.xml`):

```bash
mvn compile               # compila fuentes principales
mvn clean javafx:run      # ejecuta la app (requiere el plugin javafx-maven-plugin configurado en pom.xml)
```

Nota: el POM compila con Java 11 (`release 11`) y declara JavaFX 13. No cambies la versión de JavaFX sin probar en el JDK disponible.

## 4. Convenciones y reglas de oro (no romper estas)

### A. Persistencia
- NO agregues interfaces nuevas. Usa y extiende la existente `Listable<T>` en `persistencia/`.
- `CineArrayList` es la única implementación. Sus métodos deben estar implementados de verdad (no `UnsupportedOperationException` ni cuerpos vacíos).
- Firmas de `Listable`:
  - `boolean agregar(T elemento)`
  - `boolean actualizar(T elemento)`
  - `boolean eliminar(String dato)`
  - `ArrayList<T> listar()`
  - `ArrayList<T> consultar(String dato)`
  - `T traer(String dato)`

### B. Modelo / dominio
- `Persona` es abstracta. Sus subclases concretas: `Administrador`, `Gerente`, `Supervisor`, `ClienteFan`, `ClienteMegaFan`.
- `Persona` tiene campos: `id`, `nombre`, `apellido`, `email`, `contrasena`, `estado`, `activo`. No le quites campos ni cambies la firma del constructor de 6 args sin actualizar todas las subclases.
- Las subclases de cliente/gerente/supervisor no tienen campo `apellido` separado; al llamar a `super(...)` pasan el `nombre` también como `apellido`.
- El campo `estado` de `Persona` ahora se guarda mediante `setEstado()` (por defecto "Activo").
- El campo `contrasena` se guarda mediante `setContrasena()`/`getContrasena()`.

### C. Dinero
- Usa `java.math.BigDecimal` para precios, montos, porcentajes y límites de descuento. NUNCA uses `double` para dinero.
- Getters devuelven `BigDecimal`; setters validan `null` y signo con `compareTo(BigDecimal.ZERO)`.
- En `toString` formatea con `setScale(2, RoundingMode.HALF_UP).toPlainString()` o `doubleValue()` para evitar `IllegalFormatConversionException`.

### D. JavaFX / FXML / módulos
- `module-info.java` debe `opens` y `exports` cada paquete que contenga controladores, modelo o persistencia usados por FXML/reflexión:
  - `com.mycompany.cine`
  - `com.mycompany.cine.controladores`
  - `com.mycompany.cine.modelo`
  - `com.mycompany.cine.persistencia`
- El `fx:controller` de `cines.fxml` debe apuntar a una clase que exista: actualmente `com.mycompany.cine.controladores.CinesController`.
- `App.loadFXML(nombre)` busca `nombre + ".fxml"` en el paquete `com.mycompany.cine` de recursos.
- `requires java.base;` es redundante (implícito), no lo agregues.

### E. Igualdad
- `equals`/`hashCode` de las entidades se basan únicamente en el `id`. No cambies este contrato sin actualizar `CineArrayList.agregar` (control de duplicados por id).

## 5. Qué falta / próximo trabajo recomendado
1. Capa de servicio/logica de negocio (no existe; los controladores están vacíos o son plantillas).
2. Repositorios/persistencia para `Pelicula`, `Sala`, `Funcion`, `Boleto`, `ClienteFan`, `ClienteMegaFan` (solo existe `CineArrayList<Administrador>`).
3. Conectar `cines.fxml` (tiene labels/botones genéricos sin `fx:id`) con el controlador y el modelo.
4. Validaciones de dominio de cine: solapamiento de funciones en una sala, aforo, disponibilidad de asientos, clasificación por edad.
5. Pruebas unitarias: el POM trae JUnit 5 pero no hay `src/test/java`.
6. Unificar constructores de `Persona` (actualmente coexisten el de 4 y el de 6 args; considerar dejar uno solo para evitar confusión).

## 6. Errores ya corregidos (historial)
- `cines.fxml`: `fx:controller` apuntaba a `mycompany.cines.cinesControladores` (inexistente) → ahora `com.mycompany.cine.controladores.CinesController`.
- `module-info.java`: solo exportaba `com.mycompany.cine`; ahora abre/exporta controladores, modelo y persistencia. Se quitó `requires java.base`.
- `Persona`: faltaban `contrasena`, `getEstado`/`setEstado`; constructor de 4 args ahora delega al de 6 con estado por defecto "Activo".
- `ClienteFan`, `ClienteMegaFan`, `Gerente`, `Supervisor`: el `email` y el `apellido` iban intercambiados en la llamada a `super`; `estado` y `contrasena` se ignoraban. Ya se pasan correctamente.
- `double` para dinero → `BigDecimal` en `Funcion.precioBase`, `Boleto.precioFinal`, `Gerente.montoApertura`, `Supervisor.limiteDescuento`, `ClienteFan.porcentajeBase`, `ClienteMegaFan.porcentajeVIP`.
- `Listable` y `CineArrayList`: firma de `agregar` no coincidía y varios métodos lanzaban `UnsupportedOperationException`; ahora están implementados de verdad.
- Se eliminó el duplicado `src/main/java/controladores/CinesController.java` (paquete incorrecto).
