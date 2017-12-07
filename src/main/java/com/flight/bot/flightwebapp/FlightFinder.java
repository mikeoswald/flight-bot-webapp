package com.flight.bot.flightwebapp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class FlightFinder {

    @Autowired
    DataSource dataSource;

    @Autowired
    Message message;

    @Scheduled(fixedRate = 14400000, initialDelay = 14400000)
    public void batchFindFlights() throws MessagingException {
        //first query and save all Bots into list,
        //In this logic you need to see if there hasn't been an average set yet.
        List<Bot> botList = getBotsFromDb();


        //If not, then start adding, if it is less than five, just add the number
        //if it is at 5 numbers then sort it to the lowest and kick out the smallest
        //Then check if the current cheapest flight price is lower than the average of the 5. (7% lower)
        //If it is, then email the user the top 3 cheapest flights.
        //
        //query and logic here:
        //rest query
        //pretend backend here:
        int randomPrice = ThreadLocalRandom.current().nextInt(470, 550 + 1);
        for(Bot bot: botList){
            if(bot.getPrices()== null){
                bot.setPrices("500");
            } else {
                if(Integer.parseInt(bot.getPrices()) > randomPrice){
                    //set random price as new lowest

                    //percent decrease
                    int originalNum = Integer.parseInt(bot.getPrices());
                    int newNum= randomPrice;
                    int decrease = originalNum - newNum;
                    int percentDecrease = (decrease/originalNum) * 100;
                    bot.setPrices(Integer.toString(randomPrice));
                    if(percentDecrease > 6){
                        //send email
                        sendEmail(bot,Integer.toString(originalNum));
                    }
                }
            }
            //save back to DB
            saveBackToDb(bot);
        }
        //2. use list from database to make calls
    }

    private void saveBackToDb(Bot bot){
        try {
            Connection con = dataSource.getConnection();
            String newPrice = bot.getPrices();
            bot.setPrices(null);
            PGobject jsonObject = new PGobject();
            jsonObject.setType("jsonb");
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString ="";

            try {
                jsonInString = mapper.writeValueAsString(bot);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            String query = "UPDATE Positions SET PositionPrices = ? WHERE PositionDetails = ? AND ActiveBot = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, newPrice);
            jsonObject.setValue(jsonInString);
            pstmt.setObject(2, jsonObject);
            pstmt.setString(3, "yes");
            pstmt.executeUpdate();
            con.close();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void sendEmail(Bot bot, String oldPrice) throws MessagingException {
        message.setFrom(new InternetAddress("michaelmoswald@gmail.com"));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(bot.getEmail()));
        message.setSubject("Flight Bot Found a Cheap Flight!");
        message.setText("Dear, "+bot.getEmail()
                + "\n\n Your Flight Bot found a cheap flight for "+bot.getOrigin()+" to "+bot.getDestination()+
        ". The price is now: "+bot.getPrices()+ ". (Was: "+oldPrice+") That is more than a 10% decrease! \n" +
                "\n See the flight details here: (link)");
        Transport.send(message);
    }

    private List<Bot> getBotsFromDb(){
        List <Bot> botList = new ArrayList<>();
        try {
            Connection con = dataSource.getConnection();
            String query = "Select positiondetails, positionprices from Positions where ActiveBot = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "yes");
            ResultSet rs = pstmt.executeQuery();
            ObjectMapper mapper = new ObjectMapper();
            while (rs.next()) {
                Bot bot = mapper.readValue(rs.getString("positiondetails"), Bot.class);
                bot.setPrices(rs.getString("positionprices"));
                botList.add(bot);
            }
            con.close();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return botList;
    }
}
