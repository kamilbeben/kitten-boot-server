package org.kittenboot.server.required;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * Marker class mean't to use with
 * {@link ComponentScans#value()} and {@link ComponentScan#basePackageClasses()}
 *
 * @example
 * <code>
 *   @SpringBootApplication
 *   @ComponentScan(basePackageClasses = {
 *     KittenMarker.class, // this very class
 *     Application.class // your main application class
 *   })
 *   public class Application {
 *
 *     public static void main(String[] args) {
 *       SpringApplication.run(Application.class, args);
 *     }
 *   }
 * </code>
 */
public class KittenMarker {
}
