// Paquete donde se encuentra la clase
package cl.ucn.publishsubscribe;

// Importaciones necesarias de Vert.x para crear vértices (verticles) y desplegarlos
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

// Importación para logs
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Para controlar el tiempo de los envíos periódicos
import java.time.Duration;

public class PublishSubscribe {

    public static void main(String[] args) {
        // Se crea una instancia del core de Vert.x
        var vertx = Vertx.vertx();

        // Se despliega un verticle que actuará como publicador
        vertx.deployVerticle(new Publish());

        // Se despliega un verticle que actuará como un suscriptor
        vertx.deployVerticle(new Subscriber1());

        // Se despliegan dos instancias del verticle Subscriber2
        vertx.deployVerticle(Subscriber2.class.getName(), new DeploymentOptions().setInstances(2));
        // Esto permite probar cómo se comporta el EventBus con múltiples consumidores del mismo tipo
    }

    // Clase que representa al publicador
    public static class Publish extends AbstractVerticle {

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            startPromise.complete();

            // Envía un mensaje cada 10 segundos a través del EventBus (método publish)
            // A diferencia de send, publish lo envía a todos los consumidores registrados
            vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id ->
                    vertx.eventBus().publish(Publish.class.getName(), "Un mensaje para todos!"));
        }
    }

    // Primer suscriptor
    public static class Subscriber1 extends AbstractVerticle {

        private static final Logger LOG = LoggerFactory.getLogger(Subscriber1.class);

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            // Se registra como consumidor del canal identificado por Publish.class.getName()
            vertx.eventBus().<String>consumer(Publish.class.getName(), message -> {
                LOG.debug("Received message: " + message.body());
            });
        }
    }

    // Segundo tipo de suscriptor, del cual se desplegarán múltiples instancias
    public static class Subscriber2 extends AbstractVerticle {

        private static final Logger LOG = LoggerFactory.getLogger(Subscriber2.class);

        @Override
        public void start(final Promise<Void> startPromise) throws Exception {
            // También se registra como consumidor del canal del publicador
            vertx.eventBus().<String>consumer(Publish.class.getName(), message -> {
                LOG.debug("Received message: " + message.body());
            });
        }
    }
}
