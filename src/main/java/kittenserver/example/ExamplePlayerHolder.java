package kittenserver.example;

import kittenserver.required.AbstractPlayerHolder;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * In order to inject your own implementation of {@link AbstractPlayerHolder},
 * use <b>{@link Primary}</b> annotation.
 */
@Service
public class ExamplePlayerHolder extends AbstractPlayerHolder<ExamplePlayer> {

}
