// Paquete donde se encuentra la clase
package cl.ucn.requestresponse;

// Importaciones necesarias de Vert.x y logging
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponse {

    // Metodo main: punto de entrada del programa
    public static void main(String[] args) {
        var vertx = Vertx.vertx(); // Crea una instancia de Vert.x
        vertx.deployVerticle(new RequestVerticle()); // Despliega el verticle que envía una petición
        vertx.deployVerticle(new ResponseVerticle()); // Despliega el verticle que responderá la petición
    }

    // Verticle que realiza una petición (request)
    static class RequestVerticle extends AbstractVerticle {

        private static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);

        // Dirección del Event Bus donde se enviará el mensaje
        static final String ADDRESS = "mi.peticion.direccion";

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            startPromise.complete(); // Indica que el verticle se inició correctamente

            var eventBus = vertx.eventBus(); // Obtiene el Event Bus
            final String message = "Hola Mundo"; // Mensaje a enviar

            LOG.debug("Sending:{}", message); // Registra el envío

            // Envía una petición a la dirección especificada
            // 'request' espera una respuesta (modo request-response)
            eventBus.<String>request(ADDRESS, message, reply -> {
                // Cuando llegue la respuesta, se ejecuta este callback
                LOG.debug("Response: {}", reply.result().body()); // Muestra la respuesta recibida
            });
        }
    }

    // Verticle que recibe la petición y responde
    static class ResponseVerticle extends AbstractVerticle {

        private static final Logger LOG = LoggerFactory.getLogger(ResponseVerticle.class);

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            startPromise.complete(); // Marca el inicio exitoso del verticle

            // Registra un consumidor en el Event Bus para responder a la dirección esperada
            vertx.eventBus().<String>consumer(RequestVerticle.ADDRESS, message -> {
                LOG.debug("Mensaje recibido: {}", message.body()); // Muestra el mensaje recibido

                // Envía una respuesta al emisor
                message.reply("¡Respuesta recibida!");
            });
        }
    }
}
