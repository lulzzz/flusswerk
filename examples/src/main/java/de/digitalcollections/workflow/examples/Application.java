package de.digitalcollections.workflow.examples;

import de.digitalcollections.workflow.engine.Engine;
import de.digitalcollections.workflow.engine.exceptions.WorkflowSetupException;
import de.digitalcollections.workflow.engine.flow.Flow;
import de.digitalcollections.workflow.engine.flow.FlowBuilder;
import de.digitalcollections.workflow.engine.messagebroker.MessageBroker;
import de.digitalcollections.workflow.engine.messagebroker.MessageBrokerBuilder;
import de.digitalcollections.workflow.engine.model.DefaultMessage;
import de.digitalcollections.workflow.engine.model.Message;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  private void run() throws IOException {
    MessageBroker messageBroker = new MessageBrokerBuilder()
        .hostName("localhost")
        .username("guest")
        .password("guest")
        .exchange("workflow")
        .deadLetterExchange("workflow.dlx")
        .readFrom("someInputQueue")
        .writeTo("someOutputQueue")
        .build();

    Flow flow = new FlowBuilder<DefaultMessage, String, String>()
        .read(Message::getType)
        .transform(new UppercaseTransformer(true))
        .write(DefaultMessage::withType)
        .build();

    Engine engine = new Engine(messageBroker, flow);
    messageBroker.send("someInputQueue", DefaultMessage.withType("lowercase-text").put("text", "Shibuyara"));
    engine.start();
  }

  public static void main(String[] args) throws IOException, WorkflowSetupException {
    Application application = new Application();
    application.run();
  }

}
