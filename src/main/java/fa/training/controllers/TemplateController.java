package fa.training.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/test-with-menu")
    public String test() {
        return "layout/test-with-menu";
    }
}
