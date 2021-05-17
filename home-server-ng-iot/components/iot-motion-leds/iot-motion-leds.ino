#include <FastLED.h>
#include <HomeServerClient.h>
#include <PirDetector.h>

// RZO
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>

#ifndef STASSID
#define STASSID "SFR_B020"
#define STAPSK  "unnefhekhacunhaufad3"
#endif

const char* ssid = STASSID;
const char* password = STAPSK;

ESP8266WebServer server(80);



// gestion des leds
#define LED_PIN 4
#define NUM_LEDS 7

// gestion detection de mouvement
const int pinMotionSensor = 14;
PirDetector pirDetector(pinMotionSensor);

// connexion homeserver
const String moduleName = "homeserver-iot-motion-leds";
// on démarre sur de la lampe RGB, on verra pour l'aspecte motion après
// toujours pas fait le multi module d'ailleurs
HomeServerClient homeserverClient(moduleName, "RGBLIGHT");


// liste des leds
CRGB leds[NUM_LEDS];
// couleur par défaut
const int defaultRed = 255;
const int defaultGreen = 147;
const int defaultBlue = 41;

int currentRed = defaultRed;
int currentGreen = defaultGreen;
int currentBlue = defaultBlue;

void setAllLedsToSelectedColor() {
  setAllLedsToColor(currentRed, currentGreen, currentBlue);
}

void resetSelectedColorToDefaultValue() {
   currentRed = defaultRed;
   currentGreen = defaultGreen;
   currentBlue = defaultBlue;
   server.send(200, "application/json", "reset color to default color");
}

void setAllLedsToColor(int red, int green, int blue) {
    for(int currentLed = 0; currentLed < NUM_LEDS; currentLed++) {
      // Turn our current led on to white, then show the leds
      leds[currentLed].r = red;
      leds[currentLed].g = green;
      leds[currentLed].b = blue;
    }
    FastLED.show();
}

void setAllLedsToColor(CRGB color) {
    for(int currentLed = 0; currentLed < NUM_LEDS; currentLed++) {
      // Turn our current led on to white, then show the leds
      leds[currentLed] = color;
    }
    FastLED.show();
}

void handleStatus() {
  homeserverClient.handleStatus(server);
  setAllLedsToColor(CRGB::Yellow);
  delay(200);
  setAllLedsToColor(CRGB::Black);
}

void handleNewColorRequest(){
   String rawRed = server.arg("red");
   String rawGreen = server.arg("green");
   String rawBlue = server.arg("blue");
   Serial.println("couleur demandée :" + rawRed +"-"+ rawGreen +"-"+ rawBlue);

   currentRed = rawRed.toInt();
   currentGreen = rawGreen.toInt();
   currentBlue = rawBlue.toInt();
   
   server.send(200, "application/json", "new color set " + rawRed +"-"+ rawGreen +"-"+ rawBlue);
}

void handleTurnOffLeds() {
  setAllLedsToColor(CRGB::Black);
  server.send(200, "application/json", "all leds to off");
}

void setup() {


  Serial.begin(9600);  
  Serial.println("kikou" + moduleName);

  
  // put your setup code here, to run once:
  FastLED.addLeds<NEOPIXEL, LED_PIN>(leds, NUM_LEDS);

  setAllLedsToColor(CRGB::Red);

  // ---------------------------------
  // configuration WIFI
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
      Serial.println("not connected");
      delay(1000);
  }

  setAllLedsToColor(CRGB::Yellow);
  Serial.println("connected to wifi !" + WiFi.localIP().toString());

  if (MDNS.begin("esp8266")) {
    Serial.println("MDNS responder started");
  }

  homeserverClient.registerToHomeServer();

  setAllLedsToColor(CRGB::Green);
  delay(1000);
  setAllLedsToColor(CRGB::Black);

  // setup serveur web
  server.on("/", handleStatus);
  server.on("/status", handleStatus);
  server.on("/color", handleNewColorRequest);
  server.on("/color/reset", resetSelectedColorToDefaultValue);
  server.on("/off", handleTurnOffLeds);
  server.begin();

}

void loop() {

  server.handleClient();
  
  MDNS.update();

  if(pirDetector.isMotionInProgress()) {
    setAllLedsToSelectedColor();    
  } else  {
    setAllLedsToColor(CRGB::Black);
  }
  
  
}
