#include <Arduino.h>
#include <IPAddress.h>
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include "HomeServerClient.h"

/**
 * Addresse ip du homeserver
 * FIXME : détecter automatiquement la bonne adresse ?
 */
const String HOMESERVER_IP = "192.168.1.52";

/**
 * port du homeserver
 */
const int HOMESERVER_PORT = 80;

/**
 * Instancie un nouveau client avec le nom de composant donné
 */
HomeServerClient::HomeServerClient(String name)
{
    moduleName = name;
	moduleType = "BUZZER";
}


/**
 * Instancie un nouveau client avec le nom de composant donné et son type
 */
HomeServerClient::HomeServerClient(String name, String theType)
{
    moduleName = name;
	moduleType = theType;
}

/**
 * Enregistre le component au homeserveur
*/
void HomeServerClient::registerToHomeServer()
{
    IPAddress homeserverAddress;
    homeserverAddress.fromString(HOMESERVER_IP);
    WiFiClient wifiClient;
    if (wifiClient.connect(homeserverAddress, HOMESERVER_PORT))
    {
		Serial.println("connected to homeserver");
		// String registerRequest = "GET /api/iot/register?id=" + WiFi.macAddress() + "&ipAddress=" + WiFi.localIP().toString() + "&componentType=" + moduleType + "&name=" + moduleName + " HTTP/1.0";
        wifiClient.println("GET /api/iot/register?id=" + WiFi.macAddress() + "&ipAddress=" + WiFi.localIP().toString() + "&componentType=" + moduleType + "&name=" + moduleName + " HTTP/1.0");
        wifiClient.println();
		Serial.println("registred");
    } else {
		Serial.println("cant connect to homeserver");
	}
}

void HomeServerClient::triggered() {
	IPAddress homeserverAddress;
    homeserverAddress.fromString(HOMESERVER_IP);
    WiFiClient wifiClient;
    if (wifiClient.connect(homeserverAddress, HOMESERVER_PORT))
    {
		Serial.println("connected to homeserver");
		String triggeredRequest = "GET /api/iot/components/triggered/" + WiFi.macAddress() + " HTTP/1.0";
        wifiClient.println(triggeredRequest);
        wifiClient.println();
		Serial.println("trigger signal send to homeserver with Request : " + triggeredRequest);
    } else {
		Serial.println("cant connect to homeserver for trigger");
	}
}

void HomeServerClient::handleStatus(ESP8266WebServer &webserver) {
	String statusMessage = "{\"componentName\":\"" + moduleName + "\",";
	statusMessage += "\"ipAddress\":\""+WiFi.localIP().toString()+"\",";
	statusMessage += "\"macAddress\":\""+WiFi.macAddress()+"\"";
	statusMessage += "}";
	
	webserver.send(200, "application/json", statusMessage);
}