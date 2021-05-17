#include "PirDetector.h"



PirDetector::PirDetector(int dataPin) {
	this->pinPIR = dataPin; 
	this->lastPirState = LOW;
	pinMode(this->pinPIR, INPUT);
}

bool PirDetector::isMotionDetected() {
	int val = digitalRead(this->pinPIR);
	if (val == HIGH) {            // check if the input is HIGH
		if (this->lastPirState == LOW) {
		  // we have just turned on
		  Serial.println("Motion detected!");

		  // We only want to print on the output change, not state
		  this->lastPirState = HIGH;
		  return true;
		}
	} else {
		if (this->lastPirState == HIGH){
		  // we have just turned of
		  Serial.println("Motion ended!");
		  // We only want to print on the output change, not state
		  this->lastPirState = LOW;
		}
	}
	
	return false;
}

bool PirDetector::isMotionInProgress() {
	int val = digitalRead(this->pinPIR);
	return val == HIGH;
}