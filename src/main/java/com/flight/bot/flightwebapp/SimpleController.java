package com.flight.bot.flightwebapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        //first check that the same combination isn't in there
        boolean isDuplicate = flightBotDao.isDuplicateFlightBotDetails(bot);
        if(isDuplicate){
            return "There is already an entry for that Flight Bot";
        } else {
            boolean status = flightBotDao.addNewFlightBotDetails(bot);
            if(status){
                return "Your Flight Bot has been saved. You should receive a confirmation email at: "+ bot.getEmail() +" shortly.";
            } else
                return "Something went wrong with saving your Flight Bot. Please try again.";
        }
    }
}