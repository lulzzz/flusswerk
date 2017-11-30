package de.digitalcollections.workflow.engine;

import de.digitalcollections.workflow.engine.model.Job;
import de.digitalcollections.workflow.engine.model.Message;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Recipe for the data processing. Every message will be processed by the reader, then the transformer and finally the writer. The transformer can be omitted if <code>R</code> and <code>W</code> are the same.
 * @param <R>
 * @param <W>
 */
public class Flow<R, W> {

  private static final Logger LOGGER = LoggerFactory.getLogger(Flow.class);

  private Function<Message, R> reader;

  private Function<R, W> transformer;

  private Function<W, Message> writer;

  protected Flow(Function<Message, R> reader, Function<R, W> transformer, Function<W, Message> writer) {
    this.reader = reader;
    this.transformer = transformer;
    this.writer = writer;
  }

  public Message process(Message message) {
    Job job = new Job<>(message, reader, transformer, writer);
    if (reader != null) {
      job.read();
    }
    if (transformer != null) {
      job.transform();
    }
    if (writer != null) {
      job.write();
    }
    return job.getResult();
  }

  void setTransformer(Function<R, W> transformer) {
    this.transformer = transformer;
  }

  boolean writesData() {
    return writer != null;
  }

}
