# 📝 Examen - Desarrollo de Aplicaciones Android con Kotlin
## Resumen de Respuestas y Explicaciones

---

### 1. ¿Cuál de los siguientes lenguajes es recomendado para desarrollo Android?
**Respuesta: d. Kotlin** ✅

Kotlin es el lenguaje oficial recomendado por Google desde 2019 para desarrollo Android. Ofrece sintaxis concisa, null safety y está completamente integrado en Android Studio.

---

### 2. ¿Cuál es el primer método que se ejecuta en el ciclo de vida de una actividad?
**Respuesta: a. onCreate()** ✅

`onCreate()` es el primer método llamado cuando se crea una Activity. Aquí se inicializan componentes, se infla el layout y se configuran listeners.

**Ciclo completo**: `onCreate()` → `onStart()` → `onResume()` → `onPause()` → `onStop()` → `onDestroy()`

---

### 3. ¿Qué problema ocurre si no se maneja bien la posición de un ítem?
**Respuesta: c. Se elimina o selecciona un elemento incorrecto** ✅

Usar una posición incorrecta puede causar que se elimine o edite la tarea equivocada, resultando en pérdida de datos del usuario.

**Ejemplo correcto**:
```kotlin
val posicion = bindingAdapterPosition
if (posicion != RecyclerView.NO_POSITION) {
    onEliminarClick(posicion)
}
```

---

### 4. Los fragmentos tienen su propio ciclo de vida sincronizado con la Activity
**Respuesta: Verdadero** ✅

Los Fragments tienen su propio ciclo de vida (`onAttach()`, `onCreate()`, `onCreateView()`, etc.) que se sincroniza con el de la Activity contenedora.

---

### 5. ¿Principal ventaja de usar ViewBinding?
**Respuesta: b. Acceso directo a los views sin necesidad de findViewById** ✅

ViewBinding genera clases que proporcionan acceso type-safe y null-safe a las vistas, eliminando `findViewById()`.

**Ejemplo**:
```kotlin
binding.textoContador.text = "Hola"  // Directo y seguro
```

---

### 6. ¿Cómo se usa binding para acceder a un TextView?
**Respuesta: a. binding.textView.text** ✅

ViewBinding convierte el ID de la vista (`android:id="@+id/textView"`) en una propiedad accesible directamente.

---

### 7. ¿Qué componente se recomienda para listas eficientes?
**Respuesta: b. RecyclerView** ✅

RecyclerView es el estándar moderno para listas, ofreciendo:
- ♻️ Reutilización de vistas (ViewHolder Pattern)
- ⚡ Mejor rendimiento
- 🎨 Flexibilidad (vertical, horizontal, grid)
- 👆 Soporte para gestos

---

### 8. Para firmar una app antes de publicarla, se necesita:
**Respuesta: a. .keystore** ✅

El archivo `.keystore` (o `.jks`) contiene las claves criptográficas necesarias para firmar tu aplicación para publicarla en Google Play Store.

---

### 9. ¿Qué método se llama cuando la actividad deja de ser visible?
**Respuesta: a. onStop()** ✅

`onStop()` se llama cuando la actividad ya NO es visible pero aún existe en memoria.

**Diferencias**:
- `onPause()`: Pierde foco pero puede ser parcialmente visible
- `onStop()`: Completamente no visible
- `onDestroy()`: Se destruye completamente

---

### 10. ¿Cuál es el propósito principal del EditText?
**Respuesta: d. Permitir al usuario ingresar texto editable** ✅

EditText permite al usuario escribir, editar y modificar texto. A diferencia de TextView que solo muestra texto.

---

### 11. ¿Qué es un Intent en Android?
**Respuesta: a. Un objeto que permite la comunicación entre componentes** ✅

Intent es un objeto de mensajería para:
- 🔀 Navegar entre Activities
- 📦 Pasar datos entre componentes
- 🔔 Iniciar servicios y BroadcastReceivers

---

### 12. ¿Cómo se define la orientación en LinearLayout?
**Respuesta: a. android:orientation** ✅

`android:orientation` define la dirección:
- `vertical`: Elementos uno debajo del otro ⬇️
- `horizontal`: Elementos uno al lado del otro ➡️

---

### 13. ¿Objetivo principal de usar listas dinámicas?
**Respuesta: b. Mostrar información adaptable a los datos del usuario** ✅

Las listas dinámicas se adaptan automáticamente cuando los datos cambian (agregar, eliminar, modificar) sin reiniciar la app.

---

### 14. ¿Qué pasa al usar binding incorrecto (ActivityMainBinding en lugar de ActivitySegundoBinding)?
**Respuesta: a. Se genera un error de tipo en tiempo de compilación** ✅

ViewBinding es type-safe, detecta errores ANTES de ejecutar la app, evitando crashes en runtime.

---

### 15. ¿Qué acción permite agregar elementos dinámicamente a una lista?
**Respuesta: b. Modificar el Adapter y notificar el cambio** ✅

**Patrón estándar**:
```kotlin
listaTareas.add(tarea)  // 1. Modificar datos
adapter.notifyItemInserted(posicion)  // 2. Notificar
```

---

### 16. ¿Propósito del método onDestroy()?
**Respuesta: c. Limpiar recursos antes de que la actividad sea destruida** ✅

`onDestroy()` es el último método del ciclo de vida. Aquí se liberan recursos:
- 🗑️ Cerrar bases de datos
- 🔔 Cancelar notificaciones
- 🌐 Cerrar conexiones

---

### 17. ¿Propósito de binding = ActivityMainBinding.inflate(layoutInflater)?
**Respuesta: b. Inflar el layout y obtener referencias a los views** ✅

Convierte el XML en objetos View y crea el objeto binding con referencias type-safe a todas las vistas.

---

### 18. ¿Qué atributo define el texto de un Button?
**Respuesta: d. android:text** ✅

```xml
<Button
    android:text="Guardar"
    android:textSize="16sp"/>
```

---

### 19. ¿Función principal de un Adapter en Android?
**Respuesta: a. Conectar los datos con las vistas de la lista** ✅

El Adapter actúa como puente:
```
Datos (List<Tarea>) → Adapter → RecyclerView → UI visible
```

---

### 20. ¿Principal ventaja de ConstraintLayout sobre LinearLayout?
**Respuesta: a. Permite colocación flexible sin anidar vistas** ✅

ConstraintLayout crea layouts planos (flat) en un solo nivel, mejorando rendimiento y ofreciendo más flexibilidad.

---

### 21. ¿Qué clase se genera para activity_main.xml con ViewBinding?
**Respuesta: d. ActivityMainBinding** ✅

**Patrón de conversión**:
- `activity_main.xml` → `ActivityMainBinding`
- `item_tarea.xml` → `ItemTareaBinding`
- `fragment_home.xml` → `FragmentHomeBinding`

---

### 22. ¿Qué método detecta cambios de texto en tiempo real en EditText?
**Respuesta: a. addTextChangedListener()** ✅

```kotlin
editText.addTextChangedListener(object : TextWatcher {
    override fun onTextChanged(s: CharSequence?, ...) {
        // Detecta cambios en tiempo real
    }
})
```

---

### 23. ¿Para qué sirve el método getItemCount()?
**Respuesta: b. Indicar cuántos elementos tiene la lista** ✅

```kotlin
override fun getItemCount(): Int = listaTareas.size
```

RecyclerView usa este método para saber cuántas vistas crear.

---

### 24. ¿Qué herramienta se usa para ver logs de la aplicación?
**Respuesta: d. Logcat** ✅

Logcat muestra todos los logs en tiempo real:
```kotlin
Log.d("TAG", "Mensaje de debug")
Log.e("TAG", "Mensaje de error")
```

---

### 25. ¿Qué método del Adapter asocia datos con la vista?
**Respuesta: d. onBindViewHolder()** ✅

```kotlin
override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val dato = listaDatos[position]  // Obtiene dato
    holder.vincular(dato)             // Lo muestra en vista
}
```

---

### 26. ¿Qué hace un TextView?
**Respuesta: d. Muestra texto que no puede ser editado por el usuario** ✅

TextView es para **lectura** (📖), EditText es para **escritura** (✏️).

---

### 27. ¿Qué es una lista en desarrollo Android?
**Respuesta: d. Una colección de datos mostrados dinámicamente en la interfaz** ✅

Una lista combina:
- 📊 Datos (MutableList)
- 🎨 Adapter
- 📱 Vista (RecyclerView)
- ⚡ Actualización dinámica

---

### 28. ¿Qué método hace visible un layout inflado con ViewBinding?
**Respuesta: d. setContentView(binding.root)** ✅

```kotlin
binding = ActivityMainBinding.inflate(layoutInflater)
setContentView(binding.root)  // Hace visible
```

---

### 29. ¿Qué representa el layout de un ítem en una lista?
**Respuesta: d. El diseño que se repite para cada elemento** ✅

`item_tarea.xml` define cómo se ve CADA tarea en la lista. El mismo diseño se reutiliza para todos los elementos.

---

### 30. ¿Cuál es el rol del ViewHolder?
**Respuesta: b. Mantener referencias a las vistas del ítem** ✅

ViewHolder guarda referencias a las vistas para evitar búsquedas repetidas:

```kotlin
inner class TareaViewHolder(private val binding: ItemTareaBinding) {
    fun vincular(tarea: Tarea) {
        // Referencias ya guardadas, acceso directo ⚡
        binding.textoNombre.text = tarea.nombre
    }
}
```

**Ventajas**:
- ⚡ Mejor rendimiento
- ♻️ Reutilización eficiente
- 💾 Uso óptimo de memoria

---

## 📊 Resumen de Conceptos Clave

### Ciclo de Vida de Activity
1. `onCreate()` - Inicialización
2. `onStart()` - Visible
3. `onResume()` - Interactiva
4. `onPause()` - Pierde foco
5. `onStop()` - No visible
6. `onDestroy()` - Limpieza

### ViewBinding
- Acceso type-safe y null-safe a vistas
- Elimina `findViewById()`
- Detecta errores en compilación

### RecyclerView + Adapter + ViewHolder
- **RecyclerView**: Contenedor de la lista
- **Adapter**: Conecta datos con vistas
- **ViewHolder**: Mantiene referencias a vistas
- **3 métodos clave**: `getItemCount()`, `onCreateViewHolder()`, `onBindViewHolder()`

### Layouts
- **LinearLayout**: Elementos en una dirección (vertical/horizontal)
- **ConstraintLayout**: Posicionamiento flexible sin anidación

---

**Proyecto Final**: Aplicación "Mis Tareas" en Kotlin
- ✅ CRUD de tareas
- 🔍 Búsqueda y filtros
- 📊 RecyclerView con Adapter personalizado
- 🎨 Material Design
- 🔔 Sistema de notificaciones
- 💾 Persistencia con SharedPreferences

**Repositorio**: https://github.com/luisfernandoAngulo28/PROYECTO-FINAL-KOTLIN-UPEA.

---

*Fecha: 7 de enero de 2026*  
*Autor: Luis Fernando Angulo Heredia*  
*Curso: Desarrollo de Aplicaciones Android desde Cero con Kotlin - UPEA*
