# MI_PLANNING

## Estado actual
He leído el archivo `AGENTES.md` que contiene el Documento de Definición de Proyecto para una "App de Gestión de Armario Infantil" en Android (Kotlin + Jetpack Compose + Room + Firebase + RevenueCat).
El archivo detalla las funcionalidades, la UI/UX, el stack tecnológico y las fases del proyecto (Fase 1 a 9).

He finalizado la **Fase 1: Configuración Inicial y Arquitectura**.

**Tareas completadas en la Fase 1:**
- [x] Crear el repositorio (inicializado base).
- [x] Inicializar el proyecto en Android usando Kotlin (creando la estructura de carpetas y archivos Gradle base en Kotlin DSL).
- [x] Configurar dependencias en `build.gradle.kts` (Jetpack Compose, Room, Coil, Retrofit/Ktor, Firebase, RevenueCat).
- [x] Estructurar las carpetas del proyecto siguiendo el patrón MVVM (Model, View, ViewModel, Repository, UI).
- [x] Definir la paleta de colores, tipografías y tema global inicial en Jetpack Compose (`ui/theme`).
- [x] Crear punto de entrada Compose en `MainActivity.kt`.

## Próxima tarea
De acuerdo con las instrucciones de `AGENTES.md`, la siguiente tarea es iniciar la **Fase 2: Base de Datos Local (Room)**.

**Pasos a realizar para la Fase 2:**
- Crear la Entity de Niño (id, nombre, fecha_nacimiento, foto_uri).
- Crear la Entity de Prenda (id, nino_id, foto_uri, nombre, categoria, temporada, talla, color, estado).
- Crear el DAO de Niño (métodos para insertar, listar, actualizar y borrar niños).
- Crear el DAO de Prenda (métodos para insertar, borrar, cambiar de niño y consultas filtradas por temporada/categoría).
- Implementar la base de datos central AppDatabase en Room.

## Limitaciones (H)
- Inicializar el proyecto exactamente desde "Android Studio" es una tarea interactiva de interfaz gráfica. En su lugar, la estructura base del proyecto Android y los archivos Gradle se pueden crear mediante scripts, archivos base o utilizando herramientas de línea de comandos como `gradle init`.
- Probar la app en un emulador o dispositivo físico requerirá un humano (H).
- Crear cuentas en Google Play Console, RevenueCat y Firebase, y obtener las API Keys reales son tareas que debe hacer un Humano (H).
