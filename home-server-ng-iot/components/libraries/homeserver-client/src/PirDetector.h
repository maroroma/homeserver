#ifndef PIRDETECTOR_h
#define PIRDETECTOR_h

#include <Arduino.h>
#include <String.h>




/**
 * Définition d'une classe pour la gestion d'un détecteur PIR
 */
class PirDetector
{
public:
  /**
   * Instancie un nouveau PirDetector avec le pin de lecture
   */
  PirDetector(int dataPin);
  
  /**
   * Détermine si un mouvement a été détecté (uniquement un changement de status)
   */ 
  bool isMotionDetected();
  
  /**
   * Détermine si un mouvement est en cours (état haut du détecteur)
   * Ne présume pas du status précédent
   */
  bool isMotionInProgress();
    
private:
  /**
   * stockage du pin associé au PIR
   */
  int pinPIR;
  /**
   * stockage du dernier état
   */
  int lastPirState;
};

#endif