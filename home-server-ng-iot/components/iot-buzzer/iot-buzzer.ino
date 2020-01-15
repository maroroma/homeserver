#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>
#include "Buzzer.cpp"

#ifndef STASSID
#define STASSID "SFR_B020"
#define STAPSK  "unnefhekhacunhaufad3"
#endif

const char* ssid = STASSID;
const char* password = STAPSK;

ESP8266WebServer server(80);

// CONSTANTES
// pin pour la led interne
const int led = LED_BUILTIN;
// pin pour le buzzer
const int pinBuzzer = 5;
const String moduleName = "homeserver-iot-buzzer";

//bool isBuzzerOn = false;

Buzzer buzzer = Buzzer(pinBuzzer);

void buzz() {
  if(buzzer.isOn()) {
    for (int i = 0; i <80; i++)
    {
      digitalWrite (pinBuzzer, HIGH) ; //send tone
      delay (1) ;
      digitalWrite (pinBuzzer, LOW) ; //no tone
      delay (1) ;
    }
    for (int i = 0; i <100; i++) 
    {
      digitalWrite (pinBuzzer, HIGH) ;
      delay (2) ;
      digitalWrite (pinBuzzer, LOW) ;
      delay (2) ;
    }
  } else {
    digitalWrite (pinBuzzer, HIGH) ;
  }
}


void handleRoot() {
  digitalWrite(led, LOW);
  server.send(200, "text/plain", "hello from homeserver-iot-buzzer!");
  digitalWrite(led, HIGH);
}

void handleStatus() {
  String message = moduleName + " status\n";
  message += "IP address: " + WiFi.localIP().toString();
  server.send(200, "text/plain", message);
}

void handleBuzzerOn() {
//  isBuzzerOn = true;
  buzzer.switchOn();
  server.send(200, "application/json", "{buzzer:true}");
}

void handleBuzzerOff() {
//  isBuzzerOn = false;
  buzzer.switchOff();
  server.send(200, "application/json", "{buzzer:false}");
}

void handleNotFound() {
  digitalWrite(led, LOW);
  String message = "File Not Found\n\n";
  message += "URI: ";
  message += server.uri();
  message += "\nMethod: ";
  message += (server.method() == HTTP_GET) ? "GET" : "POST";
  message += "\nArguments: ";
  message += server.args();
  message += "\n";
  for (uint8_t i = 0; i < server.args(); i++) {
    message += " " + server.argName(i) + ": " + server.arg(i) + "\n";
  }
  server.send(404, "text/plain", message);
  digitalWrite(led, HIGH);
}

void setup(void) {
  // configuration des pins
  pinMode(led, OUTPUT);
  
  pinMode(pinBuzzer, OUTPUT);

  
  digitalWrite(led, HIGH);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("");

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
  
  if (MDNS.begin("esp8266")) {
    Serial.println("MDNS responder started");
  }

  server.on("/", handleRoot);
  server.on("/status", handleStatus);
  server.on("/buzzer/on", handleBuzzerOn);
  server.on("/buzzer/off", handleBuzzerOff);

  server.on("/inline", []() {
    server.send(200, "text/plain", "this works as well");
  });
  
  server.onNotFound(handleNotFound);

  server.begin();
}

void loop(void) {
  server.handleClient();
  MDNS.update();
  buzz();
}
