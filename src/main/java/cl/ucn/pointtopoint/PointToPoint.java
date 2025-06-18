package cl.ucn.pointtopoint;

// Importación de clases de Vert.x necesarias para crear vértices y manejar promesas
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
// Logger para imprimir información en consola
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Clase principal
public class PointToPoint {

    public static void main(String[] args) {
        // Creación de la instancia de Vert.x
        var vertx = Vertx.vertx();

        // Despliegue del vértice emisor
        vertx.deployVerticle(new Sender());

        // Despliegue del vértice receptor
        vertx.deployVerticle(new Receiver());
    }

    // Clase interna que representa el vértice emisor
    static class Sender extends AbstractVerticle {

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            // Indicar que el vértice se ha iniciado correctamente
            startPromise.complete();

            // Ejecuta cada 1000 milisegundos (1 segundo) un bloque de código
            vertx.setPeriodic(1000, id -> {
                // Enviar un mensaje al bus de eventos cada segundo
                vertx.eventBus().send(Sender.class.getName(), "Enviando un mensaje...");
            });

            // También envía un mensaje una vez inmediatamente al iniciar
            vertx.eventBus().send(Sender.class.getName(), "Enviando un mensaje...");
        }
    }

    // Clase interna que representa el vértice receptor
    static class Receiver extends AbstractVerticle {

        // Logger para imprimir mensajes de depuración
        private static final Logger Log = LoggerFactory.getLogger(Receiver.class);

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            // Indicar que el vértice se ha iniciado correctamente
            startPromise.complete();

            // Consumidor que escucha los mensajes enviados al canal identificado por el nombre de la clase Sender
            vertx.eventBus().consumer(Sender.class.getName(), message -> {
                // Imprime en consola el cuerpo del mensaje recibido
                Log.debug("Received: {}", message.body());
            });
        }
    }
}
