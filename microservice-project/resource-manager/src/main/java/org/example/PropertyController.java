package org.example;

import properties.SpringApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController {

    @Autowired
    SpringApplicationProperties springApplicationProperties;

    @GetMapping("/property")
    public String resource() {
        System.out.println(springApplicationProperties.getName());
        return "Get Property OK";
    }
}