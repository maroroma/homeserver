#include <HomeServerClient.h>
#include <ThreeLedState.h>

// RZO
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>

#ifndef STASSID
#define STASSID "SFR_B020"
#define STAPSK  "***********"
#endif

const char* ssid = STASSID;
const char* password = STAPSK;

ESP8266WebServer server(80);


// PINS
const int pinRedLed = 5;
const int pinYellowLed = 4;
const int pinGreenLed = 0;
const int pinMotionSensor = 14;

// connexion homeserver
const String moduleName = "homeserver-iot-motion-trigger";
HomeServerClient homeserverClient(moduleName, "TRIGGER");

ThreeLedState threeLedState = ThreeLedState(pinRedLed, pinYellowLed, pinGreenLed);

//ICACHE_RAM_ATTR void detectsMovement() {
//  Serial.println("MOTION DETECTED!!!");
//  threeLedState.blinkGreen().blinkGreen();
//}

int val = 0;
int pirState = LOW;

void setup() {

  Serial.begin(9600);  
  Serial.println("kikou");

  //pinMode(pinMotionSensor, INPUT_PULLUP);
  //attachInterrupt(digitalPinToInterrupt(pinMotionSensor), detectsMovement, RISING);

  threeLedState.warmup().powered();

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

  pinMode(pinMotionSensor, INPUT);

  
}

void loop() {
  
  MDNS.update();

  val = digitalRead(pinMotionSensor);  // read input value
  if (val == HIGH) {            // check if the input is HIGH
    threeLedState.blinkGreen();  // turn LED ON
    if (pirState == LOW) {
      // we have just turned on
      Serial.println("Motion detected!");
      // We only want to print on the output change, not state
      pirState = HIGH;
    }
  } else {
    //digitalWrite(ledPin, LOW); // turn LED OFF
    if (pirState == HIGH){
      // we have just turned of
      Serial.println("Motion ended!");
      // We only want to print on the output change, not state
      pirState = LOW;
    }
  }
}
