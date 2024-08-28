//IMPORTANTE: ANTES DE EJECUTAR importar libreria GifAnimation
//ALUMNOS: SANTIAGO BAEZA GRAF && ANDREW BLOCK ERNST
//TRABAJO PRACTICO INTEGRADOR FINAL | INTRODUCCION A LA PROGRAMACION

import ddf.minim.*;
import gifAnimation.*;
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

void setup() {
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

void draw() {
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

void keyPressed() {
  conqui.press(keyCode);   //acciones del conqui al presionar los botones
  if (estado == 4) pantallaFinal.votonReiniciar();   //opcion en la pantalla de reinciar con la tecla "r"
}
void keyReleased() {
  conqui.release(keyCode);   //acciones del conqui al presionar los botones
}

