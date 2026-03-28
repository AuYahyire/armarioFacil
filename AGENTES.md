Documento de Definición de Proyecto: App de Gestión de Armario Infantil
1. Visión General
El objetivo es desarrollar una aplicación Android nativa, de alcance acotado y bajo mantenimiento ("dispara y olvida"), que permita a los padres gestionar el inventario de ropa de sus hijos. La app facilitará la visualización de prendas disponibles, alertas de tallas y organización por temporadas, ofreciendo una opción premium basada en Inteligencia Artificial para agilizar el registro de la ropa.

2. Experiencia de Usuario y Navegación principal
Dashboard (Panel de Control):

Pantalla de inicio con un resumen general del estado del armario.

Métricas: Cantidad total de ropa por niño.

Sistema de Avisos: Alertas visuales de prendas que están próximas a quedar pequeñas (basado en la talla registrada y el crecimiento/edad del niño).

Botones de acción rápida: "Añadir Niño" y "Añadir Ropa".

Exploración e Inventario:

Buscador de texto libre (por nombre o características).

Filtros avanzados: Por niño, tipo de ropa (pantalón, camiseta, abrigo, etc.), temporada (verano, invierno, entretiempo) y talla.

3. Gestión de Datos y Registro de Prendas
Entidades Principales:

Niño: Nombre, fecha de nacimiento/edad actual (para calcular avisos de talla).

Ropa: Al registrar una prenda, el formulario requiere los siguientes campos:

Fotografía de la prenda.

Nombre descriptivo.

Tipo de ropa.

Temporada.

Talla.

Color.

Modo de Ingreso Manual: El usuario siempre puede rellenar los datos de la ropa a mano de forma 100% gratuita e ilimitada.

4. Asistente Inteligente (Auto-completado por IA)
Para agilizar la entrada de datos, la app cuenta con una función premium de escaneo inteligente.

Flujo de uso: El usuario toma la foto de la prenda y pulsa el botón de "Escaneo IA".

Procesamiento: Un modelo de visión artificial analiza la imagen y extrae la información visual para autocompletar automáticamente los campos del formulario (Nombre sugerido, Tipo de ropa, Temporada recomendada y Color dominante).

Validación: El usuario revisa los datos autocompletados, introduce la Talla (que suele ser difícil de detectar en foto) y guarda el registro.

5. Modelo de Negocio y Monetización
El modelo evita suscripciones recurrentes, cuentas de usuario y la fricción inicial. Se monetiza la comodidad, no el acceso a la herramienta.

Sin inicios de sesión: El usuario descarga la app y la usa inmediatamente. No hay gestión de cuentas, contraseñas ni emails.

Paquetes de IA (In-App Purchases): El escaneo por IA es una función exclusivamente de pago. Se venden paquetes consumibles de 200 usos de IA por 2,99 €.

Gestión de Compras: Se utilizará la pasarela nativa de Google Play (gestionada a través de un servicio como RevenueCat) vinculada a la cuenta de Google del dispositivo. Si el usuario cambia de teléfono, restaura su compra y sus créditos asociados a su cuenta de Google Play nativa.

6. Arquitectura Técnica ("Dispara y Olvida")
La infraestructura está diseñada para reducir los costes fijos de servidores a cero y delegar el almacenamiento en el propio usuario.

Almacenamiento Local y Descentralizado:

Base de datos móvil: Toda la información (datos y fotos) reside localmente en el dispositivo (ej. usando SQLite/Room).

Copias de seguridad: La app ofrecerá la opción de exportar/sincronizar un backup cifrado directamente en el Google Drive personal del usuario. El desarrollador no custodia datos personales.

Micro-Backend (Seguridad de la IA):

Protección de API Keys: Las claves de la IA no residirán en el código de la app.

Cloud Function (Serverless): Solo cuando un usuario que ha pagado solicita un escaneo, la app llama a una función ligera en la nube. Esta función verifica el recibo de compra en Google Play, descuenta un crédito del usuario, realiza la consulta a la IA de forma segura, y devuelve el texto a la app.

1. Estructura de la Base de Datos Local (Room / SQLite)
Necesitamos dos tablas principales (Entidades) relacionadas entre sí. La clave aquí es guardar la ruta (URI) de las fotos en el almacenamiento local del dispositivo, no la imagen en sí dentro de la base de datos, para mantenerla rápida y ligera.

Tabla 1: Niño (Child Entity)
Almacena los perfiles. La fecha de nacimiento es crucial para que la app pueda calcular automáticamente la edad actual y lanzar los "Avisos de Talla".

id (Clave Primaria, Autogenerada): Identificador único.

nombre (Texto): Nombre del niño/a.

fecha_nacimiento (Fecha / Long Timestamp): Para calcular la edad en meses/años.

foto_perfil_uri (Texto, Opcional): Ruta local a la foto del niño.

fecha_creacion (Fecha / Long Timestamp): Cuándo se creó el perfil.

Tabla 2: Prenda (Clothing Entity)
Almacena el inventario. Cada prenda pertenece a un niño.

id (Clave Primaria, Autogenerada): Identificador único de la prenda.

nino_id (Clave Foránea / Foreign Key): Vincula la prenda con el ID de la tabla Niño.

foto_prenda_uri (Texto): Ruta local donde está guardada la foto de la ropa.

nombre (Texto): Ej. "Sudadera de dinosaurios".

categoria (Texto): Ej. "Camiseta", "Pantalón", "Abrigo", "Calzado", "Accesorios".

temporada (Texto): Ej. "Verano", "Invierno", "Entretiempo", "Cualquiera".

talla (Texto): Ej. "12-18 meses", "Talla 4", "Zapatos 25".

color_principal (Texto): Ej. "Azul", "Rojo", "Multicolor".

estado_uso (Texto / Enum): Ej. "En uso", "Queda pequeña", "Guardada para el hermano". (Esto permite filtrar la ropa que ya no le sirve sin tener que borrarla del historial).

2. El "Prompt" del Asistente Inteligente (Vision AI)
Cuando el usuario paga y usa un crédito para escanear una foto, la Cloud Function debe enviar la imagen a la IA (como OpenAI GPT-4o o Gemini Flash) junto con unas instrucciones muy estrictas.

El objetivo de este Prompt es asegurar que la IA siempre responda en un formato de datos estructurado (JSON), para que tu app Android pueda leer la respuesta y rellenar las casillas del formulario automáticamente sin que el código falle.

Prompt de Sistema (Instrucciones para la IA):

"Eres un asistente experto en catalogación de moda infantil. Tu tarea es analizar la imagen adjunta de una prenda de ropa de niño/a y extraer sus características principales.

Debes devolver ÚNICAMENTE un objeto JSON válido con la siguiente estructura, sin texto adicional ni formato markdown:

{
"nombre": "Un nombre descriptivo y corto de la prenda (máximo 4 palabras, ej: 'Camiseta manga corta rayas')",
"categoria": "Clasifica la prenda en UNA de estas opciones exactas: [Camiseta, Camisa, Pantalón, Abrigo, Jersey/Sudadera, Vestido, Calzado, Accesorio, Ropa Interior, Pijama]",
"temporada": "Clasifica en UNA de estas opciones: [Verano, Invierno, Entretiempo, Todas]",
"color": "El color principal dominante de la prenda"
}

Si no puedes identificar la prenda con claridad, devuelve los campos con el valor 'Desconocido'. Nunca intentes adivinar la talla, ya que no hay referencias de tamaño en la foto."

1. La Lógica de las "Alertas de Tallas" (Sin complicar el código)
Para mantener la filosofía de bajo mantenimiento y no obligar al usuario a meter datos hiper-complejos, la lógica de avisos debe ser sencilla pero efectiva. Funciona cruzando dos datos: la edad del niño y la talla de la ropa.

El Motor de Alertas (Cómo funciona por dentro):

Cálculo de Edad en Tiempo Real: Como la base de datos tiene la fecha_nacimiento del niño, la app calcula silenciosamente su edad exacta en meses (ej. tiene 17 meses).

Estandarización de la Talla: Cuando el usuario añade una prenda, si la talla incluye un rango de meses (muy común en ropa de bebé, ej. "12-18 meses" o "24 meses"), la app extrae el número mayor (el límite: 18 meses).

Los Disparadores (Triggers):

Alerta Amarilla (A punto de expirar): Si la edad del niño está a 1 mes del límite de la prenda (ej. el niño tiene 17 meses y la prenda es hasta 18). Muestra un aviso: "Revisa si aún le sirve".

Alerta Roja (Obsoleta): Si la edad del niño supera el límite (ej. niño de 19 meses con prenda de 18). Muestra un aviso: "Probablemente ya le quede pequeña".

El "Botón de la Verdad" (Manual): Como los niños crecen a ritmos distintos y las tallas de niño mayor no van por meses (ej. "Talla 4", "Talla 6"), la app debe incluir un botón rápido en cada prenda que diga "Marcar como pequeña". Esto saca la prenda del armario activo sin necesidad de eliminarla (ideal si se quiere guardar para un hermano menor).

2. Flujo de Pantallas y Experiencia de Usuario (UI/UX)
Para que la app se sienta moderna y fácil de usar con una sola mano (pensando en padres ocupados), el esquema de navegación debe tener máximo 4 pantallas principales:

Pantalla 1: Dashboard (El Centro de Mando)
Es lo primero que ve el usuario al abrir la app.

Cabecera: Saludo y un gráfico circular muy visual con el resumen ("Tienes 145 prendas en total").

Sección de Alertas: Un carrusel deslizable con avisos. Ej: "A Mateo le van a quedar pequeños 3 pantalones pronto". Al tocar, te lleva a ver cuáles son.

Tarjetas de Niños: Una tarjeta grande por cada niño registrado. Muestra su foto, edad actual y un resumen rápido ("45 prendas de verano, 20 de invierno").

Botón Flotante (FAB): Un botón grande y accesible con el símbolo "+" para añadir ropa o niños rápidamente.

Pantalla 2: El Armario Individual (La vista del niño)
Al tocar la tarjeta de un niño, entramos a su armario.

Filtros Superiores (Píldoras): Botones de un solo toque para filtrar: [Verano] [Invierno] [Pantalones] [Camisetas] [Le queda pequeño].

Cuadrícula de Ropa: Una galería tipo Pinterest o Instagram con las fotos de la ropa. Las prendas con "Alerta de Talla" tienen un pequeño icono rojo o amarillo en la esquina de su foto.

Gestos Rápidos: Si mantienes pulsada una foto, sale un menú rápido: Eliminar, Editar, o Marcar como pequeña.

Pantalla 3: El Escáner Inteligente (La magia de pago)
Al pulsar "+" y elegir "Añadir Prenda", se abre la cámara.

Visor de Cámara: Con una silueta tenue de una camiseta para que el usuario encuadre bien la ropa.

Botón "Escanear con IA": Abajo, un botón brillante que indica los créditos restantes (ej. 🪄 Autocompletar (198 usos restantes)).

Nota: También hay un botón secundario discreto que dice "Introducir datos a mano" (Gratis).

Pantalla 4: Ficha de la Prenda (Detalle, Edición y Borrado)
Aquí se llega tras escanear la ropa o al tocar una prenda ya guardada en el armario.

Foto en Grande: En la mitad superior de la pantalla.

Formulario: Los campos (Nombre, Categoría, Temporada, Color) ya rellenados por la IA. El usuario solo debe seleccionar la Talla en un menú desplegable.

Botones de Acción Final:

Guardar cambios.

Eliminar Prenda (con un aviso de confirmación: "¿Seguro que quieres borrar esta prenda? Esta acción no se puede deshacer").

Pasar a otro niño: (Muy útil para heredar ropa entre hermanos).

Stack Tecnológico Recomendado (Android Nativo)
1. Desarrollo Front-End (La App Móvil)
Lenguaje: Kotlin. Es el estándar oficial de Google para Android. Moderno, seguro frente a errores comunes y muy eficiente.

Interfaz Gráfica (UI): Jetpack Compose. Es el paradigma actual para crear pantallas en Android. Permite construir la interfaz visual mucho más rápido y con menos código que el sistema antiguo, ideal para lograr animaciones fluidas y un diseño limpio.

Arquitectura: MVVM (Model-View-ViewModel). Separa la lógica de los datos de la interfaz visual. Evita el código espagueti y hace que cualquier mantenimiento futuro sea rápido y sin romper otras partes de la app.

Carga de Imágenes: Coil. Una librería ultraligera impulsada por Kotlin para cargar las fotos de la ropa desde la memoria del teléfono a la pantalla rápidamente y sin saturar la memoria RAM del dispositivo.

2. Almacenamiento de Datos (Local)
Base de Datos: Room. Es la herramienta oficial de Google para trabajar con bases de datos locales (SQLite). Trabaja de forma 100% offline, es robusta y encaja perfectamente con el enfoque descentralizado del proyecto.

Gestión de Archivos: Las fotos en alta resolución se guardarán en el directorio privado de la app en el teléfono, y Room solo almacenará la ruta de texto (URI) hacia esa imagen. Esto mantiene la base de datos ligera y ágil.

3. Monetización y Pagos In-App
Gestión de Compras: RevenueCat (por encima de Google Play Billing).

Motivo: Implementar pagos nativos en Android desde cero es tedioso y propenso a errores. RevenueCat actúa como un intermediario que valida los recibos de compra de forma segura, gestiona el saldo del usuario y permite restaurar las compras fácilmente si el usuario cambia de móvil, ahorrándote semanas de desarrollo y servidores propios.

4. Micro-Backend y Seguridad (Serverless)
Infraestructura: Firebase Cloud Functions (escrito en Node.js o TypeScript).

Motivo: Mantiene el modelo "dispara y olvida". Este pequeño trozo de código en la nube solo "despierta" cuando un usuario de pago envía una foto. Consulta si hay saldo disponible, usa la API Key oculta para hablar con la IA, devuelve los datos y vuelve a apagarse. Solo pagas por milisegundos de uso y la capa gratuita de Firebase cubre miles de peticiones mensuales.

5. Inteligencia Artificial (Computer Vision)
API Recomendada: Modelos de visión rápidos y de bajo coste como OpenAI (GPT-4o mini) o Google Gemini (Flash).

Motivo: Son capaces de analizar imágenes con gran precisión, su coste por petición es de fracciones de céntimo (lo que maximiza tu margen en los paquetes de 2,99 €) y se les puede forzar fácilmente a devolver las respuestas en un formato estructurado (JSON) que la app entienda automáticamente.

Fase 1: Configuración Inicial y Arquitectura
[ ] Crear el repositorio en Git (GitHub, GitLab, etc.).

[ ] Inicializar el proyecto en Android Studio usando Kotlin.

[ ] Configurar dependencias en build.gradle (Jetpack Compose, Room, Coil, Retrofit/Ktor, Firebase, RevenueCat).

[ ] Estructurar las carpetas del proyecto siguiendo el patrón MVVM (Model, View, ViewModel, Repository, UI).

[ ] Definir la paleta de colores, tipografías y tema global en Jetpack Compose.

Fase 2: Base de Datos Local (Room)
[ ] Crear la Entity de Niño (id, nombre, fecha_nacimiento, foto_uri).

[ ] Crear la Entity de Prenda (id, nino_id, foto_uri, nombre, categoria, temporada, talla, color, estado).

[ ] Crear el DAO de Niño (métodos para insertar, listar, actualizar y borrar niños).

[ ] Crear el DAO de Prenda (métodos para insertar, borrar, cambiar de niño y consultas filtradas por temporada/categoría).

[ ] Implementar la base de datos central AppDatabase en Room.

Fase 3: Gestión de Archivos (Fotos)
[ ] Configurar los permisos de cámara en el AndroidManifest.xml.

[ ] Programar la lógica para abrir la cámara desde la app.

[ ] Programar la lógica para guardar la imagen capturada en el almacenamiento interno privado de la app (para no saturar la galería del usuario).

[ ] Programar la conversión de esa imagen guardada a una ruta de texto (URI) para guardarla en Room.

Fase 4: Lógica de Negocio (ViewModels)
[ ] Crear el ViewModel para los Niños: calcular edad en meses/años a partir de su fecha_nacimiento.

[ ] Crear el ViewModel para la Ropa: manejar la inserción manual y la aplicación de los filtros de búsqueda.

[ ] Programar el "Motor de Alertas": cruzar la edad del niño con el número máximo de la talla de la prenda para disparar banderas de "Atención" o "Pequeña".

Fase 5: Interfaz de Usuario (UI - Pantallas)
[ ] Pantalla 1 - Dashboard: Construir gráfica resumen, listado horizontal de niños y carrusel de alertas de tallas.

[ ] Pantalla 2 - Armario del Niño: Construir el panel de filtros ("píldoras" clickeables) y la cuadrícula (grid) de fotos de las prendas.

[ ] Pantalla 3 - Cámara/Escáner: Crear la interfaz de captura con el botón de "Escaneo IA" y los créditos restantes.

[ ] Pantalla 4 - Ficha de la Prenda: Formulario editable con foto arriba, campos de texto/desplegables abajo, y botones de "Guardar", "Eliminar" y "Pasar a otro niño".

[ ] Implementar la navegación entre pantallas usando Jetpack Navigation Compose.

Fase 6: Micro-Backend (Protección IA y Serverless)
[ ] Crear un proyecto en Firebase y vincularlo a la app Android.

[ ] Inicializar Firebase Cloud Functions (Node.js o TypeScript).

[ ] Configurar el "Gestor de Secretos" de Google Cloud para ocultar ahí tu API Key de OpenAI / Gemini.

[ ] Escribir la Cloud Function que recibe la foto de Android, comprueba créditos, la envía a la IA con el Prompt de Sistema y devuelve el JSON estructurado.

[ ] Implementar el mapeo en Android para que ese JSON rellene automáticamente los campos de la Pantalla 4.

Fase 7: Monetización (RevenueCat y Google Play)
[ ] Crear una cuenta de desarrollador en Google Play Console (pago único de $25 a Google).

[ ] Crear el producto in-app "Paquete de 200 Créditos IA" en la consola de Google Play.

[ ] Crear cuenta en RevenueCat y enlazarla con Google Play.

[ ] Integrar el SDK de RevenueCat en la app de Android.

[ ] Programar el botón "Comprar Escaneos" y el flujo de pago con Google Play Billing.

[ ] Sincronizar el saldo pagado con la Cloud Function de Firebase.

Fase 8: Pruebas y QA (Quality Assurance)
[ ] Probar el flujo "Offline y Gratis": Añadir niños y prendas sin internet.

[ ] Probar la persistencia: Cerrar la app, reiniciar el móvil y comprobar que los datos y fotos siguen ahí.

[ ] Probar el motor de alertas de talla forzando fechas de nacimiento antiguas.

[ ] Probar el borrado en cascada: Si se elimina un niño, ¿se elimina su ropa o queda huérfana? (Programar para que se elimine o pregunte).

[ ] Probar el flujo premium en entorno de pruebas (Sandbox): comprar un paquete de prueba, consumir un crédito, validar respuesta de la IA y verificar saldo actualizado.

Fase 9: Preparación para Lanzamiento
[ ] Diseñar el icono de la aplicación (launcher icon).

[ ] Generar capturas de pantalla promocionales para la tienda.

[ ] Redactar una Política de Privacidad sencilla (obligatorio para Google Play, especificando que las fotos y datos infantiles no salen del teléfono).

[ ] Generar el archivo final firmado (.aab - Android App Bundle).

[ ] Subir a Google Play Console, rellenar el cuestionario de contenido y mandar a revisión.



> Nota: Si hay tareas que no puedes realizar, marcalas con H (H de humano) y explica por qué no puedes hacerlas. No intentes hacer tareas que no puedes realizar.