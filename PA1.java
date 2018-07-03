
package pa1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/* 
 * PA1.java 
 * 
 * Version: 
 *     $1.0$ 
 * 
 * Revisions: 
 *     $initial$ 
 *     
 * This program makes API calls to get nearby OCBC bank ATMS, retrieves
 * current weather at ATM location and finds the nearby restaurants to the 
 * ATM location.
 *
 * @author	Vaibhavi Awghate
 */
public class PA1 {

    /**
     * @param args None
     */
    public static void main(String[] args) throws MalformedURLException, IOException, ParseException{
        // TODO code application logic here
        String result = null; 
        
        // function call to get nearby ATM location
        result = getATMLocation();
        System.out.println();
        
        // function call to get current weather conditions at ATM location
        getWeatherConditions(result);
        System.out.println();
        
        // function call to get nearby restaurants to the ATM
        getRestaurantsNearby(result);
        
    }

    private static void getWeatherConditions(String result) throws MalformedURLException, IOException, ParseException{
        String input = new String(result);
        String result_array[];
        result_array = input.split(" ");
        
        // HTTP request
        String Myurl = "http://weathers.co/api.php?city=" + result_array[0];
        URL url = new URL(Myurl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String str;
        StringBuffer stringBuffer = new StringBuffer();
        while((str = br.readLine()) != null){
            stringBuffer.append(str);
            stringBuffer.append("\n");
        }
        
        //JSON parser
        str = stringBuffer.toString();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject)parser.parse(str);
        JSONObject a =  (JSONObject) obj.get("data");
        System.out.println(">>>>>>>Current Weather at ATM location<<<<<<<<");
        System.out.println("temperature: " + a.get("temperature") + " degree celsius");
        System.out.println("Sky conditions: " + a.get("skytext"));
        System.out.println("humidity: " + a.get("humidity"));
        System.out.println("wind: " + a.get("wind"));
    }

    private static String getATMLocation() throws MalformedURLException, IOException, ParseException{
        
        // HTTP request
        URL url = new URL("https://api.ocbc.com:8243/atm_locator/1.0?country=SG");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Authorization", "Bearer a72532b016dd690ca1ff0d6562456a18");
        connection.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String str, result = null;
        StringBuffer stringBuffer = new StringBuffer();
        while((str = br.readLine()) != null){
            stringBuffer.append(str);
            stringBuffer.append("\n");
        }
        
        //JSON parser
        str = stringBuffer.toString();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject)parser.parse(str);
        Object key = null;
        for (Object key_element:obj.keySet()){
            key = key_element;
            break;
        }
        JSONArray msg = (JSONArray) obj.get(key);
        Iterator<?> it = msg.iterator();
        JSONObject a;
        while(it.hasNext()){
            a = (JSONObject)it.next();
            System.out.println("OCBC ATM located nearby is at:");
            System.out.println(a.get("address")+","+a.get("postalCode"));
            result = (a.get("postalCode")).toString();
            break;
        }
        return result;
    }

    private static void getRestaurantsNearby(String result) throws MalformedURLException, IOException, ParseException{
        String input = new String(result);
        String api_id = "AIzaSyCbEE0LvUOpQpaiVGTdbqIeN6VxPtFv_4Q";
        String result_array[];
        result_array = input.split(" ");
        
        // HTTP request
        String Myurl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurants+in+" + result_array[0]+ "&radius=500&key=" + api_id;
        URL url = new URL(Myurl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String str;
        StringBuffer stringBuffer = new StringBuffer();
        while((str = br.readLine()) != null){
            stringBuffer.append(str);
            stringBuffer.append("\n");
        }
        
        // JSON parser
        str = stringBuffer.toString();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject)parser.parse(str);
        JSONArray msg = (JSONArray) obj.get("results");
        Iterator<?> it = msg.iterator();
        JSONObject a;
        System.out.println(">>>>>>>>>Restaurants nearby to ATM and their ratings<<<<<<<<<");
        while(it.hasNext()){
            a = (JSONObject)it.next();
            System.out.println(a.get("name")+"\t"+ a.get("rating"));
            
        }
    }
    
}
