package de.digitalcollections.flusswerk.engine.reporting;

import de.digitalcollections.flusswerk.engine.exceptions.FinallyFailedProcessException;
import de.digitalcollections.flusswerk.engine.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultProcessReport implements ProcessReport{

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProcessReport.class);

  @Override
  public void reportSuccess(Message message) {
    LOGGER.info("Successfully processed message: {}", message.getEnvelope().getBody());
  }

  @Override
  public void reportFail(Message message, FinallyFailedProcessException e) {
    LOGGER.error("Failed message because of processing error: {}", message.getEnvelope().getBody(), e);
  }

  @Override
  public void reportFailAfterMaxRetries(Message message, Exception e) {
    LOGGER.error("Failed message after max number of retries because of processing error: {}", message.getEnvelope().getBody(), e);
  }

  @Override
  public void reportReject(Message message, Exception e) {
    LOGGER.warn("Rejected message because of processing error: {}", message.getEnvelope().getBody(), e);
  }
}
