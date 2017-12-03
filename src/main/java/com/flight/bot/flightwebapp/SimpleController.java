package com.flight.bot.flightwebapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by Michael on 11/30/2017.
 */
@Controller
public class SimpleController {
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/main")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "index";
    }

    @RequestMapping(value = "/register-bot", method = RequestMethod.POST)
    public ModelAndView createNewUser(@RequestBody Bot bot) {
        System.out.println( "BOT NAME YO: "+bot.getName());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("successMessage", "User has been registered successfully");
        modelAndView.addObject("user", new SecurityProperties.User());
        modelAndView.setViewName("registration");

        return modelAndView;
    }
}