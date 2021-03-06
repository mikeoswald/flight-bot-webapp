package com.flight.bot.flightwebapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
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

    @Autowired
    Message message;

    @GetMapping("/main")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "index";
    }

    @RequestMapping(value = "/register-bot", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String createNewUser(@RequestBody Bot bot) throws IOException, MessagingException {
        //first check that the same combination isn't in there
        boolean isDuplicate = flightBotDao.isDuplicateFlightBotDetails(bot);
        if(isDuplicate){
            return "There is already an entry for that Flight Bot";
        } else {
            boolean status = flightBotDao.addNewFlightBotDetails(bot);
            if(status){
                //send email
                confirmationEmail(bot);
                return "Your Flight Bot has been saved. You should receive a confirmation email at: "+ bot.getEmail() +" shortly.";
            } else
                return "Something went wrong with saving your Flight Bot. Please try again.";
        }
    }

    private void confirmationEmail(Bot bot) throws MessagingException {
        message.setFrom(new InternetAddress("michaelmoswald@gmail.com"));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(bot.getEmail()));
        message.setSubject("New Flight Bot Created");
        message.setText("Dear ,"+bot.getEmail()
                + "\n\n Thanks for registering a new Flight Bot. Below are the details \n\n"+
        "origin: "+bot.getOrigin()
        +" desination: "+bot.getDestination()
        + " trip duration: "+bot.getLength()
        + " season: "+ bot.getSeason()+"\n\n Flight bot will check the flights for your and send " +
                "you an email when a really good deal is found. Stay tuned! \n\n Thanks, Flight Bot");
        Transport.send(message);
    }
}