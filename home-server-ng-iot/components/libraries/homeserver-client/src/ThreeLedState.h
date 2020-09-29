#ifndef THREELEDSTATE_h
#define THREELEDSTATE_h

#include <Arduino.h>
#include <String.h>

/**
 * Définition d'une classe pour le pilote de 3 leds pour l'affichage de l'état
 */
class ThreeLedState
{
public:
  /**
   * Instancie un nouveau ThreeLedState avec les 3 adresses de led données
   */
  ThreeLedState(int redPin, int yellowPin, int greenPin);
  /**
   * Allume brièvement les 3 leds pour s'assurer qu'elles fonctionnent
   */
  ThreeLedState warmup();
  
  /**
   * Allume la led rouge dans la séquence de démarrage
   */
  ThreeLedState powered();
  
  /**
   * Eteint la led rouge et allume la led jaune
   */
  ThreeLedState wifiConnected();
  
  /**
   * Eteint la led jaune et allume brievement la led verte
   */
  ThreeLedState registred();
    
  /**
   * allume la led donnée
   */
  ThreeLedState on(int pin);
  
  /**
   * éteint la led donnée
   */
  ThreeLedState off(int pin);
  
  /**
   * fait clignoter une led donnée
   */
  ThreeLedState blink(int pin);
  
  /**
   * fait clignoter la led rouge
   */
  ThreeLedState blinkRed();
  
  /**
   * fait clignoter la led jaune
   */
  ThreeLedState blinkYellow();
  
  /**
   * fait clignoter la led green
   */
  ThreeLedState blinkGreen();
  
  /**
   * Attente en ms
   */
  ThreeLedState wait(int durationMillis);
  
  
  ThreeLedState redOn();
  ThreeLedState redOff();
  
  ThreeLedState yellowOn();
  ThreeLedState yellowOff();
  
  ThreeLedState greenOn();
  ThreeLedState greenOff();
  
private:
  /**
   * pin pour la led rouge
   */
  int redPin;
  /**
   * pin pour la led jaune
   */
  int yellowPin;
  /*
   * pin pour la led verte
   */
  int greenPin;
    
};

#endif