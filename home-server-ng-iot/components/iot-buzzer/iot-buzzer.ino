#include <HomeServerClient.h>

// RZO
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>

// gestion matrice de led
#include <LedControl.h>
#include <binary.h>

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

// gestion matrice de led
const int pinDIN = 14;
const int pinCS = 12;
const int pinCLK = 13;
LedControl ledMatrix = LedControl(pinDIN, pinCLK, pinCS,1);

// fixme : virer les sprites dont on ne se sert plus
int spriteStart[] = {
  B00011000,
  B01011010,
  B10011001,
  B10011001,
  B10000001,
  B10000001,
  B01000010,  
  B00111100
};

int spriteHomeLogo[] = {
  B00011000,
  B00100100,
  B01000010,
  B11111111,
  B10000001,
  B10000001,
  B10000001,  
  B11111111,
  };

int arrow[] = {
  B00111100,
  B00111100,
  B00111100,
  B00111100,
  B11111111,
  B01111110,
  B00111100,
  B00011000,
  };

// gestion du buzzer sonore
Buzzer buzzer = Buzzer(pinBuzzer);
// connection au serveur central
HomeServerClient homeserverClient(moduleName);

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

  // FIXME : gérer ça proprement dans une fonction dédiée
  // récupération du template de led
  String ledTemplate = server.arg("ledTemplate");
  Serial.println("ledTemplate complet : " + ledTemplate);
  int finalTemplate[8];
  int currentLine = 0;
  int lastIndex = 0;
  while(lastIndex != -1) {
    int nextIndex = ledTemplate.indexOf("-", lastIndex);
    String rawValue;
    if (nextIndex != -1) {
      rawValue = ledTemplate.substring(lastIndex, nextIndex);
      lastIndex = nextIndex + 1;
    } else {
      rawValue = ledTemplate.substring(lastIndex);
      lastIndex = nextIndex;
    }
    Serial.println("nouvel entier to decode : " + rawValue);
    finalTemplate[currentLine] = rawValue.toInt();
    currentLine++;
  }

  
  buzzer.switchOn();
  wakeupLedMatrix();
  displaySprite(finalTemplate);
  server.send(200, "application/json", "fy");
}

/**
 * Désactivation du buzzer
 */
void handleBuzzerOff() {
  buzzer.switchOff();
  shutDownLedMatrix();
  server.send(200, "application/json", "{\"buzzer\":false}");
}

void handleNotFound() {
  digitalWrite(led, LOW);
  String message= "this url can't be handled : ";
  message += server.uri();
  server.send(404, "text/plain", message);
  digitalWrite(led, HIGH);
}

void setupLedMatrix() {
   // réveil de la matrix ^^
   ledMatrix.shutdown(0,false);
   // set a medium brightness for the Leds
   ledMatrix.setIntensity(0,1);
   ledMatrix.clearDisplay(0);
}

void displayWaitingAnimation() {
  ledMatrix.clearDisplay(0);
  for(int i=0;i<8;i++) {
    for (int j=0;j<8;j++) {
      ledMatrix.setLed(0,i,j, true);
      delay(20);
      ledMatrix.setLed(0,i,j, false);
    }
  }
}

void shutDownLedMatrix() {
  ledMatrix.clearDisplay(0);
  ledMatrix.shutdown(0,true);
}

void wakeupLedMatrix() {
  ledMatrix.shutdown(0,false);
  ledMatrix.clearDisplay(0);
}

void displaySprite(int sprite[]) {
  ledMatrix.clearDisplay(0);
  // dessin complet du sprite
  for(int i = 0;i < 8;i++) {
    ledMatrix.setRow(0,i,sprite[i]);
  }
}

void setup(void) {
  // init matrix
  setupLedMatrix();
  Serial.begin(9600);  
  Serial.println("kikou");

  // ---------------------------------
  // configuration WIFI
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    displayWaitingAnimation();
  }
  
  if (MDNS.begin("esp8266")) {
    Serial.println("MDNS responder started");
  }



  // ---------------------------------
  // configuration serveur
  displaySprite(spriteHomeLogo);
  server.on("/", handleStatus);
  server.on("/status", handleStatus);

  // pilotage du buzzer via les urls suivantes
  server.on("/buzzer/on", handleBuzzerOn);
  server.on("/buzzer/off", handleBuzzerOff);

  server.onNotFound(handleNotFound);

  server.begin();

  // inscription auprès du homeserver
  homeserverClient.registerToHomeServer();
  delay(500);
  shutDownLedMatrix();
}

void loop(void) {
  server.handleClient();
  MDNS.update();
  // inclusion dans la boucle de traitement
  buzzer.buzz();
}
