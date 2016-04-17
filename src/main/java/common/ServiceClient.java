package common;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dosapati on 4/17/16.
 */
public class ServiceClient {

  public static void main(String[] args) {

    ServiceClient sc = new ServiceClient();

    //getBrewNews();
    //sc.getResponseFor("https://access.alchemyapi.com/calls/data/GetNews?apikey=5255afd06e416044d1f4f7c912f0810ebb7711b7&return=enriched.url.title,enriched.url.url,enriched.url.author,enriched.url.publicationDate&start=1460160000&end=1460847600&q.enriched.url.cleanedTitle=Beer&q.enriched.url.enrichedTitle.docSentiment.type=positive&count=25&outputMode=json");

    //getResponseFor("https://www.delivery.com/api/data/search?section=beer&address=100%205th%20Avenue%2C%20New%20York%2C%20NY%2C%20United%20States&order_type=delivery&order_time=ASAP&limit_if_segmented=20&search_type=Alcohol&iso=true&client_id=MDlkMzY3Nzg3MjU1ZjRkNmY4OWZjNDA0NjBjMTI0MWZl");

    getBeerCategories();

  }

  static Gson gson = new GsonBuilder().serializeNulls().create();
  static final String KEY = "5255afd06e416044d1f4f7c912f0810ebb7711b7";
  //static final String KEY = "264904cbb3c8671d11d1f45b2610b6d5fa4ebf35";

  //public static List<Map<String,Map>> categories = new ArrayList<>();

  public static Map<Map, List<Map>> getBeerCategories(){

    Map<Map,List<Map>> categories = new HashMap<>();

    buildProd1(categories);


    buildProd2(categories);

    try {
      final String categoryResponse = "{\n" +
        "  \"data\": {\n" +
        "    \"categories\": [\n" +
        "      {\n" +
        "        \"name\": \"Beer\",\n" +
        "        \"value\": \"beer\",\n" +
        "        \"key\": \"section\",\n" +
        "        \"is_selected\": true,\n" +
        "        \"count\": 151,\n" +
        "        \"children\": [\n" +
        "          {\n" +
        "            \"name\": \"Other Ale\",\n" +
        "            \"value\": \"other-ale\",\n" +
        "            \"image\": \"https://s3.amazonaws.com/alcohol-images/other-ale.jpg\",\n" +
        "            \"key\": \"subsection\",\n" +
        "            \"is_selected\": false,\n" +
        "            \"children\": [],\n" +
        "            \"count\": 82,\n" +
        "            \"product_ids\": [\n" +
        "              76,\n" +
        "              20,\n" +
        "              122,\n" +
        "              75,\n" +
        "              14,\n" +
        "              41,\n" +
        "              10,\n" +
        "              34,\n" +
        "              98,\n" +
        "              16,\n" +
        "              19,\n" +
        "              102,\n" +
        "              50,\n" +
        "              44,\n" +
        "              12,\n" +
        "              52,\n" +
        "              111,\n" +
        "              140,\n" +
        "              106,\n" +
        "              114\n" +
        "            ]\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"Lager\",\n" +
        "            \"value\": \"lager\",\n" +
        "            \"image\": \"https://s3.amazonaws.com/alcohol-images/lager.jpg\",\n" +
        "            \"key\": \"subsection\",\n" +
        "            \"is_selected\": false,\n" +
        "            \"children\": [],\n" +
        "            \"count\": 44,\n" +
        "            \"product_ids\": [\n" +
        "              127,\n" +
        "              126,\n" +
        "              97,\n" +
        "              143,\n" +
        "              43,\n" +
        "              129,\n" +
        "              32,\n" +
        "              103,\n" +
        "              133,\n" +
        "              83,\n" +
        "              85,\n" +
        "              95,\n" +
        "              22,\n" +
        "              121,\n" +
        "              136,\n" +
        "              80,\n" +
        "              90,\n" +
        "              130,\n" +
        "              116,\n" +
        "              87\n" +
        "            ]\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"Pale Ale\",\n" +
        "            \"value\": \"pale-ale\",\n" +
        "            \"image\": \"https://s3.amazonaws.com/alcohol-images/pale-ale.jpg\",\n" +
        "            \"key\": \"subsection\",\n" +
        "            \"is_selected\": false,\n" +
        "            \"children\": [],\n" +
        "            \"count\": 42,\n" +
        "            \"product_ids\": [\n" +
        "              20,\n" +
        "              122,\n" +
        "              75,\n" +
        "              10,\n" +
        "              34,\n" +
        "              19,\n" +
        "              102,\n" +
        "              44,\n" +
        "              12,\n" +
        "              11,\n" +
        "              111,\n" +
        "              140,\n" +
        "              114,\n" +
        "              109,\n" +
        "              65,\n" +
        "              31,\n" +
        "              47,\n" +
        "              151,\n" +
        "              4,\n" +
        "              45\n" +
        "            ]\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"Wheat\",\n" +
        "            \"value\": \"wheat\",\n" +
        "            \"image\": \"https://s3.amazonaws.com/alcohol-images/wheat.jpg\",\n" +
        "            \"key\": \"subsection\",\n" +
        "            \"is_selected\": false,\n" +
        "            \"children\": [],\n" +
        "            \"count\": 13,\n" +
        "            \"product_ids\": [\n" +
        "              127,\n" +
        "              152,\n" +
        "              1,\n" +
        "              134,\n" +
        "              149,\n" +
        "              53,\n" +
        "              128,\n" +
        "              123,\n" +
        "              73,\n" +
        "              147,\n" +
        "              115,\n" +
        "              27,\n" +
        "              92\n" +
        "            ]\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"Light\",\n" +
        "            \"value\": \"light\",\n" +
        "            \"image\": \"https://s3.amazonaws.com/alcohol-images/light.jpg\",\n" +
        "            \"key\": \"subsection\",\n" +
        "            \"is_selected\": false,\n" +
        "            \"children\": [],\n" +
        "            \"count\": 10,\n" +
        "            \"product_ids\": [\n" +
        "              126,\n" +
        "              129,\n" +
        "              103,\n" +
        "              83,\n" +
        "              80,\n" +
        "              116,\n" +
        "              118,\n" +
        "              71,\n" +
        "              9,\n" +
        "              89\n" +
        "            ]\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"Cider\",\n" +
        "            \"value\": \"cider\",\n" +
        "            \"image\": \"https://s3.amazonaws.com/alcohol-images/cider.jpg\",\n" +
        "            \"key\": \"subsection\",\n" +
        "            \"is_selected\": false,\n" +
        "            \"children\": [],\n" +
        "            \"count\": 8,\n" +
        "            \"product_ids\": [\n" +
        "              113,\n" +
        "              79,\n" +
        "              72,\n" +
        "              62,\n" +
        "              60,\n" +
        "              86,\n" +
        "              142,\n" +
        "              38\n" +
        "            ]\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"Stout\",\n" +
        "            \"value\": \"stout\",\n" +
        "            \"image\": \"https://s3.amazonaws.com/alcohol-images/stout.jpg\",\n" +
        "            \"key\": \"subsection\",\n" +
        "            \"is_selected\": false,\n" +
        "            \"children\": [],\n" +
        "            \"count\": 6,\n" +
        "            \"product_ids\": [\n" +
        "              41,\n" +
        "              93,\n" +
        "              8,\n" +
        "              69,\n" +
        "              0,\n" +
        "              119\n" +
        "            ]\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"Amber Ale\",\n" +
        "            \"value\": \"amber-ale\",\n" +
        "            \"image\": \"https://s3.amazonaws.com/alcohol-images/amber-ale.jpg\",\n" +
        "            \"key\": \"subsection\",\n" +
        "            \"is_selected\": false,\n" +
        "            \"children\": [],\n" +
        "            \"count\": 4,\n" +
        "            \"product_ids\": [\n" +
        "              76,\n" +
        "              14,\n" +
        "              98,\n" +
        "              17\n" +
        "            ]\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"Ale\",\n" +
        "            \"value\": \"ale\",\n" +
        "            \"image\": \"https://s3.amazonaws.com/alcohol-images/ale.jpg\",\n" +
        "            \"key\": \"subsection\",\n" +
        "            \"is_selected\": false,\n" +
        "            \"children\": [],\n" +
        "            \"count\": 2,\n" +
        "            \"product_ids\": [\n" +
        "              123,\n" +
        "              147\n" +
        "            ]\n" +
        "          },\n" +
        "          {\n" +
        "            \"name\": \"Other\",\n" +
        "            \"value\": \"other\",\n" +
        "            \"image\": \"https://s3.amazonaws.com/alcohol-images/other.jpg\",\n" +
        "            \"key\": \"subsection\",\n" +
        "            \"is_selected\": false,\n" +
        "            \"children\": [],\n" +
        "            \"count\": 3,\n" +
        "            \"product_ids\": [\n" +
        "              131,\n" +
        "              107,\n" +
        "              91\n" +
        "            ]\n" +
        "          }\n" +
        "        ],\n" +
        "        \"product_ids\": [\n" +
        "          76,\n" +
        "          20,\n" +
        "          122,\n" +
        "          75,\n" +
        "          14,\n" +
        "          41,\n" +
        "          10,\n" +
        "          34,\n" +
        "          98,\n" +
        "          16,\n" +
        "          19,\n" +
        "          102,\n" +
        "          50,\n" +
        "          44,\n" +
        "          12,\n" +
        "          52,\n" +
        "          111,\n" +
        "          140,\n" +
        "          106,\n" +
        "          114,\n" +
        "          93,\n" +
        "          8,\n" +
        "          69,\n" +
        "          0,\n" +
        "          119,\n" +
        "          127,\n" +
        "          152,\n" +
        "          1,\n" +
        "          134,\n" +
        "          149,\n" +
        "          53,\n" +
        "          128,\n" +
        "          123,\n" +
        "          73,\n" +
        "          147,\n" +
        "          115,\n" +
        "          27,\n" +
        "          92,\n" +
        "          11,\n" +
        "          109,\n" +
        "          65,\n" +
        "          31,\n" +
        "          47,\n" +
        "          151,\n" +
        "          4,\n" +
        "          45,\n" +
        "          126,\n" +
        "          97,\n" +
        "          143,\n" +
        "          43,\n" +
        "          129,\n" +
        "          32,\n" +
        "          103,\n" +
        "          133,\n" +
        "          83,\n" +
        "          85,\n" +
        "          95,\n" +
        "          22,\n" +
        "          121,\n" +
        "          136,\n" +
        "          80,\n" +
        "          90,\n" +
        "          130,\n" +
        "          116,\n" +
        "          87,\n" +
        "          118,\n" +
        "          71,\n" +
        "          9,\n" +
        "          89,\n" +
        "          94,\n" +
        "          25,\n" +
        "          29,\n" +
        "          139,\n" +
        "          100,\n" +
        "          13,\n" +
        "          59,\n" +
        "          57,\n" +
        "          137,\n" +
        "          84,\n" +
        "          138,\n" +
        "          40,\n" +
        "          63,\n" +
        "          70,\n" +
        "          148,\n" +
        "          105,\n" +
        "          17,\n" +
        "          113,\n" +
        "          79,\n" +
        "          72,\n" +
        "          62,\n" +
        "          60,\n" +
        "          86,\n" +
        "          142,\n" +
        "          38,\n" +
        "          131,\n" +
        "          107,\n" +
        "          91\n" +
        "        ]\n" +
        "      }\n" +
        "    ],\n"
        ;//getResponseFor("https://www.delivery.com/api/data/search?section=beer&address=100%205th%20Avenue%2C%20New%20York%2C%20NY%2C%20United%20States&order_type=delivery&order_time=ASAP&limit_if_segmented=20&search_type=Alcohol&iso=true&client_id=MDlkMzY3Nzg3MjU1ZjRkNmY4OWZjNDA0NjBjMTI0MWZl");

      System.out.println("categoryResponse = " + categoryResponse);

    }catch (Exception e){
      e.printStackTrace();
    }
    return categories;

    /*if(categoryResponse != null) {
      Map<String, Object> resultMap = gson.fromJson(categoryResponse, Map.class);

      Map<String, Object> result = (Map<String, Object>) resultMap.get("data");
      List<Map<String, Object>> result1 = (List<Map<String, Object>>) result.get("categories");
      *//*if(StringUtils.equalsIgnoreCase((String)result1.get("value"),"beer")){

      }*//*
      for (Map<String, Object> map : result1) {
        if(StringUtils.equalsIgnoreCase((String)map.get("value"),"beer")){
          List<Map<String, Object>> result2 = (List<Map<String, Object>>) map.get("children");

          for (Map<String, Object> objectMap : result2) {
            final String catName = (String) objectMap.get("name");
            System.out.println("objectMap = " + catName);
            if(StringUtils.equalsIgnoreCase("light",catName)){
              List<Map> results = new ArrayList<>();
              Map<String,String>  c1 = new HashMap<>();
              c1.put("product_name","Amstel Light");
              c1.put("product_price","6 Pack $18.25");
              c1.put("product_price_num","18.25");


              results.add(c1);

              Map<String,String>  c2 = new HashMap<>();
              c2.put("product_name","Corona Light");
              c2.put("product_price","6 Pack $21.25");
              c2.put("product_price_num","21.25");

              //Corona Light

              results.add(c2);
              categories.put(objectMap,results);
            }
            if(StringUtils.equalsIgnoreCase("ale",catName)){
              List<Map> results = new ArrayList<>();
              Map<String,String>  c1 = new HashMap<>();
              c1.put("product_name","Abita Amber");
              c1.put("product_price","6 Pack $9.25");
              c1.put("product_price_num","9.25");

              results.add(c1);

              Map<String,String>  c2 = new HashMap<>();
              c2.put("product_name","Ballast Point Grapefruit Sculpin");
              c2.put("product_price","6 Pack $7.85");
              c2.put("product_price_num","7.85");


              results.add(c2);
              categories.put(objectMap,results);
            }

          }

        }
      }
    }

    return categories;*/
  }

  private static void buildProd2(Map<Map, List<Map>> categories) {
    Map<String,String> cat2 = new HashMap<>();
    /**
     * "name": "Light",
     "value": "light",
     "image": "https://s3.amazonaws.com/alcohol-images/light.jpg",
     */
    cat2.put("name","ale");
    cat2.put("image","https://s3.amazonaws.com/alcohol-images/other-ale.jpg");

    List<Map> results = new ArrayList<>();
    Map<String,String>  c1 = new HashMap<>();
    c1.put("product_name","Abita Amber");
    c1.put("product_price","6 Pack $9.25");
    c1.put("product_price_num","9.25");

    results.add(c1);

    Map<String,String>  c2 = new HashMap<>();
    c2.put("product_name","Ballast Point Grapefruit Sculpin");
    c2.put("product_price","6 Pack $7.85");
    c2.put("product_price_num","7.85");


    results.add(c2);
    categories.put(cat2,results);
  }

  private static void buildProd1(Map<Map, List<Map>> categories) {
    Map<String,String> cat1 = new HashMap<>();
    /**
     * "name": "Light",
     "value": "light",
     "image": "https://s3.amazonaws.com/alcohol-images/light.jpg",
     */
    cat1.put("name","light");
    cat1.put("image","https://s3.amazonaws.com/alcohol-images/light.jpg");

    List<Map> results = new ArrayList<>();
    Map<String,String>  c1 = new HashMap<>();
    c1.put("product_name","Amstel Light");
    c1.put("product_price","6 Pack $18.25");
    c1.put("product_price_num","18.25");


    results.add(c1);

    Map<String,String>  c2 = new HashMap<>();
    c2.put("product_name","Corona Light");
    c2.put("product_price","6 Pack $21.25");
    c2.put("product_price_num","21.25");

    //Corona Light

    results.add(c2);
    categories.put(cat1,results);
  }

  public static List<String> getBrewNews() {
    System.out.println("Calling -------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:          getBrewNews");
    List<String> newsList = new ArrayList<>();
    try {
      String serviceResponse = getResponseFor("https://access.alchemyapi.com/calls/data/GetNews?apikey=" + KEY + "&return=enriched.url.title,enriched.url.url,enriched.url.author,enriched.url.publicationDate&start=1460160000&end=1460847600&q.enriched.url.cleanedTitle=Beer&q.enriched.url.enrichedTitle.docSentiment.type=positive&count=25&outputMode=json");
      if (serviceResponse != null) {
        Map<String, Object> resultMap = gson.fromJson(serviceResponse, Map.class);

        Map<String, Object> result = (Map<String, Object>) resultMap.get("result");
        List<Map<String, Object>> result1 = (List<Map<String, Object>>) result.get("docs");

        for (Map<String, Object> map : result1) {
          //System.out.println(map);
          final Map<String, Object> source = (Map<String, Object>) map.get("source");
          final Map<String, Object> enriched = (Map<String, Object>) source.get("enriched");
          final Map<String, Object> urlInfo = (Map<String, Object>) enriched.get("url");

          //System.out.println("enriched = " + enriched.keySet() + " -->" + source.keySet());
          final String title = (String) urlInfo.get("title");
          newsList.add(title);
          System.out.println("map = " + title);
        }
        //System.out.println("resultMap = " + result.get("docs"));
      }
      if (newsList.size() > 2) {
        newsList = newsList.subList(0, 2);
      }
    }catch (Exception e){
      newsList.add("Cheers! Portland ranked Best Beer City in the World");
      newsList.add("Craft Beer Boom: 16 breweries seeking to start up in NH's expanding craft brewing scene.");

      newsList.add("Milwaukee Beer Week - Release The Dragons! at Draft and Vessel in Milwaukee, WI");

      newsList.add("Catherine Hall All-Stars hunt third straight win - Basketball");

    }
    return newsList;
  }

  public static String getResponseFor(String url) {
    try {

      Client client = Client.create();

      WebResource webResource = client
        .resource(url);

      com.sun.jersey.api.client.ClientResponse response = webResource.accept("application/json")
        .get(ClientResponse.class);

      if (response.getStatus() != 200) {
        throw new RuntimeException("Failed : HTTP error code : "
          + response.getStatus());
      }

      String output = response.getEntity(String.class);

      //System.out.println("Output from Server .... \n");
      System.out.println(output);

      return output;

    } catch (Exception e) {

      e.printStackTrace();

    }

    return null;

  }
}
