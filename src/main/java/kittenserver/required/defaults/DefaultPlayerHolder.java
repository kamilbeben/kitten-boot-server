package kittenserver.required.defaults;

import kittenserver.required.AbstractPlayerHolder;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * In order to inject your own implementation of {@link AbstractPlayerHolder},
 * use <b>{@link Primary}</b> annotation.
 */
@Service
public class DefaultPlayerHolder extends AbstractPlayerHolder<DefaultPlayer> {

}
