package com.itt.controller;

import com.itt.service.CalendarService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.HttpRetryException;
import java.text.ParseException;

@RestController
public class CalendarController {

    private final CalendarService calendarService;


    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @RequestMapping(value = "/save-calendar", method= RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<String> saveCalendarAction(@RequestBody String newStructure) {
        try {
            this.calendarService.saveAcademicYearFormCalendar(newStructure);
        } catch (ParseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("saved",HttpStatus.OK);
    }

    @RequestMapping(value = "/get-week-by-date", method= RequestMethod.POST)
    public @ResponseBody
    ResponseEntity getWeekByDateAction(@RequestBody String dataAsJson) {
        try {

            BasicDBObject data = (BasicDBObject) JSON.parse(dataAsJson);

            DBObject result = this.calendarService.getWeekNumber(
                    (String)  data.get("date"),
                    (Integer) data.get("year_of_study"),
                    (String)  data.get("specialization")
            );

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (ParseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (HttpRetryException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.valueOf(e.responseCode()));
        }
    }


        @RequestMapping("/get_file")
    public ResponseEntity<String> getFileContent() {
          return ResponseEntity.status(HttpStatus.CONFLICT).body("eee");
    }
}
