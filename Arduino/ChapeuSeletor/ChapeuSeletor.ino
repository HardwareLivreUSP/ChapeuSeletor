/**
 * Este sketch faz parte do projeto ChapeuSeletor do grupo HardwareLivreUSP.
 */

#include "Arduino.h"
//A IDE Arduino a partir da versão 1.0 já inclui a biblioteca SoftwareSerial.
//Se a sua IDE for de uma versão anterior ou não tiver a biblioteca procure-a em: http://arduiniana.org/
#include <SoftwareSerial.h>
#include "DFRobotDFPlayerMini.h"
//Na arduino UNO o uso de servos com Servo.h desabilita a funcionalidade de PWM nos pinos 9 e 10 (mesmo
//que não haja um servo em algum desses pinos). Por isso, para evitar possíveis problemas, evitei usar 
//esses pinos.
#include <Servo.h>


//#define DEBUG

 
/* Definição de um objeto SoftwareSerial para a comunicação com o módulo Bluetooth.
 * Usaremos os pinos 7 e 8, como RX e TX, respectivamente.
 */
SoftwareSerial bluetoothSerial(7, 8);
 
/* A String dados será utilizada para armazenar dados vindos
 *  do Android. O inteiro mensagem é um contador de mensagens
 *  recebida e transmitido ao Android para debug epossível 
 *  trabamento de erros. Obs.: Se o bluetoothSerial receber
 *  uma string com mais de um caractere, cada um deles será
 *  contado como uma mensagem diferente.
 */
String dados = "";
int mensagem = 0;

/* Definindo um objeto SoftwareSerial para a comunicação com o módulo MP3.
 * Este objeto usa os pinos 11 e 12 como RX e TX, respectivamente.
 */
SoftwareSerial mp3Serial(11, 12);
DFRobotDFPlayerMini dfMP3player;
void printDetail(uint8_t type, int value);

Servo servo;
 
/* O setup faz a inicialização dos objetos SoftwareSerial,
 * para comunicação com o módulo bluetooth e com o módulo
 * MP3 e, um delay de 2 segundos, para garantir que o 
 * módulo HC-06 iniciou completamente.
 */
void setup() {
  Serial.begin(115200);
  bluetoothSerial.begin(9600);
  mp3Serial.begin(9600);
  #ifdef DEBUG
    Serial.println(F("DFRobot DFPlayer Mini Demo"));
    Serial.println(F("Initializing DFPlayer ... (May take 3~5 seconds)"));
    if (!dfMP3player.begin(mp3Serial)) {  //Use softwareSerial to communicate with mp3.
      Serial.println(F("Unable to begin:"));
      Serial.println(F("1.Please recheck the connection!"));
      Serial.println(F("2.Please insert the SD card!"));
      while(true);
    }
    Serial.println(F("DFPlayer Mini online."));
  #endif
  delay(2000);
  dfMP3player.volume(30);  //Define o volume de 30 para o MP3 em um escala de 0 a 30
  //Definindo a instância de SoftwareSerial do bluetooth como ativa
  //para poder receber mensagens pelo módulo bluetooth.
  bluetoothSerial.listen();
}

/**
 * Move o servo de uma posição inicial pi para uma posição final pf
 * n vezes.
 * 
 * @param servo - variável / objeto da biblioteca Servo.h
 * @param pi - valor inicial que será escrito no servo
 * @param pf - valor final que será escrito no servo
 */
void moveServo(Servo servo, int pi, int pf, int n) {
  servo.attach(5);
  for(int i=0; i < n; i++){
    servo.write(pi);
    delay(200);
    servo.write(pf); 
    delay(200);
  }
  servo.detach();
}


void loop() {
  //No início de cada loop, verificamos se há algo no buffer
  //da nossa serial. Se houver bytes disponíveis, significa 
  //que o Android enviou algo, então faremos a leitura do 
  //novo caractere e adicionamos ao final da string dados.
  #ifdef DEBUG
    if(bluetoothSerial.available() <= 0) {
      Serial.println("Nada a ser lido");
    }
  #endif
  while(bluetoothSerial.available() > 0) {
    dados = char(bluetoothSerial.read());
    #ifdef DEBUG
      int bytes = bluetoothSerial.available();
      Serial.print("Recebido: ");
      for(int i=0; i < bytes; i++) {
        dados += char(bluetoothSerial.read());
      }
      Serial.println(dados);
    #endif
    if(dados == "g") {
      mp3Serial.listen();
      #ifdef DEBUG
        if (dfMP3player.available()) {
          printDetail(dfMP3player.readType(), dfMP3player.read());
        }
      #endif
      dfMP3player.playFolder(1, 1);
      delay(200); // delay para ajustar o instante do som com o movimento da boca
      moveServo(servo, 160, 176, 3);
    } else if(dados == "c") {
      mp3Serial.listen();
      #ifdef DEBUG
        if (dfMP3player.available()) {
          printDetail(dfMP3player.readType(), dfMP3player.read());
        }
      #endif
      dfMP3player.playFolder(1, 2);
      delay(200); // delay para ajustar o instante do som com o movimento da boca
      moveServo(servo, 160, 176, 3);
    } else if(dados == "s") {
      mp3Serial.listen();
      #ifdef DEBUG
        if (dfMP3player.available()) {
          printDetail(dfMP3player.readType(), dfMP3player.read());
        }
      #endif
      dfMP3player.playFolder(1, 3);
      delay(200); // delay para ajustar o instante do som com o movimento da boca
      moveServo(servo, 160, 176, 2);
    } else if(dados == "l") {
      mp3Serial.listen();
      #ifdef DEBUG
        if (dfMP3player.available()) {
          printDetail(dfMP3player.readType(), dfMP3player.read());
        }
      #endif
      dfMP3player.playFolder(1, 4);
      delay(200); // delay para ajustar o instante do som com o movimento da boca
      moveServo(servo, 160, 176, 3);
    }
    bluetoothSerial.listen();
    mensagem = mensagem + 1;
    //Ao fim de cada loop, o Arduino transmite uma string
    //representando o numero de mensagens recebidas, seguido
    //de uma quebra de linha. Essa string será enviada para o
    //módulo HC-06 e daí para o Android.
    bluetoothSerial.print(String(mensagem));
    bluetoothSerial.print('\n');
  }
  delay(10); //Um pequeno delay para evitar bugs estranhos.
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
