package com.flight.bot.flightwebapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;

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
    @ResponseBody
    public ModelAndView createNewUser(String json) throws IOException {
        System.out.println( "BOT NAME YO: "+json);
        ObjectMapper mapper = new ObjectMapper();
        Bot bot = mapper.readValue("{\"name\": \"John1\"}", Bot.class);

        System.out.println(bot.getName()); //John1post

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("successMessage", "User has been registered successfully");
       // modelAndView.addObject("user", new SecurityProperties.User());
        modelAndView.setViewName("registration");

        return modelAndView;
    }
}