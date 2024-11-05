package org.cosmic.backend.globals.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController implements ErrorController {

  @GetMapping({"/", "/error"})
  public String getIndex() {
    return "index.html";
  }

  @PostMapping({"/", "/error"})
  public String postIndex() {
    return "index.html";
  }
}

