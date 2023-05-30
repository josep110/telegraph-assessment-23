package com.weatherapp.myweatherapp.controller;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;

@Controller
public class WeatherController {

  @Autowired
  WeatherService weatherService;

  @GetMapping("/forecast/{city}")
  public ResponseEntity<CityInfo> forecastByCity(@PathVariable("city") String city) {

    CityInfo ci = weatherService.forecastByCity(city);


    
    return ResponseEntity.ok(ci);
  }

  // TODO: given two city names, compare the length of the daylight hours and return the city with the longest day

  @GetMapping("/daylightcompare/{citya}/{cityb}")
  public ResponseEntity<CityInfo> compareDayLight(@PathVariable("citya") String city_a, @PathVariable("cityb") String city_b){

    try {
      CityInfo ca = weatherService.forecastByCity(city_a);
      CityInfo cb = weatherService.forecastByCity(city_b);

      // calculate daylight hours for each

      long a_daylight = getDaylight(ca);
      long b_daylight = getDaylight(cb);

      if (a_daylight > b_daylight){ return ResponseEntity.ok(ca); }
      return ResponseEntity.ok(cb);
    } 
      catch(HttpClientErrorException e){ return ResponseEntity.of(Optional.empty()); 
    }

  }

  public long getDaylight(CityInfo c){

    String rise_raw = c.currentConditions.sunrise;
    LocalTime rise_lt = LocalTime.parse(rise_raw, DateTimeFormatter.ofPattern("HH:mm:ss"));

    String set_raw = c.currentConditions.sunset;
    LocalTime set_lt = LocalTime.parse(set_raw, DateTimeFormatter.ofPattern("HH:mm:ss"));

    return rise_lt.until(set_lt, ChronoUnit.MINUTES);

  }

  // TODO: given two city names, check which city its currently raining in

  @GetMapping("/raincheck/{citya}/{cityb}")
  public ResponseEntity<String> rainCheck(@PathVariable("citya") String city_a, @PathVariable("cityb") String city_b){

    try {
      CityInfo ca = weatherService.forecastByCity(city_a);
      CityInfo cb = weatherService.forecastByCity(city_b);

      System.out.println(ca);

      boolean raining_a = raining(ca);
      boolean raining_b = raining(cb);

      if (raining_a && raining_b){
        return ResponseEntity.ok("It is raining in both " + city_a + " and " + city_b);
      }
      if (raining_a){ return ResponseEntity.ok("It is raining in " + city_a);}
      if (raining_b){ return ResponseEntity.ok("It is raining in " + city_b);}

      return ResponseEntity.ok("It is not raining in either " + city_a + " or " + city_b);
    } 
      catch(HttpClientErrorException e){ return ResponseEntity.of(Optional.empty()); // Handles exception generated when bad city name given.
    }
  }


  public boolean raining(CityInfo c){

    String conds = c.currentConditions.conditions;
    if (conds.contains("Rain") || conds.contains("rain")){ return true; }
    return false;

  }



}
