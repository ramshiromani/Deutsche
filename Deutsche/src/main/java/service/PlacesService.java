package main.java.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


import main.java.vo.Place;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class PlacesService {

	private static Logger log = Logger.getLogger(PlacesService.class);
	// Google API Key 
	private static final String API_KEY = "AIzaSyAFAoAMnx7yQw_Cy8xQMRIFuWRhmFRe3M8"; 
	

	// Google Places serach url's
	private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
	
	
	
	public List<Place> findPlaces(String latitude, String longitude,String placeSpacification) {

		String urlString = makeUrl(latitude, longitude, placeSpacification);
		

		try {
			String json = getJSON(urlString);
			log.debug("Result is --"+json); 
			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("results");

			ArrayList<Place> arrayList = new ArrayList<Place>();
			for (int i = 0; i < array.length(); i++) {
				try {
					Place place =Place.jsonToPlaceReference((JSONObject) array.get(i));

					arrayList.add(place);
				} catch (Exception e) {
				}
			}
			return arrayList;
		} catch (JSONException ex) {

		}
		return null;
	}

	private String makeUrl(String latitude, String longitude,String place) {
        StringBuilder urlString = new StringBuilder(PLACES_SEARCH_URL);

       if (place.equals("")) {
               urlString.append("&location=");
               urlString.append(latitude);
               urlString.append(","); 
               urlString.append(longitude);
               urlString.append("&radius=1000"); 
               urlString.append("&sensor=false&key=" + API_KEY);
               System.out.println(urlString);
       } else {
               urlString.append("&location=");
               urlString.append(latitude);
               urlString.append(",");
               urlString.append(longitude);
               urlString.append("&radius=1000");
               urlString.append("&types="+place);
               urlString.append("&sensor=false&key=" + API_KEY);
       }
       log.debug("URL is --"+urlString);
       return urlString.toString();
	}

	protected String getJSON(String url) {
		return getUrlContents(url);
	}

	private String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();

		try {
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()), 8);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}
}
