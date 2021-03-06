package de.digitalcollections.flusswerk.examples.spring;

import de.digitalcollections.flusswerk.engine.model.DefaultMessage;
import de.digitalcollections.flusswerk.engine.model.Message;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class StringWriter implements Function<String, Message> {

  @Override
  public DefaultMessage apply(String s) {
    return new DefaultMessage("uppercase-strings").put("text", s);
  }

}
