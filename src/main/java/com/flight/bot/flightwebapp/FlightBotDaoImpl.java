package com.flight.bot.flightwebapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

/**
 * Created by Michael on 12/4/2017.
 */
@Component
public class FlightBotDaoImpl implements FlightBotDao {

    @Autowired
    DataSource dataSource;

    @Override
    public boolean addNewFlightBotDetails(Bot bot) {
        boolean result = false;
        PGobject jsonObject = new PGobject();
        jsonObject.setType("json");
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString ="";
        try {
             jsonInString = mapper.writeValueAsString(bot);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            //use below to check if object and id is already in database. And if they have reached their limit.
            //Select * from Positions where UserUniqueId ='UserUniqueId-1' AND PositionDetails = '{"name": "John1"}'
            Connection con = dataSource.getConnection();
            Statement stmt = con.createStatement();
            UUID uuid = UUID.randomUUID();
            String json = "{\"name\": \"John1\"}";
            String query = "INSERT INTO Positions (PositionId,UserUniqueId,PositionDetails)\n" +
                    "VALUES (?,?,?);";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, "mikeoID");//need to query customers to get this, with (email)
            jsonObject.setValue(jsonInString);
            pstmt.setObject(3, jsonObject);
            int i = pstmt.executeUpdate();
            result = i > 0;
            con.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean modFlightBotDetails(Bot bot) {
        return false;
    }
}
