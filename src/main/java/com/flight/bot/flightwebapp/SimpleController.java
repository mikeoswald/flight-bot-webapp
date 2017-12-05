package com.flight.bot.flightwebapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    FlightBotDao flightBotDao;

    @GetMapping("/main")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "index";
    }

    @RequestMapping(value = "/register-bot", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String createNewUser(@RequestBody Bot bot) throws IOException {

        // add bot to database
       boolean status = flightBotDao.addNewFlightBotDetails(bot);

        return "Success";
    }
}