import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
 
/*
 * Class that reads in texts and outputs it as an audio
 */
public class TextReader {

	private StringBuilder text;
	private URL url;
    //Make Weather and News variable static??
	private final String WEATHER;
	private final String NEWS;
	private BufferedReader br;
	private String websiteText;
	private Calendar calendar;
	private static SimpleDateFormat timeFormat;
	
	//Constructor for the TextReader class
	public TextReader() {
		calendar = Calendar.getInstance();
//        http://api.openweathermap.org/data/2.5/forecast/daily?q=Austin,TX&units=imperial&APPID=452f600cab86d8a9bc0d071014688aa3
        //Weather for the week
		WEATHER = "http://api.openweathermap.org/data/2.5/weather?q=Austin,TX&units=imperial" +
				"&APPID=452f600cab86d8a9bc0d071014688aa3";
		NEWS = "http://feeds.bbci.co.uk/news/world/rss.xml";
		websiteText = "";
		text = new StringBuilder();
		calculateText();
		
	}
	
	public String getCurrentDate() {
		calendar = Calendar.getInstance();
		timeFormat = new SimpleDateFormat("EEEE, MMMM d YYYY. ");
		String time = timeFormat.format(calendar.getTime());
		return time;
	}
	
	public String getCurrentTime() {
		calendar = Calendar.getInstance();
		timeFormat = new SimpleDateFormat("hh:mm a. ");
    	String time = timeFormat.format(calendar.getTime());
    	return time;
    }
	
	/*
	 * Gets the current weather (Temp, humidity, and chance of rain) 
	 * and returns it as a string. The information updates every two hours.
	 */
	private String getWeather() {
		StringBuilder message = new StringBuilder();
		
		try {
			message.append("Your weather forecast for today is ");
			url = new URL(WEATHER);
			InputStream is = url.openStream();
			JsonReader rdr = Json.createReader(is);
			
			JsonObject obj = rdr.readObject();
			JsonArray weather = obj.getJsonArray("weather");
			JsonObject arrObject = weather.getJsonObject(0);

			message.append(arrObject.getString("description"));
			message.append(". The temperature is ");
			JsonObject main = obj.getJsonObject("main");

			message.append((int) main.getJsonNumber("temp").doubleValue());
			message.append(" degrees Fahrenheit with ");
			message.append(main.getJsonNumber("humidity").intValue());
			message.append("% humidity. ");
			//Check to see if the rain object information is online
			if(main.getJsonObject("rain") != null){
				main = obj.getJsonObject("rain");
				JsonNumber number = main.getJsonNumber("3h");
				int num = (int) (number.doubleValue() * 100);
                message.append("There is a ");
				message.append(num);
                message.append("% chance of rain. ");
			}
			rdr.close();
			is.close();
		}
		catch(Exception e) {
			message = new StringBuilder();
			message.append("Sorry, the weather forecast is unavailable. ");
		}
		return message.toString();
	}
	
	/*
	 * Retrieves the news that is happening today, It gets BBC news and
	 * returns it as a String
	 */
	private String getNews() {
		StringBuilder message = new StringBuilder();
		try{
			message.append("Now for today's news from the world section of the BBC news.");
			url = new URL(NEWS);
			InputStream is = url.openStream();
		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = (Document)builder.parse(is);
			Element docEle = document.getDocumentElement();
			//Get the NodeLists of the titles and descriptions
			NodeList title = docEle.getElementsByTagName("title");
			NodeList description = docEle.getElementsByTagName("description");
			
			//Gets the first five news stories
			for(int i = 2; i <= 6;i++) {
				Element titleEle = (Element)title.item(i);
				Element descriptionEle = (Element)description.item(i-1);
				
				String titleValue = titleEle.getFirstChild().getNodeValue();
				String descriptionValue = descriptionEle.getFirstChild().getNodeValue();
				
				//Single append statements to avoid String concatenation for better efficiency
				message.append(" ");
				message.append(titleValue);
				message.append(".");
				message.append(" ");
				message.append(descriptionValue);
			}
			message.append(" ");
		}
		catch(Exception e) {
			message = new StringBuilder();
			message.append("The news is currently unavailable. ");
			e.printStackTrace();
		}
		return message.toString();
	}
	
	//Getter for text
	public StringBuilder getText() {
        return text;
	}
	
	//Sets the text
	public void calculateText() {
        text.append(getCurrentTime().substring(6,8).equals("AM") ? "Good Morning " :
                    "Good Afternoon ");
		text.append("Gilbert, Today is ");
		text.append(getCurrentDate());
		text.append("The time is ");
		text.append(getCurrentTime());
		text.append(getWeather());
		text.append(getNews());
        text.append("The alarm will not turn off unless you solve the Rubik's Cube. Have a nice day.");
		System.out.println(text);

	}
	
}
