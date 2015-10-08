  /*
  Piano.ino
  An Arduino piano.
  @author: Suyash Kumar
  @author: Shana Fielding
  @author: Amy Zhao 
  */
  
  #include <Wire.h>  
  #include "Max3421e.h"
  #include "Usbhost.h"
  #include "AndroidAccessory.h"

  
  
  #define numKeys 8
  #define piezo 7
  #define startButton 47
  #define winLED 22
  #define endButton 45
  
  
  AndroidAccessory acc("Manufacturer",
  		"Model",
  		"Description",
  		"1.0",
  		"http://yoursite.com",
                  "0000000012345678");
  
  int pianoPins[numKeys]={42, 40, 38, 36, 34, 32, 30, 28};
  int pianoFreqs[numKeys]={523, 587, 659, 698, 784, 880, 988, 1047};

  

  void setup(){
    // set communiation speed
    Serial.begin(115200);
   
    Serial.print("\r\nStart");
    boolean out = acc.isConnected();
    Serial.println(out, DEC);
    acc.powerOn();
    //Set all pins:
    pinMode(winLED, OUTPUT);
    for (int i=0;i<numKeys;i++){
      pinMode(pianoPins[i],INPUT);
    }
    
  }

  long lastTime = 0;
  long debounceTime = 500;
  void loop(){
    byte msg[1];

    
    //Serial.println(i);
    
    int c = digitalRead(42);
    int d = digitalRead(40);
    int e = digitalRead(38);
    int f = digitalRead(36);
    int g = digitalRead(34);
    int a = digitalRead(32);
    int b = digitalRead(30);
    int h = digitalRead(28);
    
    if (acc.isConnected()){
                 

          if (c && (millis()-lastTime)>debounceTime) {
            acc.write('C');
            lastTime = millis();
            playSound(0, 500);
          }
          if (d && (millis()-lastTime)>debounceTime) {
            acc.write('D');
             lastTime = millis();
             playSound(1, 500);

          }
          if (e && (millis()-lastTime)>debounceTime) {
            acc.write('E');
            lastTime = millis();
            playSound(2, 500);

          }
          if (f && (millis()-lastTime)>debounceTime) {
            acc.write('F');
            lastTime = millis();
             playSound(3, 500);

          }
          if (g && (millis()-lastTime)>debounceTime) {
            acc.write('G');
            lastTime = millis();
            playSound(4, 500);

          }
          if (a && (millis()-lastTime)>debounceTime) {
            acc.write('A');
            lastTime = millis();
            playSound(5, 500);

          }
          if (b && (millis()-lastTime)>debounceTime) {
            acc.write('B');
            lastTime = millis();
            playSound(6, 500);

          }
          if (h && (millis()-lastTime)>debounceTime) {
            acc.write('H');
            lastTime = millis();
            playSound(7, 500);

          }
          
        
        
          if (digitalRead(endButton)) {
            acc.write('X');
            Serial.println("endbutton");
          }
          
          int len;
          len = acc.read(msg, sizeof(msg), 1);
          if (len>0){
            Serial.println(msg[0]);
            if (msg[0]==1) {
              digitalWrite(winLED, HIGH);
              delay(5000);
              digitalWrite(winLED, LOW);
            }
            else digitalWrite(winLED, LOW);
          }


      
      }

  
  }
    
  


      
   void playSound(int key, int del){
    tone(piezo, pianoFreqs[key]);	
    delay(del);
    noTone(piezo);
   }
