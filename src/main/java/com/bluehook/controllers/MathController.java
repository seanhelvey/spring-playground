package com.bluehook.controllers;

import com.bluehook.models.MathService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MathController {

    @GetMapping("/math/pi")
    public String pi() {
        return "3.14";
    }

    @GetMapping("/math/calculate")
    public Integer calculate(@RequestParam String operation, @RequestParam Integer x, @RequestParam Integer y) {
        return MathService.calculate(operation, x, y);
    }

    @GetMapping("/math/sum")
    public Object calculate(@RequestParam Map queryString) {
        int sum = 0;
        for (Object value : queryString.values()){
            System.out.println(value);
            sum += Integer.valueOf((String) value);
        }
        return sum;
    }
}
