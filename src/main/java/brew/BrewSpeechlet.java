/**
 * Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at
 * <p>
 * http://aws.amazon.com/apache2.0/
 * <p>
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package brew;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.*;
import common.ServiceClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazon.speech.ui.Image;


/**
 * This sample shows how to create a simple speechlet for handling speechlet requests.
 */
public class BrewSpeechlet implements Speechlet {
  private static final Logger log = LoggerFactory.getLogger(BrewSpeechlet.class);
  public static final String CART_TOTAL = "cart_total";
  public static final String P_NAMES = "p_names";
  public static final String STATE = "STATE";

  @Override
  public void onSessionStarted(final SessionStartedRequest request, final Session session)
    throws SpeechletException {
    log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
      session.getSessionId());
    // any initialization logic goes here
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

    Intent intent = request.getIntent();
    String intentName = (intent != null) ? intent.getName() : null;

    if ("BrewIntent".equals(intentName)) {
      return getHelpResponse();
    } else if ("OrderIntent".equals(intentName)) {
      return getOrderResponse(intent, session);
    } else if ("JokeIntent".equals(intentName)) {
      return getJokeResponse(intent, session);
    }//CompleteIntent
    /**
     * {
     "intent": "CompleteIntent",
     "slots": []
     },
     {
     "intent": "RecommendIntent",
     "slots": []
     },
     {
     "intent": "RemindIntent",
     "slots": []
     }
     */
    else if ("CompleteIntent".equals(intentName)) {
      return getCompleteResponse(intent, session);
    }
    else if ("NewsIntent".equals(intentName)) {
      return getNewsResponse();
    }
    else if ("RecommendIntent".equals(intentName)) {
      return getRecommendResponse(intent, session);
    }
    else if ("RemindIntent".equals(intentName)) {
      return getRemindResponse(intent, session);
    }
    else if("BuyIntent".equals(intentName)){
      Integer state = (Integer) session.getAttribute(STATE);
      if(state != null && state == 2){
       return getRemindResponseResult(intent, session);
      }else {
        return buyIntent(intent, session);
      }
    }else if ("HelpBrewIntent".equals(intentName)) {
      return getHelpResponse();
    } else {
      System.out.println("intent ---------->>> " + intent != null ? intent.toString() : "");
      System.out.println("request = " + request.getRequestId());
      throw new SpeechletException("Invalid Intent");
    }
  }

  private SpeechletResponse getRecommendResponse(final Intent intent, final Session session) {



    String speechText="";

    speechText = "Do you want to shop for top shell fish delicacies that will go along well with your "+session.getAttribute(P_NAMES) != null ? (String)session.getAttribute(P_NAMES)+" beer":" ale beer. ";

    session.setAttribute(STATE,4);

    return getSpeechletResponse(speechText, "shell fish with " +session.getAttribute(P_NAMES) != null ? (String)session.getAttribute(P_NAMES)+" beer":" ale beer. "+
      " will bring out salinity and natural sweetness while cleansing the palate", true);
  }

  private SpeechletResponse getCompleteResponse(final Intent intent, final Session session) {



    String speechText="", repromptText="";

    Double cartTotal = (Double)session.getAttribute(CART_TOTAL);
    final String pName = (String)session.getAttribute(P_NAMES);

    //session.setAttribute("p_obj",selEntry);
    speechText = "Here are your Order details : ";
    speechText+= pName;
    speechText+= "Scallops - Shellfish .";
    speechText+= " , where order total came as : "+(cartTotal+7.65);


    speechText += "Your order has been sent to delivery.com and will be delivered to 119 West 24th Street \n" +
      "New York, NY 10011. in next 15 minutes. You will receive your order email shortly at brewhack@gmail.com";

    repromptText+=" . Would like me to play music from : All My Friends Say music album, while waiting for your brew ....";


    return getSpeechletResponse(speechText, repromptText, true);
  }

  private SpeechletResponse getRemindResponse(final Intent intent, final Session session) {



    String speechText="";

    speechText = "Do you want your beer guzzling friends to be reminded of John's birthday? (OR) do you want me to invite your brew pals at your place to celebrate John's birthday?";

    session.setAttribute(STATE,2);

    return getSpeechletResponse(speechText, "", true);
  }

  private SpeechletResponse getRemindResponseResult(final Intent intent, final Session session) {



    String speechText="";

    speechText = "I sent text & email on behalf of you to your guzzling friends ";

    session.setAttribute(STATE,-1);

    return getSpeechletResponse(speechText, "", true);
  }

  private SpeechletResponse getJokeResponse(final Intent intent, final Session session) {



    String speechText="", repromptText="";

    List<String> jokes = new ArrayList<>();
    jokes.add("Beer doesn't turn people into somebody they're not. It just makes them forget to hide that part of themselves.");

    jokes.add("Life and beer are very similar .....chill for best results.");

    jokes.add("Where do monkeys go to grab a beer?  ........... ........ The monkey bars! " );

    jokes.add("A bee goes into a bar, It comes out 2 hours later buzzing ");

    final int i = RandomUtils.nextInt(1, 4);

    speechText = jokes.get(i);


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
        speechText+="Thank you for your business. ";
      }else{
        speechText+="Hmm... Ok.";
      }

    }


    return getSpeechletResponse(speechText, "", true);
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
   * @return SpeechletResponse spoken and visual response for the given intent
   */
  private SpeechletResponse getWelcomeResponse() {
    String speechText = "Welcome to brew hack, just say news, recommend, order, joke, remind, complete";

    // Create the Simple card content.
    SimpleCard card = new SimpleCard();
    card.setTitle("Brew HelloWorld");
    card.setContent(speechText);

    // Create the plain text output.
    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
    speech.setText(speechText);

    // Create reprompt
    Reprompt reprompt = new Reprompt();
    reprompt.setOutputSpeech(speech);

    //return SpeechletResponse.newAskResponse(speech, reprompt, card);
    return getSpeechletResponse(speechText, "Hey, how did you like yesterdays Light beer from Budweiser?", true);
  }

  /**
   * Creates a {@code SpeechletResponse} for the hello intent.
   *
   * @return SpeechletResponse spoken and visual response for the given intent
   */
  private SpeechletResponse getHelloResponse() {
    String speechText = "Welcome to Brew Hacks";

    // Create the Simple card content.
    SimpleCard card = new SimpleCard();
    card.setTitle("Brew HelloWorld - Hacks1");
    card.setContent(speechText);

    // Create the plain text output.
    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
    speech.setText(speechText);

    return SpeechletResponse.newTellResponse(speech, card);
  }


  private SpeechletResponse getNewsResponse() {
    String speechText = "Reading out news from brew events, companies & friends ... ";

    // Create the Simple card content.
    SimpleCard card = new SimpleCard();
    card.setTitle("Brew Order - Hacks1");
    card.setContent(speechText);


    final List<String> brewNews = ServiceClient.getBrewNews();

    for (String brewNew : brewNews) {
      speechText += brewNew + " . ";
    }

    // Create the plain text output.
    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
    speech.setText(speechText);


    //return SpeechletResponse.newTellResponse(speech, card);
    return  getSpeechletResponse(speechText,"",true);
  }



  private SpeechletResponse getOrderResponse(final Intent intent, final Session session) {

    // Get the slots from the intent.
    Map<String, Slot> slots = intent.getSlots();

    // Get the color slot from the list of slots.
    Slot beerType = slots.get("beer");


    // Create the Simple card content.
    StandardCard card = new StandardCard();

      /*StandardCard sc = new StandardCard();
      Image i2 = new Image();
      i2.setSmallImageUrl();*/


    card.setTitle("Brew Order - Hacks1");
    final String whichCatBeer = beerType.getValue();
    System.out.println("beerType for order is -->>>>>>>>>>>>>>>> " + beerType.getName() + " ---> " + whichCatBeer);
    Map.Entry<Map, List<Map>> selEntry=null;
    final Map<Map, List<Map>> beerCategories = ServiceClient.getBeerCategories();
    String pName = "";
    String pPrice = "";
    String pVal = "";
    String image = "";
    for (Map.Entry<Map, List<Map>> entry : beerCategories.entrySet()) {

      if(StringUtils.equalsIgnoreCase(whichCatBeer,(String)entry.getKey().get("name"))){
        selEntry = entry;
        int pNum = RandomUtils.nextInt(1,3);
         Map map = entry.getValue().get(1);
        if(map == null){
          pNum = RandomUtils.nextInt(1,3);
          map = entry.getValue().get(pNum);
          if(map == null){
            pNum = RandomUtils.nextInt(1,3);
            map = entry.getValue().get(pNum);
          }
        }
        if(map != null){
          image = (String)entry.getKey().get("image");
          pName = (String)map.get("product_name");
          pPrice = (String)map.get("product_price");
          pVal = (String)map.get("product_price_num");

        }
      }


    }

    final String pNameCat = pName + " from category " + whichCatBeer;
    String speechText = pNameCat +
      " is available and added to your shopping cart at " + pPrice;

    session.setAttribute(CART_TOTAL,pVal != null ? Float.parseFloat(pVal):16.34F);
    session.setAttribute(P_NAMES,pNameCat);

    session.setAttribute("p_obj",selEntry);


    card.setText(speechText);

    // Create the plain text output.
    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
    speech.setText(speechText);

    String repromptText =
      "May I suggest you consider buying 24 case to get the best from your hard earned dollars, would you like to order that one to save $16.31? ";

    System.out.println("image found as ----------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + image);
    //SpeechletResponse.newTellResponse()
    return  getSpeechletImageResponse(speechText, repromptText, true,image);
    //return SpeechletResponse.newAskResponse(speechText, repromptText, true);

  }

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

  private SpeechletResponse getSpeechletImageResponse(String speechText, String repromptText,
                                                 boolean isAskResponse,String image) {
    log.info("$$$$$$$$$$$$$$$$$$-----> sending speechText = " + speechText);
    // Create the Simple card content.
    StandardCard card = new StandardCard();
    card.setTitle(RandomStringUtils.randomAlphanumeric(10));
    card.setText(speechText);
    Image img = new Image();
    img.setLargeImageUrl(image);
    img.setSmallImageUrl(image);
    card.setImage(img);
    //card.setImage();

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

  /**
   * Creates a {@code SpeechletResponse} for the help intent.
   *
   * @return SpeechletResponse spoken and visual response for the given intent
   */
  private SpeechletResponse getHelpResponse() {
    String speechText = "just say news, recommend, order";

    // Create the Simple card content.
    SimpleCard card = new SimpleCard();
    card.setTitle("Brew HelloWorld - Help");
    card.setContent(speechText);

    // Create the plain text output.
    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
    speech.setText(speechText);

    // Create reprompt
    Reprompt reprompt = new Reprompt();
    reprompt.setOutputSpeech(speech);

    return SpeechletResponse.newAskResponse(speech, reprompt, card);
  }
}
