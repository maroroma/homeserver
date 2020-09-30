#include <Arduino.h>
#include "ThreeLedState.h"


/**
 * Instancie un nouveau threeLedState avec l'adresse de led qui va bien
 */
ThreeLedState::ThreeLedState(int redPin, int yellowPin, int greenPin)
{
    this->redPin = redPin;
    this->yellowPin = yellowPin;
    this->greenPin = greenPin;
	pinMode(this->redPin, OUTPUT);
	pinMode(this->yellowPin, OUTPUT);
	pinMode(this->greenPin, OUTPUT);
}

 /**
   * Allume brièvement les 3 leds pour s'assurer qu'elles fonctionnent
   */
  ThreeLedState ThreeLedState::warmup(){
	  return this->on(this->redPin)
	  .on(this->yellowPin)
	  .on(this->greenPin)
	  .wait(1000)
	  .off(this->redPin)
	  .off(this->yellowPin)
	  .off(this->greenPin);	  
  }
  
  /**
   * Allume la led rouge dans la séquence de démarrage
   */
  ThreeLedState ThreeLedState::powered() {
	  return this->on(this->redPin);	 
  }
  
  /**
   * Eteint la led rouge et allume la led jaune
   */
  ThreeLedState ThreeLedState::wifiConnected() {
	  return this->off(this->redPin).on(this->yellowPin);
  }
  
  /**
   * Eteint la led jaune et allume brievement la led verte
   */
  ThreeLedState ThreeLedState::registred(){
	  return this->off(this->yellowPin)
	  .on(this->greenPin)
	  .wait(1000)
	  .off(this->greenPin);
  }
  
  /**
   * allume la led donnée
   */
  ThreeLedState ThreeLedState::on(int pin){
	  digitalWrite(pin, HIGH);
	  // return this;	 
  }
  /**
   * éteint la led donnée
   */
  ThreeLedState ThreeLedState::off(int pin){
	  digitalWrite(pin, LOW);
	  // return this;	 
  }
  
  /**
   * fait clignoter une led donnée
   */
  ThreeLedState ThreeLedState::blink(int pin){
	  return this->on(pin)
	  .wait(200)
	  .off(pin)
	  .wait(200);
  }
  
  /**
   * fait clignoter la led rouge
   */
  ThreeLedState ThreeLedState::blinkRed(){
	  return this->blink(this->redPin);	 
  }
  
  /**
   * fait clignoter la led jaune
   */
  ThreeLedState ThreeLedState::blinkYellow(){
	  return this->blink(this->yellowPin);	 
  }
  
  /**
   * fait clignoter la led green
   */
  ThreeLedState ThreeLedState::blinkGreen(){
	  return this->blink(this->greenPin);
  }
  
  ThreeLedState ThreeLedState::wait(int durationMillis) {
	delay(durationMillis);
	// return this;
  }