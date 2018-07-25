package org.kittenboot.example;

import org.kittenboot.kittenserver.required.KittenMarker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
  // This line tells Spring to search for @Beans and @Configuration in package containing KittenMarker.class,
  // which absolutely necessary for KittenServer to work.
  KittenMarker.class,
  // After telling Spring to search for components in KittenServer package, you need to remind him
  // about your own beans as well
  Application.class
})
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
