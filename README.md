# 🌀 Breve Intro a la Programación Reactiva con Vert.x y EventBus

Este proyecto demuestra tres patrones fundamentales de comunicación reactiva usando **Vert.x**:

1. **Point-to-Point** (punto a punto)
2. **Publish-Subscribe** (publicador-suscriptor)
3. **Request-Response** (petición-respuesta)

Cada patrón se encuentra implementado en una carpeta distinta dentro del proyecto.

---

## 📚 ¿Qué es la Programación Reactiva?

La **programación reactiva** es un paradigma basado en flujos de datos asincrónicos y propagación de cambios. Ideal para aplicaciones concurrentes, distribuidas y no bloqueantes.

---

## ⚙️ ¿Qué es Vert.x?

**Eclipse Vert.x** es un toolkit reactivo para la JVM que permite construir aplicaciones escalables y asincrónicas. Algunas características clave:

- Totalmente asincrónico y no bloqueante.
- Basado en componentes llamados **verticles**.
- Comunicación mediante un **EventBus** interno.
- Compatible con múltiples lenguajes: Java, Kotlin, JavaScript, etc.

---

## 🔄 Patrones de Comunicación

### 📍 1. Point-to-Point

**Carpeta:** `pointtopoint/`

- Usa el método `send()` del `EventBus`.
- Envía un mensaje a un único consumidor registrado (seleccionado automáticamente).
- Es un patrón común para tareas distribuidas o balanceo de carga.

**Código clave:**
```java
vertx.eventBus().send("direccion", "mensaje");
```

### 📍 2. Publish-Subscribe
Carpeta: publishsubscribe/

- Usa el método publish() del EventBus.
- Envía un mensaje a todos los consumidores suscritos a una dirección.
- Ideal para difusión de eventos o notificaciones globales.

**Código clave:**
```java
vertx.eventBus().publish("direccion", "mensaje");
```

**Ejemplo:**
```java
vertx.setPeriodic(10000, id -> {
        vertx.eventBus().publish(Publish.class.getName(), "Un mensaje para todos!");
});
```

### 📍 3. Request-Response
Carpeta: publishsubscribe/

- Usa el método request() del EventBus.
- Envía una solicitud y espera una respuesta asíncrona.
- Modelo útil para obtener datos de otro servicio o componente.

**Código clave:**
```java
vertx.eventBus().request("direccion", "solicitud", reply -> {
        if (reply.succeeded()) {
            System.out.println("Respuesta: " + reply.result().body());
        }
});
```

**Ejemplo:**
```java
vertx.eventBus().request("servicio", "¿Cuál es el estado?", reply -> {
        LOG.debug("Response: {}", reply.result().body());
});
```

## ▶️ ¿Cómo ejecutar?

Requiere **Java 17+** y **Maven**.

Desde la raíz del proyecto, puedes ejecutar cualquier patrón:

```bash
cd pointtopoint
mvn clean compile exec:java -Dexec.mainClass=cl.ucn.pointtopoint.PointToPoint

cd publishsubscribe
mvn clean compile exec:java -Dexec.mainClass=cl.ucn.publishsubscribe.PublishSubscribe

cd requestresponse
mvn clean compile exec:java -Dexec.mainClass=requestresponse.RequestResponse
```