package com.bluehook;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/")
  public String helloWorld() {
    return "Hello from Spring!";
  }

  @GetMapping("/math/pi")
  public String pi() {
    return "3.14";
  }

}
