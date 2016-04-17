/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package hearst;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import org.jsoup.Jsoup;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This sample shows how to create 222 a simple speechlet for handling intent requests and managing
 * session interactions.
 */
public class HearstSpeechlet implements Speechlet {
  private static final Logger log = LogManager.getLogger(HearstSpeechlet.class);

    private static final String SAVE_MAGAZINE_KEY = "my_magazine";
    private static final String COLOR_SLOT = "Color";

  private static final String MAGAZINE_SLOT = "magazine";
  private static final String HASHTAG_SLOT = "hashtag";
  private static final String NAME_SLOT = "name";
  public static final String READ_MORE_TEXT = "READ_MORE_TEXT";
  public static final String READ_MORE_TITLE = "READ_MORE_TITLE";


  @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
        init();
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
          session.getSessionId());

        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // Get intent from the request object.
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        log.info("intentName = " + intentName);

        // Note: If the session is started with an intent, no welcome message will be rendered;
        // rather, the intent specific response will be returned.
        if ("MagazinesFromIntent".equals(intentName)) {
          try {
            return sendMgazineIntentResponse(intent, session);
          } catch (Exception e) {
            e.printStackTrace();
            throw new SpeechletException("Invalid Intent");
          }
        } else if ("TweetsIntent".equals(intentName)) {
            return sendTweetIntentResponse(intent, session);
        }else if ("AnyQuestionIntent".equals(intentName)) {
          return sendWhoIsIntentResponse(intent, session);
        } else if("ReadMeMoreIntent".equals(intentName)){
          return readMeMoreTweetIntentResponse(intent, session);
        }else if("ThankyouIntent".equals(intentName)){
          return thankyouIntent(intent, session);
        }else if("BuyIntent".equals(intentName)){
          return buyIntent(intent, session);
        }
        else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
          session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual welcome message
     */
    private SpeechletResponse getWelcomeResponse() {
        // Create the welcome message.
        String speechText =
                "Welcome to the Hearst Publication.  "
                        + "you can say things like, read me Cosmopolitan";
        String repromptText =
                "Please tell me which magazine to read, saying, read me women's world";

        return getSpeechletResponse(speechText, repromptText, true);
    }

  private SpeechletResponse getDefaultResponse() {
    // Create the welcome message.
    String speechText =
      "you could ask me like, read a elle magazine, tweets on Kardashian or would like to buy a subscription for Cosmopolitan magazine. ";
    String repromptText =
      "Please tell me which magazine to read, saying, read me women's world";

    return getSpeechletResponse(speechText, repromptText, true);
  }

  private SpeechletResponse readMeMoreTweetIntentResponse(final Intent intent, final Session session) {


    String speechText, repromptText;

    // Check for favorite color and create output to user.

      // Store the user's favorite color in the Session and create response.
      log.info("--------->readMeMoreTweetIntentResponse to read ==========> " + session.getAttribute(READ_MORE_TITLE));
      //session.setAttribute(SAVE_MAGAZINE_KEY, favoriteColor);
    //session.setAttribute(READ_MORE_TITLE, allArticlesBy.get(0).get("title"));
    //session.setAttribute(READ_MORE_TEXT, allArticlesBy.get(0).get("body"));
      speechText =
        String.format( "Here is the more info on " + session.getAttribute(READ_MORE_TITLE)+", ");
/*
doc = The 12 cleverest Harry Potter twists that confirm JK Rowling is actually magic
doc = Christmas city break: 48 hours in Lübeck, Germany
doc = Quiz: Which Halloween horror movie villain are you?
doc = The X Factor Judges Houses: Simon Cowell and Cheryl choose final acts for live shows
doc = WIN: Michael Clifford's signed mini guitar from our 5SOS play tiny instruments video
doc = Cara Delevingne joins the Kardashians as they celebrate Olivier Rousteing's birthday at fancy Hollywood bash
doc = Demi Lovato doesn't really see the funny side of the Poot Lovato conspiracy theory
doc = Danny Dyer isn't particularly a fan of One Direction: 'They're bland and nothingy - I've seen holograms with more presence'
doc = New Pretty Little Liars trailer for season 6b seems to confirm Hanna isn't engaged to Caleb
doc = One Direction's Harry Styles has been repeating the same French sentence for years
 */
      speechText+=" "+session.getAttribute(READ_MORE_TEXT);
      /*speechText+=" One Direction's Harry Styles has been repeating the same French sentence for years.";
      speechText+=" The X Factor Judges Houses: Simon Cowell and Cheryl choose final acts for live shows.";
      speechText+=" Christmas city break: 48 hours in Lübeck, Germany.";
      speechText+=" The 12 cleverest Harry Potter twists that confirm JK Rowling is actually magic.";*/
      repromptText =
        " Ask me about more hashtags , saying, tweets for Jenners";



    return getSpeechletResponse(speechText, repromptText, true);
  }


  private SpeechletResponse sendTweetIntentResponse(final Intent intent, final Session session) {
    // Get the slots from the intent.
    Map<String, Slot> slots = intent.getSlots();

    // Get the color slot from the list of slots.
    Slot favoriteColorSlot = slots.get(HASHTAG_SLOT);

    String speechText, repromptText;

    // Check for favorite color and create output to user.
    if (favoriteColorSlot != null) {
      // Store the user's favorite color in the Session and create response.
      String favoriteColor = favoriteColorSlot.getValue();
      log.info("--------->sendTweetIntentResponse to read ==========> " + favoriteColor);
      //session.setAttribute(SAVE_MAGAZINE_KEY, favoriteColor);
      speechText =
        String.format( "Here is the latest tweets from "+favoriteColor+". ");
/*
doc = The 12 cleverest Harry Potter twists that confirm JK Rowling is actually magic
doc = Christmas city break: 48 hours in Lübeck, Germany
doc = Quiz: Which Halloween horror movie villain are you?
doc = The X Factor Judges Houses: Simon Cowell and Cheryl choose final acts for live shows
doc = WIN: Michael Clifford's signed mini guitar from our 5SOS play tiny instruments video
doc = Cara Delevingne joins the Kardashians as they celebrate Olivier Rousteing's birthday at fancy Hollywood bash
doc = Demi Lovato doesn't really see the funny side of the Poot Lovato conspiracy theory
doc = Danny Dyer isn't particularly a fan of One Direction: 'They're bland and nothingy - I've seen holograms with more presence'
doc = New Pretty Little Liars trailer for season 6b seems to confirm Hanna isn't engaged to Caleb
doc = One Direction's Harry Styles has been repeating the same French sentence for years
 */
      speechText+=" 3 more days until my new interior design book “Home” hits stores! I love homes. I’ve lived in them all my life.";
      /*speechText+=" One Direction's Harry Styles has been repeating the same French sentence for years.";
      speechText+=" The X Factor Judges Houses: Simon Cowell and Cheryl choose final acts for live shows.";
      speechText+=" Christmas city break: 48 hours in Lübeck, Germany.";
      speechText+=" The 12 cleverest Harry Potter twists that confirm JK Rowling is actually magic.";*/
      repromptText =
        " Ask me about more hashtags , saying, tweets for Jenners";

    } else {
      // Render an error since we don't know what the users favorite color is.
      return getDefaultResponse();
    }

    return getSpeechletResponse(speechText, repromptText, true);
  }

  private  Map<String,Object> queryContent(String input) throws IOException {
    String url="https://api.havenondemand.com/1/api/sync/getcontent/v1?index_reference=http%3A%2F%2Fen.wikipedia.org%2Fwiki%2F" +input+
      "&highlight_expression=%22Alan+Turing%22&apikey=5c4af805-c545-4702-b779-da7bc378cb83";
    URI uri = URI.create(url);

    final Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target(uri);

    Response response = webTarget.request(MediaType.APPLICATION_JSON).get();

    //Se Response.Status.OK;
    System.out.println("response = " + response.getStatus());
    StringReader stringReader = new StringReader(webTarget.request(MediaType.APPLICATION_JSON).get(String.class));
    String result = IOUtils.toString(stringReader);
    System.out.println("stringReader = " + result);
    return  new Gson().fromJson(result,Map.class);
  }

  private SpeechletResponse sendWhoIsIntentResponse(final Intent intent, final Session session) {
    // Get the slots from the intent.
    Map<String, Slot> slots = intent.getSlots();

    // Get the color slot from the list of slots.
    Slot favoriteColorSlot = slots.get(NAME_SLOT);

    String speechText, repromptText;

    // Check for favorite color and create output to user.
    if (favoriteColorSlot != null) {
      // Store the user's favorite color in the Session and create response.
      String favoriteColor = favoriteColorSlot.getValue();
      log.info("--------->sendWhoIsIntentResponse to read ==========> " + favoriteColor);
      try {
        Map<String, Object> result = queryContent(favoriteColor);
        String title = (String)result.get("title");
      } catch (IOException e) {
        e.printStackTrace();
      }

      //session.setAttribute(SAVE_MAGAZINE_KEY, favoriteColor);
      speechText =
        String.format(  favoriteColor +" is , ");
/*
doc = The 12 cleverest Harry Potter twists that confirm JK Rowling is actually magic
doc = Christmas city break: 48 hours in Lübeck, Germany
doc = Quiz: Which Halloween horror movie villain are you?
doc = The X Factor Judges Houses: Simon Cowell and Cheryl choose final acts for live shows
doc = WIN: Michael Clifford's signed mini guitar from our 5SOS play tiny instruments video
doc = Cara Delevingne joins the Kardashians as they celebrate Olivier Rousteing's birthday at fancy Hollywood bash
doc = Demi Lovato doesn't really see the funny side of the Poot Lovato conspiracy theory
doc = Danny Dyer isn't particularly a fan of One Direction: 'They're bland and nothingy - I've seen holograms with more presence'
doc = New Pretty Little Liars trailer for season 6b seems to confirm Hanna isn't engaged to Caleb
doc = One Direction's Harry Styles has been repeating the same French sentence for years
 */
      speechText+=" in her own words, the Kardashians’ momager (that’s their mum-slash-manager) and runs her own production company, Jenner Communications.";
      //speechText+=" We are giving away one year free subscription to our new customers, Would you like to buy it? ";
      /*speechText+=" One Direction's Harry Styles has been repeating the same French sentence for years.";
      speechText+=" The X Factor Judges Houses: Simon Cowell and Cheryl choose final acts for live shows.";
      speechText+=" Christmas city break: 48 hours in Lübeck, Germany.";
      speechText+=" The 12 cleverest Harry Potter twists that confirm JK Rowling is actually magic.";*/
      repromptText =
        " you could ask me like, read from elle magazine, tweets on Kardashian or would like to buy a subscription for Cosmopolitan magazine..";

    } else {
      // Render an error since we don't know what the users favorite color is.
      return getDefaultResponse();
    }

    return getSpeechletResponse(speechText, repromptText, true);
  }

  private SpeechletResponse thankyouIntent(final Intent intent, final Session session) {
    String speechText="";
    boolean isAskResponse = false;

    // Get the user's favorite color from the session.

    speechText+="You are welcome. We really appreciate using our service. We are giving away one year free subscription to our new customers, Would you like to buy it? ";

    String repromptText =
      " Would you like to buy it?";

    /*// Check to make sure user's favorite color is set in the session.
    if (StringUtils.isNotEmpty(favoriteColor)) {
      speechText = String.format("Your favorite color is %s. Goodbye.", favoriteColor);
    } else {
      // Since the user's favorite color is not set render an error message.
      speechText =
        "I'm not sure what your favorite color is. You can say, my favorite color is "
          + "red";
      isAskResponse = true;
    }*/

    return getSpeechletResponse(speechText, repromptText, true);
  }

  private SpeechletResponse buyIntent(final Intent intent, final Session session) {

    Map<String, Slot> slots = intent.getSlots();

    // Get the color slot from the list of slots.
    Slot favoriteColorSlot = slots.get("buyresponse");

    String speechText="", repromptText;

    // Check for favorite color and create output to user.
    if (favoriteColorSlot != null) {

      log.info("--------->sendWhoIsIntentResponse to read ==========> " + favoriteColorSlot.getValue());


      if(StringUtils.equalsIgnoreCase(favoriteColorSlot.getValue(),"yes") || StringUtils.equalsIgnoreCase(favoriteColorSlot.getValue(),"buy")){
        speechText+="Once again, thank you for your business. ";
      }else{
        speechText+="We look forward to join you our family.  ";
      }

    }


    return getSpeechletResponse(speechText, speechText, false);
  }

  Map<String,String> allMags = new HashMap<String,String>();
  public void init(){
    /**
     * Cosmopolitan
     Redbook
     Woman's day
     Elle
     Housekeeping
     Seventeen
     Road and Track
     Esquire
     Harper's Bazaar
     */
    allMags.put("cosmopolitan","sugarscape.hearst.io");
    allMags.put("elle","elle.hearst.io");
    allMags.put("redbook","redbookmag.hearst.io");
    allMags.put("housekeeping","goodhousekeeping.hearst.io");
    allMags.put("road","roadandtrack.hearst.io");
    allMags.put("esquire","esquire.hearst.io");
    allMags.put("woman","womansday.hearst.io");
    allMags.put("harper","harpersbazaar.hearst.io");
    allMags.put("house","housebeautiful.hearst.io");
    allMags.put("seventeen","seventeen.hearst.io");


  }

  public List<Map<String,String>> getAllArticlesBy(String hostName) throws Exception{


    /*String url="https://elle.hearst.io/api/v1/articles?" +

      "publishedFrom=2015-10-01&publishedTo=2015-10-24" +

      "&_key=ydg5q7ezdemk89dmcn6ckj4p";*/

    String url = "https://" +hostName+
      "/api/v1/articles?visibility=1&all_images=0&get_image_cuts=0&ignore_cache=0&limit=10&order_by=date+desc&_key=ydg5q7ezdemk89dmcn6ckj4p";

    System.out.println("url = " + url);

    URI uri = URI.create(url);

    final Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target(uri);

    /*Form form = new Form();
    form.param("index", "test1");
    form.param("json",s);*/

    Response response = webTarget.request(MediaType.APPLICATION_JSON).get();

    StringReader stringReader = new StringReader(webTarget.request(MediaType.APPLICATION_JSON).get(String.class));
    String result = IOUtils.toString(stringReader);
    System.out.println("output = " + result.length());
    Gson gson = new Gson();
    Map<String,Object> results = gson.fromJson(result,Map.class);
    List items = (List)results.get("items");
    List<Map<String,String>> returnResults = new ArrayList<Map<String, String>>();
    for (Object item : items) {
      Map<String,Object> inObj = (Map<String,Object>)item;

      Map<String,String> res1 = new HashMap<String, String>();
      //Document doc = Jsoup.parse((String)inObj.get("title"));
      //System.out.println("(String)inObj.get(\"title\") = " + (String) inObj.get("title"));
      res1.put("title", Jsoup.parse((String) inObj.get("title")).body().text());
      res1.put("body",Jsoup.parse((String)inObj.get("body")).body().text());
      System.out.println("doc = " + Jsoup.parse((String) inObj.get("title")).body().text());
      returnResults.add(res1);
    }
    return returnResults;
  }


  private SpeechletResponse sendMgazineIntentResponse(final Intent intent, final Session session) throws Exception{
    // Get the slots from the intent.
    Map<String, Slot> slots = intent.getSlots();

    // Get the color slot from the list of slots.
    Slot favoriteColorSlot = slots.get(MAGAZINE_SLOT);

    String speechText, repromptText;

    // Check for favorite color and create output to user.
    if (favoriteColorSlot != null) {
      // Store the user's favorite color in the Session and create response.
      String favoriteColor = favoriteColorSlot.getValue();
      log.info("------------>>>> favoriteMagazine to read ==========> " + favoriteColor);
      session.setAttribute(SAVE_MAGAZINE_KEY, favoriteColor);
      speechText =
        String.format( "Here is the latest news from your favorite magazine %s, ", favoriteColor);
/*
doc = The 12 cleverest Harry Potter twists that confirm JK Rowling is actually magic
doc = Christmas city break: 48 hours in Lübeck, Germany
doc = Quiz: Which Halloween horror movie villain are you?
doc = The X Factor Judges Houses: Simon Cowell and Cheryl choose final acts for live shows
doc = WIN: Michael Clifford's signed mini guitar from our 5SOS play tiny instruments video
doc = Cara Delevingne joins the Kardashians as they celebrate Olivier Rousteing's birthday at fancy Hollywood bash
doc = Demi Lovato doesn't really see the funny side of the Poot Lovato conspiracy theory
doc = Danny Dyer isn't particularly a fan of One Direction: 'They're bland and nothingy - I've seen holograms with more presence'
doc = New Pretty Little Liars trailer for season 6b seems to confirm Hanna isn't engaged to Caleb
doc = One Direction's Harry Styles has been repeating the same French sentence for years
 */
      String hostToContact = allMags.get(favoriteColor);

      if(hostToContact == null){
        hostToContact = "womansday.hearst.io";
      }

      List<Map<String, String>> allArticlesBy = getAllArticlesBy(hostToContact);

      if(allArticlesBy != null && allArticlesBy.size()>0){
        speechText+=allArticlesBy.get(0).get("title")+". ";
        if(allArticlesBy.get(1) != null){
          speechText+=allArticlesBy.get(1).get("title")+". ";
        }
        session.setAttribute(READ_MORE_TITLE, allArticlesBy.get(0).get("title"));
        session.setAttribute(READ_MORE_TEXT, StringUtils.substringBefore(allArticlesBy.get(0).get("body"), "."));
      }else{
        log.info("<<<<<<<<<<<<<<<<<<< Reading hardcoded text -----  >>>>>>>>>>>>>>>>>");
        speechText+=" Cara Delevingne joins the Kardashians as they celebrate Olivier Rousteing's birthday at fancy Hollywood bash.";
        session.setAttribute(READ_MORE_TEXT, "Apparently not everyone spends their Friday night laying on the sofa watching re-runs of The Simpsons and the Kardashian gang joined the Smith family, Cara Delevingne, Jaime King and a bunch more supermodel types to celebrate the birthday of Balmain designer Olivier Rousteing in Hollywood.");
      }

      /*speechText+=" Cara Delevingne joins the Kardashians as they celebrate Olivier Rousteing's birthday at fancy Hollywood bash.";*/
      /*speechText+=" One Direction's Harry Styles has been repeating the same French sentence for years.";
      speechText+=" The X Factor Judges Houses: Simon Cowell and Cheryl choose final acts for live shows.";
      speechText+=" Christmas city break: 48 hours in Lübeck, Germany.";
      speechText+=" The 12 cleverest Harry Potter twists that confirm JK Rowling is actually magic.";*/
      repromptText =
        " Ask me about tweets on kim or ellen ";

    } else {
      // Render an error since we don't know what the users favorite color is.
      return getDefaultResponse();
    }

    return getSpeechletResponse(speechText, repromptText, true);
  }

    /**
     * Creates a {@code SpeechletResponse} for the intent and stores the extracted color in the
     * Session.
     *
     * @param intent
     *            intent for the request
     * @return SpeechletResponse spoken and visual response the given intent
     */
    private SpeechletResponse setColorInSession(final Intent intent, final Session session) {
        // Get the slots from the intent.
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from the list of slots.
        Slot favoriteColorSlot = slots.get(COLOR_SLOT);
        String speechText, repromptText;

        // Check for favorite color and create output to user.
        if (favoriteColorSlot != null) {
            // Store the user's favorite color in the Session and create response.
            String favoriteColor = favoriteColorSlot.getValue();
            session.setAttribute(SAVE_MAGAZINE_KEY, favoriteColor);
            speechText =
                    String.format("I now know that your favorite color is %s. You can ask me your "
                            + "favorite color by saying, what's my favorite color?", favoriteColor);
            repromptText =
                    "You can ask me your favorite color by saying, what's my favorite color?";

        } else {
            // Render an error since we don't know what the users favorite color is.
            speechText = "I'm not sure what your favorite color is, please try again";
            repromptText =
                    "I'm not sure what your favorite color is. You can tell me your favorite "
                            + "color by saying, my favorite color is red";
        }

        return getSpeechletResponse(speechText, repromptText, true);
    }



    /**
     * Returns a Speechlet response for a speech and reprompt text.
     */
    private SpeechletResponse getSpeechletResponse(String speechText, String repromptText,
            boolean isAskResponse) {
      log.info("$$$$$$$$$$$$$$$$$$-----> sending speechText = " + speechText);
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        if (isAskResponse) {
            // Create reprompt
            PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
            repromptSpeech.setText(repromptText);
            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(repromptSpeech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);

        } else {
            return SpeechletResponse.newTellResponse(speech, card);
        }
    }
}
