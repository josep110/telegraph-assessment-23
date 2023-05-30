package com.weatherapp.myweatherapp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.weatherapp.myweatherapp.controller.WeatherController;
import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;

@SpringBootTest
class MyweatherappApplicationTests {

	@Autowired
	MyweatherappApplication test;

	@Test
	void contextLoads() {
	}

	@Test
	void checkDaylight(){
		
		// comparing london and mumbai
		// as of writing this test, london has more daylight than mumbai, so if working correctly WeatherController.compareDayLight("london","mumbai")
		// should return a response entity for "london".

		WeatherService weatherService = new WeatherService();
		CityInfo clondon = weatherService.forecastByCity("london");

		WeatherController tested = new WeatherController();

		assertEquals(tested.compareDayLight("London", "Mumbai"), ResponseEntity.ok(clondon));
	}

	@Test
	void checkRaincheck(){	

	  // comparing tokyo and madrid
	  // as of writing this test, it is not raining in Tokyo or Madrid,
	  // so if working correctly WeatherController.rainCheck("tokyo","madrid") should return a ResponseEntity with String body "It is not raining in tokyo or madrid".

	  WeatherController tested = new WeatherController();
	  assertEquals(tested.rainCheck("tokyo", "madrid"), ResponseEntity.ok("It is not raining in tokyo or madrid"));

	}
}
