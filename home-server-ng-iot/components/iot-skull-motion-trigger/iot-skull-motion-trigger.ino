#include <HomeServerClient.h>
#include <ThreeLedState.h>
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


// PINS
const int pinRedLed = 5;
const int pinYellowLed = 4;
const int pinGreenLed = 0;
const int pinMotionSensor = 14;

// pin skulls
// on va les plugger sur un threeLedState pour le pilotage
// RED led d'un point de vue threeLedState
const int pinEye1Led = 12;
// YELLOW led d'un point de vue threeLedState
const int pinEye2Led = 13;
// GREEN led d'un point de vue threeLedState
const int pinSkullLed = 15;


// connexion homeserver
const String moduleName = "homeserver-iot-skull-motion-trigger";
// en réalité, on reste sur un trigger, mais qui va clignoter à fond
HomeServerClient homeserverClient(moduleName, "TRIGGER");
PirDetector pirDetector(pinMotionSensor);

ThreeLedState threeLedState = ThreeLedState(pinRedLed, pinYellowLed, pinGreenLed);
ThreeLedState threeLedStateForSkull = ThreeLedState(pinEye1Led, pinEye2Led, pinSkullLed);


void handleStatus() {
  homeserverClient.handleStatus(server);
  threeLedState.blinkYellow();
}

void setup() {

  Serial.begin(9600);  
  Serial.println("kikou");

  threeLedState.warmup().powered();
  threeLedStateForSkull.redOn();
  threeLedStateForSkull.yellowOn();
  threeLedStateForSkull.greenOn();

  // ---------------------------------
  // configuration WIFI
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    Serial.println("not connected");
    delay(1000);
    threeLedState.blinkYellow();
  }

  threeLedState.wifiConnected();
  Serial.println("connected to wifi !");

  if (MDNS.begin("esp8266")) {
    Serial.println("MDNS responder started");
  }

  homeserverClient.registerToHomeServer();
  threeLedState.registred();
  threeLedStateForSkull.redOff();
  threeLedStateForSkull.yellowOff();
  threeLedStateForSkull.greenOff();

  server.on("/", handleStatus);
  server.on("/status", handleStatus);
  server.begin();
 
}

void loop() {

  server.handleClient();
  
  MDNS.update();

  if(pirDetector.isMotionDetected()) {
    threeLedState.greenOn();
    homeserverClient.triggered();

    // animation pour le skull
    threeLedStateForSkull.blinkRed();
    threeLedStateForSkull.blinkYellow();
    threeLedStateForSkull.redOn();
    threeLedStateForSkull.yellowOn();
    threeLedStateForSkull.blinkGreen();
    threeLedStateForSkull.blinkGreen();
    threeLedStateForSkull.blinkGreen();
    threeLedStateForSkull.blinkGreen();
    threeLedStateForSkull.blinkGreen();
    threeLedStateForSkull.greenOn();
    threeLedStateForSkull.blinkRed();
    threeLedStateForSkull.blinkYellow();
    threeLedStateForSkull.blinkRed();
    threeLedStateForSkull.blinkYellow();
    threeLedStateForSkull.greenOff();
  } else {
    threeLedState.greenOff();
  }
}
