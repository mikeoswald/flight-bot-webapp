package com.flight.bot.flightwebapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlightFinder {

    @Scheduled(fixedRate = 14400000, initialDelay = 14400000)
    public void batchFindFlights() {
        //first query and save all Bots into list,
        //In this logic you need to see if there hasn't been an average set yet.
        //If not, then start adding, if it is less than five, just add the number
        //if it is at 5 numbers then sort it to the lowest and kick out the smallest
        //Then check if the current cheapest flight price is lower than the average of the 5. (7% lower)
        //If it is, then email the user the top 3 cheapest flights.
        //
        //query and logic here:

        //2. use list from database to make calls
    }

    private List<Bot> getBotsFromDb(){
        List <Bot> botList = new ArrayList<>();
        PGobject jsonObject = new PGobject();
        jsonObject.setType("jsonb");
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString ="";
        try {
            jsonInString = mapper.writeValueAsString(bot);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            Connection con = dataSource.getConnection();
            String query = "Select useruniqueid from Positions where UserUniqueId =? " +
                    "AND PositionDetails = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, bot.getEmail());//need to query customers to get this, with (email)
            jsonObject.setValue(jsonInString);
            pstmt.setObject(2, jsonObject);
            ResultSet rs = pstmt.executeQuery();
            con.close();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return botList;
    }
    private static final Logger log = LoggerFactory.getLogger(FlightFinder.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

/*    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }*/
}
