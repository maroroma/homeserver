#include <Arduino.h>
#include <IPAddress.h>
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
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
}

/**
 * Enregistre le component au homeserveur
*/
HomeServerClient HomeServerClient::registerToHomeServer()
{
    IPAddress homeserverAddress;
    homeserverAddress.fromString(HOMESERVER_IP);
    WiFiClient wifiClient;
    if (wifiClient.connect(homeserverAddress, HOMESERVER_PORT))
    {
        wifiClient.println("GET /api/iot/register?id=" + WiFi.macAddress() + "&ipAddress=" + WiFi.localIP().toString() + "&componentType=BUZZER&name=" + moduleName + " HTTP/1.0");
        wifiClient.println();
    }
}