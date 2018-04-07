package com.bluehook;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/")
  public String helloWorld() {
    return "Hello from Spring!";
  }

  @GetMapping("/vehicles")
  public String getIndividualParams(@RequestParam String year, @RequestParam String doors) {
    return String.format("Year : %s, Doors : %s", year, doors);
  }

}
