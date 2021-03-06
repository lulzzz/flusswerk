package de.digitalcollections.flusswerk.engine.model;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JobTest {

  private static final Message<String> SOME_MESSAGE = new DefaultMessage("Hey!");

  private static final Function<Message, String> DUMMY_READER = Message<String>::getId;

  private static final Function<String, String> DUMMY_TRANSFORMER = Function.identity();

  private static final Function<String, Message> DUMMY_WRITER = DefaultMessage::new;


  class CheckIfCalled<R, W> implements Function<R, W> {

    boolean called = false;

    @Override
    public W apply(R r) {
      called = true;
      return null;
    }
  }

  class CheckIfWritten<R> implements Function<R, Collection<Message>> {

    boolean called = false;

    @Override
    public Collection<Message> apply(R r) {
      called = true;
      return null;
    }
  }

  @BeforeEach
  void setUp() {
  }

  @Test
  @DisplayName("Read should call the read function")
  void read() {
    CheckIfCalled<Message, String> reader = new CheckIfCalled<>();
    Job<Message, String, String> job = new Job<>(SOME_MESSAGE);
    job.read(reader);
    assertThat(reader.called).isTrue();
  }

  @Test
  void transform() {
    CheckIfCalled<String, String> transformer = new CheckIfCalled<>();
    Job<Message, String, String> job = new Job<>(SOME_MESSAGE);
    job.transform(transformer);
    assertThat(transformer.called).isTrue();
  }

  @Test
  void write() {
    CheckIfWritten<String> writer = new CheckIfWritten<>();
    Job<Message, String, String> job = new Job<>(SOME_MESSAGE);
    job.write(writer);
    assertThat(writer.called).isTrue();
  }

  @Test
  @DisplayName("Read, Transform, Write should pass values along")
  void readTransformWriteShouldPassValues() {
    String message = "Jolene, Jolene, Jolene, Jolene";
    Job<DefaultMessage, String, String> job = new Job<>(new DefaultMessage().put("message", message));
    job.read(m -> m.get("message"));
    job.transform(String::toUpperCase);
    job.write((Function<String, Collection<Message>>) s -> Collections.singleton(new DefaultMessage().put("message", s)));
    assertThat(job.getResult()).allSatisfy(
        result -> assertThat(assertThat(((DefaultMessage) result).get("message")).isEqualTo(message.toUpperCase()))
    );
  }

  @Test
  void getMessage() {
    Message message = new DefaultMessage("Wuthering Heights");
    Job<Message, String, String> job = new Job<>(message);
    assertThat(job.getMessage()).isEqualTo(message);
  }

}
