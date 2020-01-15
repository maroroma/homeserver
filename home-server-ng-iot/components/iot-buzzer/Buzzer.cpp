#include <Arduino.h>

/**
 * Classe utilitaire pour la gestion du buzzer
 */
class Buzzer {
  private:
    // pin utilisé au niveau du matos
    int pinBuzzer;
    // détermine si le buzzer est activé
    bool isBuzzerOn;

  public:
    /**
     * Constructeur.
     * Initialise le pin en OUTPUT
     * @param pinBuzzer numéro du pin utilisé sur le matos
     */
    Buzzer(int pinBuzzer) {
      this->pinBuzzer = pinBuzzer;
      this->isBuzzerOn = false;
      pinMode(this->pinBuzzer, OUTPUT);
    }
    
    /**
     * active le buzzer
     */
    void switchOn() {
      this->isBuzzerOn = true;
    }

    /**
     * désactive le buzzer
     */
    void switchOff() {
      this->isBuzzerOn = false;
    }

    /**
     * détermine si le buzzer est actif
     */
    bool isOn() {
      return this->isBuzzerOn;
    }
    
    /**
     * appelé par la boucle de traitement principal, génère du son si le buzzer est activé
     * @return true si le buzzer était activé
     */
    bool buzz() {
      if (this->isOn()) {
        for (int i = 0; i <80; i++)
        {
          digitalWrite (this->pinBuzzer, HIGH) ; //send tone
          delay (1) ;
          digitalWrite (this->pinBuzzer, LOW) ; //no tone
          delay (1) ;
        }
        for (int i = 0; i <100; i++) 
        {
          digitalWrite (this->pinBuzzer, HIGH) ;
          delay (2) ;
          digitalWrite (this->pinBuzzer, LOW) ;
          delay (2) ;
        }
      } else {
        digitalWrite (this->pinBuzzer, HIGH) ;
      }

      return this->isOn();
    }

};
