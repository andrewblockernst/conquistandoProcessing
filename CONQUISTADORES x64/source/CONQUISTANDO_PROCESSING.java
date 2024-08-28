import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import gifAnimation.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CONQUISTANDO_PROCESSING extends PApplet {

//IMPORTANTE: ANTES DE EJECUTAR importar libreria GifAnimation
//ALUMNOS: SANTIAGO BAEZA GRAF && ANDREW BLOCK ERNST
//TRABAJO PRACTICO INTEGRADOR FINAL | INTRODUCCION A LA PROGRAMACION



Gif pathfinder, pathfinder2;
Minim conquiHimno, coin, jump, gameOver, juego, AAA;
AudioPlayer menuTheme, moneda, saltar, finalTheme, juegoTheme, desvivir;
PFont font;

fondos constante; //CUADRILLA QUE SIRVEN PARA DAR DIMENSIONES, ES UNA GUIA, NO VISIBLE EN VERSION FINAL
//CLASE FONDO
Fondo pantallaMenu;
Fondo pantallaLore;
Fondo pantallaHelp;
Fondo pantallaJuego;
Fondo pantallaFinal;
PuntosVida points; //CLASE PUNTOS
jugador conqui; //CLASE JUGADOR


int estado = 0;  //variable global de los distintos estados

int cuadrilla = 40; //CUADRADOS 40X40, PANTALLA ENTERA ES DE 32X18 CUADRADOS
int suelo; //POSICION DEL SUELO
int frecuencia = 0;
PImage fuego, hacha, serpiente, arania; //IMAGENES DE ENEMIGOS
PImage jugarbackground, lorebackground, helpbackground, gameoverbackground; //IMAGENES DE LOS FONDOS
PImage espAmigo, espCompaniero, espExcursionista, espExplorador, espGuia, espPionero; //IMAGENES ESPECIALIDADES
ArrayList <Enemigo> enemigos = new ArrayList <Enemigo> (); //LISTA DE ARRAY Y CLASE DE ENEMIGOS
ArrayList<Especialidad> especialidades = new ArrayList <Especialidad> (); //LISTA DE ARRAY Y CLASE DE ESPECIALIDADES

public void setup() {
  size(1280, 720);  
  //SUELO
  suelo = height - cuadrilla*3;
  imageMode(CORNER);
  //FUENTE
   font = loadFont("pixelfont.vlw"); 
  textFont(font);
  //IMAGENES
  fuego = loadImage("fogata.png");
  hacha = loadImage("hacha.png");
  serpiente = loadImage("serpiente.png");
  arania = loadImage("arania.png");
  jugarbackground = loadImage("jugarbackground.png");
  lorebackground = loadImage("lorebackground.png");
  helpbackground = loadImage("helpbackground.png");
  gameoverbackground = loadImage("gameoverbackground.png");
  espAmigo = loadImage("amigo.png");
  espCompaniero = loadImage("companero.png");
  espExcursionista = loadImage("excursionista.png");
  espExplorador = loadImage("explorador.png");
  espGuia = loadImage("guia.png");
  espPionero = loadImage("pionero.png"); 
  //GIFS
  pathfinder = new Gif(this, "conqui.gif");
  pathfinder2 = new Gif(this, "conqui2.gif");
  pathfinder.loop();
  pathfinder2.loop();
  //SONIDOS
  conquiHimno = new Minim (this);
  coin = new Minim (this);
  jump = new Minim (this);
  gameOver =  new Minim (this);
  juego = new Minim (this);
  AAA = new Minim (this);
  menuTheme = conquiHimno.loadFile ("Himno Conquistadores.mp3");
  moneda = coin.loadFile ("Sonido de Moneda.mp3");
  saltar = jump.loadFile ("Sonido de Salto.mp3");
  finalTheme = gameOver.loadFile ("Final Theme.mp3");
  juegoTheme = juego.loadFile ("Pathfinder Theme.mp3");
  desvivir = AAA.loadFile ("AAA sound.mp3");
  //OBJETOS DE CLASES
  pantallaMenu = new Fondo();
  pantallaLore = new Fondo();
  pantallaHelp = new Fondo();
  pantallaJuego = new Fondo();
  pantallaFinal = new Fondo();
  constante = new fondos();
  points = new PuntosVida();
  conqui = new jugador(suelo, cuadrilla);

}

public void draw() {
  //ESTADO DEL MENU PRINCIPAL
  if (estado == 0) { 
    menuTheme.play();    //correr musica del menu
    pantallaMenu.dibujarPantallaMenu();    //mostrar imagen del menu, con los distintos botones
    finalTheme.pause();
  } //ESTADO PANTALLA LORE
  if (estado == 1) {
    pantallaLore.dibujarPantallaLore();   //mostrar imagen del lore y el boton para volver al menu
  } //ESTADO PANTALLA CONTROLES
  if (estado == 2) {
    pantallaHelp.dibujarPantallaHelp();    //mostrar imagen de controles y boton
  } //ESTADO JUEGO
  if (estado == 3) {   

    pantallaJuego.dibujarPantallaJuego();   
    juegoTheme.play();   //ejecuta musica de juego
    //constante.rejilla();  //cuadrilla
    conqui.mostrar();   //dibuja al conqui
    conqui.mover();   //funcion del salto y agache
    enemigo();   //muestra y mueve
    addEnemigo();   //agrega un enemigo
    removeEnemigo();   //elimina enemigo
    especialidad();   //muestra y mueve las especialidades
    addEspecialidad();   //agrega una especialidad
    removeEspecialidad();   //elimina especialidad
    Colision();  //detecta colisiones del conqui con los enemigos o las especialidades
    points.puntos();   //muestra puntaje
    points.especialidades();   //muestra puntaje de especialidades
    points.perderVida();   //consecuencia de colisionar con enemigo (perder vida)
  } //ESTADO PANTALLA FINAL
  if (estado == 4) {
    pantallaFinal.dibujarPantallaFinal();   //muestra pantalla final y sus botones
    pantallaFinal.volverMenu();   //opcion de tocar "volver" para regresar al menu
    finalTheme.play();   //reproducir musica final
  }
}

public void keyPressed() {
  conqui.press(keyCode);   //acciones del conqui al presionar los botones
  if (estado == 4) pantallaFinal.votonReiniciar();   //opcion en la pantalla de reinciar con la tecla "r"
}
public void keyReleased() {
  conqui.release(keyCode);   //acciones del conqui al presionar los botones
}

public void Colision () {
  for (Enemigo e : enemigos) { //recorre el array de enemigos
    if (e.x <= conqui.x + conqui.ancho && e.x + e.ancho >= conqui.x && e.y <= conqui.y + conqui.altura && e.y + e.alto >= conqui.y) { //detecta colision entre enemigo y conqui
      points.vida = false;
    } 
    for (int i = especialidades.size ()-1; i >= 0; i--) { 
      if (especialidades.get(i).x <= conqui.x + conqui.ancho && especialidades.get(i).x + especialidades.get(i).diam >= conqui.x &&
        especialidades.get(i).y <= conqui.y + conqui.altura && especialidades.get(i).y + especialidades.get(i).diam >= conqui.y) { //detecta colision entre especialidad y conqui
        especialidades.remove(i); //eliminarla
        points.especialidades += 1; //sumar uno al puntaje de especialidades
        moneda.play(); //sonido
        moneda.rewind();
      }
    }
  }
}

class Enemigo {
  float x = width, y = suelo - cuadrilla*3;
  float velocidad = 7;
  int tipo;
  int ancho, alto;
  boolean tierra = true;
  int caida, gravedad = 1;
  

  Enemigo  (int tipo, float aceleracion) {
    this.tipo = tipo;   //decide el tipo de enemigo que aparece
    velocidad += aceleracion;   //aumento de la velocidad con el tiempo
  }
  public void dibujar() {
    if (tipo == 0) {   //HACHA
      ancho = 80; 
      alto = 40;    
      hacha.resize(ancho, alto);
      image (hacha, x, y);
    } else if (tipo == 1) {   //FUEGO
      ancho = 80; 
      alto = 80;
      y = suelo - cuadrilla*2;
      fuego.resize(ancho, alto);
      image (fuego, x, y);
    } else if (tipo == 2) {   //SERPIENTE
      ancho = 120; 
      alto = 120;
      serpiente.resize(ancho, alto);
      image (serpiente, x, y);
    } else if (tipo == 3) {   ///ARANIA
      ancho = 120;
      alto = 80;
      arania.resize(ancho, alto);
      //SALTO
      y += caida; 
      caida += gravedad;  
      if (y>= suelo-alto) {  //detecta si entra en contacto con el suelo
        y = suelo - alto;
        tierra = true;
      } else tierra = false;
      if (tierra) caida = -15;   //salta automaticamente al tocar el suelo

      image (arania, x, y);
    }
  }
  public void mover() {
    x -= velocidad;   //movimiento
  }
}

public void enemigo () {
  for (Enemigo e : enemigos) {   //recorre array de enemigos y ejecuta las funciones de la clase Enemigo
    e.dibujar();  
    e.mover();
  }
}
public void addEnemigo () {
  
  int periodo;
  frecuencia += 1;   //aumenta con cada frame, sirve para medir el paso del tiempo en el juego
  periodo = 150 - PApplet.parseInt(frecuencia/500)*5;
  println(periodo);
    if (frecuencia % periodo == 0) {   //siempre que frecuencia sea divisble por 150
      enemigos.add(new Enemigo(PApplet.parseInt(random(0, 4)), frecuencia/500));  //crea un elemento del ArrayList enemigos
  }  //se crea un random para el tipo, y tambien se cambia el valor de aceleracion
}
public void removeEnemigo() {
  for (int i = enemigos.size ()-1; i >= 0; i--) {
    if (enemigos.get (i).x < -enemigos.get(i).ancho) {   //cuando el enemigo sale de la pantalla
      enemigos.remove(i);   //eliminarlo
    }
  }
}

class fondos {
  int x, y;
  

  public void rejilla() {   
    for (int i  = 0; i<=height; i+=cuadrilla) {
      y = i;
      for (int k = 0; k<=width; k+=cuadrilla) {
        x = k;
        strokeWeight(1);
        stroke(0, 30);
        noFill();
        rect(x, y, cuadrilla, cuadrilla);
      }
    }
  }
}




class Fondo {
  // Asigno y creo los arrays con las im\u00e1genes que estar\u00edan en bucle
  PImage[]background= new PImage [3];   
  PImage[]foreground= new PImage [3];
  int[]posXbackground= new int[3];
  int[]posXforeground= new int[3];
  boolean reinicio = false;
  int vel1 = 1, vel2 = 2;

  // Constructor con los dos fondos elegidos y en que lugar posicionarse
  Fondo() {
    for (int i=0; i<background.length; i++) {
      background[i]= loadImage("background.png");
      foreground[i]= loadImage("foreground.png");
    }
    for (int u=0; u<posXbackground.length; u++) {
      posXbackground[u]= width*u;
      posXforeground[u]= width*u;
    }
    vel1 += frecuencia/80;
    vel2 += frecuencia/80;
  }

  //Movimiento del array de la imagen del fondo para que "desaparezca y vuelva a aparecer" en forma de loop
  public void dibujarPantallaJuego() {
    for (int k=0; k<background.length; k++) {
      image(background[k], posXbackground[k], 0);
      posXbackground[k] -= vel1;
      if (posXbackground[k]<=-width) {
        posXbackground[k]=width*2;
      }
    }
    //Movimiento del array de la imagen del frente para que "desaparezca y vuelva a aparecer" en forma de loop
    for (int k=0; k<background.length; k++) {
      image(foreground[k], posXforeground[k], 0);
      posXforeground[k]=posXforeground[k]- vel2;
      if (posXforeground[k]<=-width) {
        posXforeground[k]=width*2;
      }
    }
  }

  // Funci\u00f3n que dibuja el MENU
  public void dibujarPantallaMenu() {    
    image(jugarbackground, 0, 0);
    if (mousePressed && mouseX > 65 && mouseX < 130 && mouseY > 558 && mouseY < 670) { //LORE
      estado=1;   //Estado lore
    }

    if (mousePressed && mouseX > 1138 && mouseX < 1240 && mouseY > 562 && mouseY < 670) { //HELP
      estado=2;   //Estado controles
    }
    if (mousePressed && mouseX > 402 && mouseX < 873 && mouseY > 517 && mouseY < 672) {  //PLAY
      estado=3;   //Estado juego
      menuTheme.pause();   //pausar musica al iniciar el juego
      menuTheme.rewind();   //rebobinar para que pueda ser ejecutada de vuelta mas tarde
    }
  }

  // Funci\u00f3n que dibuja el la HISTORIA
  public void dibujarPantallaLore() {    
    image(lorebackground, 0, 0);
    if (mousePressed && mouseX > 548 && mouseX < 735 && mouseY > 55 && mouseY < 92) {   //detecta si se clickeo en el objeto
      estado=0;   //cambio de estado a pantalla inicio
    }
  }

  // Funci\u00f3n que dibuja el apartado de AYUDA y CONTROLES
  public void dibujarPantallaHelp() {
    image(helpbackground, 0, 0);   
    if (mousePressed && mouseX > 548 && mouseX < 735 && mouseY > 55 && mouseY < 92) {   //detecta si se clickeo en el objeto
      estado=0;   //cambio de estado a pantalla inicio
    }
  }

  // Funci\u00f3n que dibuja el GAME_OVER
  public void dibujarPantallaFinal() {

    image(gameoverbackground, 0, 0);
    stroke(0);
    strokeWeight(10);
    text(PApplet.parseInt(points.puntaje), 595, 395);
    text(points.especialidades, 595, 450);
    text(PApplet.parseInt(points.puntaje) + points.especialidades*50, 595, 528);   //calcula y muestra puntaje final
  }
  public void volverMenu() {  //cuando se aprieta en la pantalla final "volver"
    if (mousePressed && mouseX > 548 && mouseX < 735 && mouseY > 55 && mouseY < 92 ) {
      pantallaFinal.valoresIniciales(); //reinicio de los valores
      estado=0;
      
    }
  }
  public void votonReiniciar () {
    if (key == 'r' || key == 'R') {   //si se apreta la tecla "r"
    pantallaFinal.valoresIniciales();   //reiniciar valores
    estado = 3;   //recomenzar el juego
  }
}
  public void valoresIniciales() {
    reinicio = true;
    if (reinicio) {
      for (int i = enemigos.size ()-1; i >= 0; i--) enemigos.remove(i);   //eliminar todos los enemigos
      for (int i = especialidades.size ()-1; i >= 0; i--) especialidades.remove(i);   //eliminar todas las especialidades
      //reinciar puntajes a 0
      points.especialidades = 0;  
      points.puntaje = 0;
      points.vida = true;  //"revivir" al conqui
      conqui.y = 0;   //posicion en el cielo para luego la caida del conqui al "suelo"
      conqui.caida = 0;  //reinicia la caida para que no aparezca saltando      
      frecuencia = 0;  //frecuencia se reinicia desde 0
      finalTheme.pause();   //pausa la musica de perder
      finalTheme.rewind();   //la rebobina
      reinicio = false;   //vuelve reinicio falso para que se ejecute una unica vez cada vez que se active reinicio
    }
  }
}

 class jugador {
  float x, y, ancho, altura;
  float gravedad = 1, caida; 
  boolean salto = false;   //se activa al presionar la tecla UP 
  boolean tierra = false;   //se activa cuando esta en contacto con la tierra
  boolean fuerza = false;   //mientras este activo, le da mas fuerza al salto
  boolean agache = false;   //se activa al presionar la tecla DOWN 
  boolean normal = false;   //se activa para volver a la altura de parado despues de estar agachado
  boolean parado;   //se activa si no esta agachado
  jugador(int suelo, int cuadrilla) {
    altura = cuadrilla*3;
    ancho = cuadrilla;
    x = cuadrilla;
    y = 0;
  }
  public void mostrar() {
    if (parado) image(pathfinder, x, y, cuadrilla, altura);   //corre gif del conqui moviendose parado
  }
  public void mover() {
    y += caida;   //tira al conqui para abajo
    caida += gravedad;   //hace que luego de saltar caiga con cierta parabola, no siempre a la misma velocidad
    if (y >=suelo-altura) {   //detecta colision con el suelo
      y = suelo -altura;    //establece el valor de y para que no atraviese el suelo para abajo
      tierra = true;
      fuerza = true;
    } else tierra = false;
    if (salto && tierra) {   //si, mientras esta en el suelo, se presiona UP
      caida = -18;  //ahora tira al conqui para arriba, pega un salto
      saltar.play();   //ejecuta sonido de salto
      saltar.rewind();   //lo rebobina
    }
    if (salto && fuerza) caida -= 0.4f;   //agrega mas altura al salto mientras se mantenga presionado UP
    if (agache) {    //si se estapresionando DOWN
      altura = cuadrilla*1.5f;   //cambia la altura
      image(pathfinder2, x, y, cuadrilla, altura);   //muestra gif del conqui agachado
      parado = false;
    } else {    //si NO se esta presionando DOWN
      altura = cuadrilla*3;  //vuelve la altura normal
      parado = true;
    }
    if (normal) {
      y -= cuadrilla*1.5f;   //sube una vez la posicion y al soltar DOWN, para que no atraviese la tierra por un segundo
      normal = false;
    }
    if (agache && !tierra) caida += 1.5f;   //hace que la caida del salto sea mas fuerte si se esta agachando el conqui
  }  
  public void press(int tecla) {   //detecta cuando se presionan las teclas
    if (key == CODED) {
      if (tecla == UP) salto = true;
      if (tecla == DOWN) agache = true;
    }
  }
  public void release(int tecla) {   //detecta cuando se sueltan las teclas
    if (key == CODED) {
      if (tecla == UP) {
        salto = false;
        fuerza = false;
      }
      if (tecla == DOWN) {
        agache = false;
        normal = true;
      }
    }
  }
}

class PuntosVida {

  float puntaje = 0;
  int especialidades = 0;
  boolean vida = true; 
  PuntosVida() {
  }

  public void puntos() {
    puntaje += 0.1f;  //suma del puntaje
    fill(255);
    fill(0xffFFE200);  
    textSize(25);
    stroke(0,10);
    text("Puntaje:" + PApplet.parseInt(puntaje), 275, 50);   //mostrar puntaje durante el juego
  }
  public void especialidades () {
    fill(255);
    fill(0xffFFE200);
    textSize(25);
    stroke(0,10);
    text("Especialidades:" + especialidades, 600, 50);   //mostrar puntaje de las especialidades 
  }
  public void perderVida () {
    if (vida == false) {   //cuando colisiona conqui con enemigo
      estado = 4;   //mostrar pantalla final
      juegoTheme.pause();   //pausar musica del juego
      juegoTheme.rewind();   //rebobinarla
      desvivir.play();   //reproducir sonido de muerte
      desvivir.rewind();   //rebobinarlo
    }
  }
}




class Especialidad {
  int x = width;
  int y = PApplet.parseInt(random(suelo-cuadrilla*6, suelo-cuadrilla*3));
  int vel = 5;
  int diam = 40;
  int tipo;
  Especialidad(int tipo, int aceleracion) {
    this.tipo = tipo;   //establece el tipo de especialidad
    vel += aceleracion;   //aumenta la velocidad con el paso del tiempo
  }   

  public void dibujar() {
    //TIPOS DE ESPECIALIDADES
    if (tipo == 0) {    //AMIGO
      espAmigo.resize(diam, diam);
      y = suelo - cuadrilla*4;
      image (espAmigo, x, y);
    } else if (tipo == 1) {   //COMPANIERO
      espCompaniero.resize(diam, diam);
      y = suelo - cuadrilla*5;
      image (espCompaniero, x, y);
    } else if (tipo == 2) {   //EXCURSIONISTA
      espExcursionista.resize(diam, diam);
      y = suelo - cuadrilla*6;
      image (espExcursionista, x, y);
    } else if (tipo == 3) {   //EXPLORADOR
      espExplorador.resize(diam, diam);
      y = suelo - cuadrilla*7;
      image (espExplorador, x, y);
    } else if (tipo == 4) {   //GUIA
      espGuia.resize(diam, diam);
      y = suelo - cuadrilla*8;
      image (espGuia, x, y);
    } else if (tipo == 5) {   //PIONERO
      espPionero.resize(diam, diam);
      y = suelo - cuadrilla*9;
      image (espPionero, x, y);
    }
  }
  public void mover() {
    x -= vel;   //movimiento
  }
}

public void especialidad () {
  for (Especialidad s : especialidades) {   //recorre array de especialidades
    s.dibujar();
    s.mover();
  }
}
public void addEspecialidad () {
  int probabilidad = PApplet.parseInt(random(1000));   //genera un numero del 1 al 1000
  if (probabilidad <= 1)    //da una probabilidad para que se genere cada cierto tiempo random
    especialidades.add (new Especialidad(PApplet.parseInt(random(0, 6)), frecuencia/500));  //crea un elemento de la ArrayList
}                                                                              //se crea un random para el tipo, y tambien se cambia el valor de aceleracion
public void removeEspecialidad () {
  for (int i = especialidades.size ()-1; i >= 0; i--) {
    if (especialidades.get (i).x < -especialidades.get(i).diam) {   //cuando la especialidad sale de la pantalla
      especialidades.remove(i);   //eliminarla
    }
  }
}











  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CONQUISTANDO_PROCESSING" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
