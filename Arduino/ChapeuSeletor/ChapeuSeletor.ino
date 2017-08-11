/**
 * Este sketch faz parte do projeto ChapeuSeletro do grupo HardwareLivreUSP.
 */

#include "Arduino.h"
#include <SoftwareSerial.h>
#include "DFRobotDFPlayerMini.h"
#include <Servo.h>

 
/* Definição de um objeto SoftwareSerial para a comunicação com o módulo Bluetooth.
 * Usaremos os pinos 8 e 9, como RX e TX, respectivamente.
 */
SoftwareSerial bluetoothSerial(7, 8);
 
/* A String data será utilizada para armazenar dados vindos
 *  do Android. O inteiro counter será incrementado a cada
 *  execução do loop principal e transmitido ao Android.
 *  O led conectado ao pino 2 é mais para debug. É útil.
 */
String data = "";
int counter = 0;

/* Definindo um objeto SoftwareSerial para a comunicação com o módulo MP3.
 * Este objeto usa os pinos 10 e 11.
 */
SoftwareSerial mp3Serial(11, 12); // RX, TX
DFRobotDFPlayerMini dfMP3player;
void printDetail(uint8_t type, int value);

Servo servo;
 
/* Nosso setup inclui a inicialização do objeto SoftwareSerial,
 *  como saída e um delay de 2 segundos, só para garantir que
 *  com uma baud rate de 9600 bps. A definição do pino do led
 *  o módulo HC-06 iniciou direitinho.
 */
void setup() {
  Serial.begin(115200);
  bluetoothSerial.begin(9600);
  Serial.println("Módulo bluetooth iniciado");
  servo.attach(5);
  //servo.detach(5); TODO
  Serial.println("Servo iniciado");
  mp3Serial.begin(9600);
  Serial.println(F("DFRobot DFPlayer Mini Demo"));
  Serial.println(F("Initializing DFPlayer ... (May take 3~5 seconds)"));
  
  if (!dfMP3player.begin(mp3Serial)) {  //Use softwareSerial to communicate with mp3.
    Serial.println(F("Unable to begin:"));
    Serial.println(F("1.Please recheck the connection!"));
    Serial.println(F("2.Please insert the SD card!"));
    while(true);
  }
  Serial.println(F("DFPlayer Mini online."));
  delay(2000);

  dfMP3player.volume(30);  //Set volume value. From 0 to 30


  /* Definindo a instância de SoftwareSerial do bluetooth como ativa
   * para poder receber mensagens pelo módulo bluetooth.
   */
  bluetoothSerial.listen();
}
 
/* Vamos pelo loop passo a passo.
 */
void loop() {
  /* No início de cada loop, verificamos se há algo no buffer
   *  da nossa serial. Se houver bytes disponíveis, significa 
   *  que o Android enviou algo, então faremos a leitura do 
   *  novo caractere e adicionamos ao final da string data.
   */
  //int bytes = bluetoothSerial.available();
  //while(bytes > 0) {
  while(bluetoothSerial.available() > 0) {
    //Serial.println(bytes);
    data = char(bluetoothSerial.read());
    //Para ler uma String inteira vindo do bluetooth
    //for(int i=0; i < bytes; i++) {
    //  data += char(bluetoothSerial.read());
    //}
    //bytes = 0;
    Serial.println(data);
    if(data == "g") {
      mp3Serial.listen();
      //dfMP3player.next();
      dfMP3player.playFolder(1, 1);  //play specific mp3 in SD:/15/004.mp3; Folder Name(1~99); File Name(1~255)
      //if (dfMP3player.available()) {
      //  printDetail(dfMP3player.readType(), dfMP3player.read()); //Print the detail message from DFPlayer to handle different errors and states.
      //}
      delay(200);
      for(int i=0; i < 3; i++){
        servo.write(160);
        delay(200);
        servo.write(176); 
        delay(200);
      }
    } else if(data == "c") {
      mp3Serial.listen();
      //dfMP3player.next();
      dfMP3player.playFolder(1, 2);
      delay(200);
      for(int i=0; i < 3; i++){
        servo.write(160);
        delay(200);
        servo.write(176); 
        delay(200);
      }
    } else if(data == "s") {
      mp3Serial.listen();
      //dfMP3player.next();
      dfMP3player.playFolder(1, 3);
      delay(200);
      for(int i=0; i < 3; i++){
        servo.write(160);
        delay(200);
        servo.write(176); 
        delay(200);
      }
    } else if(data == "l") {
      mp3Serial.listen();
      //dfMP3player.next();
      dfMP3player.playFolder(1, 4);
      delay(200);
      for(int i=0; i < 3; i++){
        servo.write(160);
        delay(200);
        servo.write(176); 
        delay(200);
      }
    }
    bluetoothSerial.listen();
    
    /* Ao fim de cada loop, o Arduino transmite uma string
     *  representando o valor armazenado no contador, seguido
     *  de uma quebra de linha. Essa string será enviada para o
     *  módulo HC-06 e daí para o Android.
     */
    bluetoothSerial.print(String(counter));
    bluetoothSerial.print('\n');
   
    /* Finalmente, incrementamos o contador e limpamos data.
     */
    counter = counter + 1;
    
  }
  /* Um pequeno delay para evitar bugs estranhos.
   */
  delay(10);
}


void printDetail(uint8_t type, int value){
  switch (type) {
    case TimeOut:
      Serial.println(F("Time Out!"));
      break;
    case WrongStack:
      Serial.println(F("Stack Wrong!"));
      break;
    case DFPlayerCardInserted:
      Serial.println(F("Card Inserted!"));
      break;
    case DFPlayerCardRemoved:
      Serial.println(F("Card Removed!"));
      break;
    case DFPlayerCardOnline:
      Serial.println(F("Card Online!"));
      break;
    case DFPlayerPlayFinished:
      Serial.print(F("Number:"));
      Serial.print(value);
      Serial.println(F(" Play Finished!"));
      break;
    case DFPlayerError:
      Serial.print(F("DFPlayerError:"));
      switch (value) {
        case Busy:
          Serial.println(F("Card not found"));
          break;
        case Sleeping:
          Serial.println(F("Sleeping"));
          break;
        case SerialWrongStack:
          Serial.println(F("Get Wrong Stack"));
          break;
        case CheckSumNotMatch:
          Serial.println(F("Check Sum Not Match"));
          break;
        case FileIndexOut:
          Serial.println(F("File Index Out of Bound"));
          break;
        case FileMismatch:
          Serial.println(F("Cannot Find File"));
          break;
        case Advertise:
          Serial.println(F("In Advertise"));
          break;
        default:
          break;
      }
      break;
    default:
      break;
  }
}
