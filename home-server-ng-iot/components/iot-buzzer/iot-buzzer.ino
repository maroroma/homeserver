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

// MELCHIOR
IPAddress melchiorAddress(192,168,1,52);
WiFiClient melchiorClient;

// CONSTANTES
// pin pour la led interne
const int led = LED_BUILTIN;
// pin pour le buzzer
const int pinBuzzer = 5;
const String moduleName = "homeserver-iot-buzzer";
// gestion du buzzer sonore
Buzzer buzzer = Buzzer(pinBuzzer);

void handleRoot() {
  digitalWrite(led, LOW);
  server.send(200, "text/plain", "hello from homeserver-iot-buzzer!");
  digitalWrite(led, HIGH);
}

void handleStatus() {
  String statusMessage = "{\"componentName\":\"" + moduleName + "\",";
  statusMessage += "\"ipAddress\":\""+WiFi.localIP().toString()+"\",";
  statusMessage += "\"macAddress\":\""+WiFi.macAddress()+"\"";
  statusMessage += "}";
  server.send(200, "application/json", statusMessage);
}
/**
 * Activation du buzzer
 */
void handleBuzzerOn() {
  buzzer.switchOn();
  server.send(200, "application/json", "{buzzer:true}");
}

/**
 * Désactivation du buzzer
 */
void handleBuzzerOff() {
  buzzer.switchOff();
  server.send(200, "application/json", "{buzzer:false}");
}

void handleNotFound() {
  digitalWrite(led, LOW);
  String message= "this url can't be handled : ";
  message += server.uri();
  server.send(404, "text/plain", message);
  digitalWrite(led, HIGH);
}

void setup(void) {
  // configuration des pins
  pinMode(led, OUTPUT);
  
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

  server.on("/", handleStatus);
  server.on("/status", handleStatus);

  // pilotage du buzzer via les urls suivantes
  server.on("/buzzer/on", handleBuzzerOn);
  server.on("/buzzer/off", handleBuzzerOff);

  server.on("/inline", []() {
    server.send(200, "text/plain", "this works as well");
  });
  
  server.onNotFound(handleNotFound);

  server.begin();

  // inscription auprès du homeserver
  if (melchiorClient.connect(melchiorAddress, 80)) {
      melchiorClient.println("GET /api/iot/register?id=" + WiFi.macAddress() +"&ipAddress=" + WiFi.localIP().toString() + "&componentType=BUZZER&name=" + moduleName + " HTTP/1.0");
      melchiorClient.println();
  }
}

void loop(void) {
  server.handleClient();
  MDNS.update();
  // inclusion dans la boucle de traitement
  buzzer.buzz();
}
