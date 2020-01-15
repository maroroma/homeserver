class Buzzer {
  private:
    int pinBuzzer;
    bool isBuzzerOn;

  public:
    Buzzer(int pinBuzzer) {
      this->pinBuzzer = pinBuzzer;
      this->isBuzzerOn = false;
    }

    void switchOn() {
      this->isBuzzerOn = true;
    }

    void switchOff() {
      this->isBuzzerOn = false;
    }

    bool isOn() {
      return this->isBuzzerOn;
    }
};
