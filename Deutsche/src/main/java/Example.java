package main.java;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import main.java.service.LatLng;
import main.java.service.PlacesService;
import main.java.vo.Place;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = PlacesService.class)
@ComponentScan(basePackageClasses = LatLng.class)
public class Example {

    @Autowired
    private PlacesService placesService;

    @Autowired
    private LatLng latLng;
	
	private Map<String, Place> map = new ConcurrentHashMap<>();
	private static Logger log = Logger.getLogger(Example.class);
	@RequestMapping("/Hello")
	String home() {

		return "Hello World test!";
	}

	
	@RequestMapping(value = "/createShop", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Void> createEmployee(@RequestBody Place place)  {
		
		try{
			    log.debug("--createShop Request received-- ");
				String s[]=latLng.getLatLongPositions(place.getId());
				place.setLatitude(Double.valueOf(s[0]));
				place.setLongitude(Double.valueOf(s[1]));
				Place oldPlace=map.get(place.getId());
				if(oldPlace==null){
					log.debug("--New Shop Received-- ");
				   map.put(place.getId(), place);
				  return new ResponseEntity<Void>(HttpStatus.CREATED);
				}else{
					log.debug("--Shop is already exist-- ");
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/getShopDetails/{pincode}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Place getCountries(@PathVariable(value = "pincode") String pincode) {
		log.debug("--getShopDetails Request received with pincode-- "+pincode);
		return map.get(pincode);
	}

	@RequestMapping(value = "/getShopDetails", method = RequestMethod.GET, headers = "Accept=application/json")
	public Place getCountries() {
		Place place = new Place();
		place.setId("488333");
		place.setAddressNumber("pune");
		place.setShopName("Test");
		return place;
	}
	
	@RequestMapping(value = "/getPlaces/{latitude}/{longitude:.+}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Place> getCountries(@PathVariable(value = "latitude") String latitude,@PathVariable(value = "longitude") String longitude) {
		log.debug("--getPlaces Request received with latitude-- "+latitude +"& longitude -"+longitude);
		List<Place> place=placesService.findPlaces(latitude, longitude, "");
		return place;
	}
	
	@RequestMapping(value = "/getPlaces/{latitude}/{longitude:.+}/{placeSpacification}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Place> getCountries(@PathVariable(value = "latitude") String latitude,@PathVariable(value = "longitude") String longitude,@PathVariable(value = "placeSpacification") String placeSpacification) {
		log.debug("--getPlaces Request received with latitude-- "+latitude +"& longitude -"+longitude+"& placeSpacification-"+placeSpacification);
		List<Place> place=placesService.findPlaces(latitude, longitude, placeSpacification);
		return place;
	}

	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(Example.class, args);
	}

}
