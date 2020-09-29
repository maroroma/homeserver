#ifndef HOMESERVERCLIENT_h
#define HOMESERVERCLIENT_h

#include <Arduino.h>
#include <IPAddress.h>
#include <WiFiClient.h>
#include <String.h>
#include <ESP8266WiFi.h>

/**
 * Définition d'une classe pour la communication avec le homeserver
 */
class HomeServerClient
{
public:
  /**
   * Instancie un nouveau client avec le nom de composant donné
   */
  HomeServerClient(String moduleName);
  /**
   * Instancie un nouveau client avec le nom de composant donné et son type
   */
  HomeServerClient(String moduleName, String moduleType);
  /**
   * Enregistre le component au homeserveur
   */
  HomeServerClient registerToHomeServer();

private:
  /**
   * stockage du nom du module
   */
  String moduleName;
  /**
   * stockage du type de module
   */
  String moduleType;
};

#endif