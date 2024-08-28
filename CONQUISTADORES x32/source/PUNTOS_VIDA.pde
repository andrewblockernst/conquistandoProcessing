class PuntosVida {

  float puntaje = 0;
  int especialidades = 0;
  boolean vida = true; 
  PuntosVida() {
  }

  void puntos() {
    puntaje += 0.1;  //suma del puntaje
    fill(255);
    fill(#FFE200);  
    textSize(25);
    stroke(0,10);
    text("Puntaje:" + int(puntaje), 275, 50);   //mostrar puntaje durante el juego
  }
  void especialidades () {
    fill(255);
    fill(#FFE200);
    textSize(25);
    stroke(0,10);
    text("Especialidades:" + especialidades, 600, 50);   //mostrar puntaje de las especialidades 
  }
  void perderVida () {
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
  int y = int(random(suelo-cuadrilla*6, suelo-cuadrilla*3));
  int vel = 5;
  int diam = 40;
  int tipo;
  Especialidad(int tipo, int aceleracion) {
    this.tipo = tipo;   //establece el tipo de especialidad
    vel += aceleracion;   //aumenta la velocidad con el paso del tiempo
  }   

  void dibujar() {
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
  void mover() {
    x -= vel;   //movimiento
  }
}

void especialidad () {
  for (Especialidad s : especialidades) {   //recorre array de especialidades
    s.dibujar();
    s.mover();
  }
}
void addEspecialidad () {
  int probabilidad = int(random(1000));   //genera un numero del 1 al 1000
  if (probabilidad <= 1)    //da una probabilidad para que se genere cada cierto tiempo random
    especialidades.add (new Especialidad(int(random(0, 6)), frecuencia/500));  //crea un elemento de la ArrayList
}                                                                              //se crea un random para el tipo, y tambien se cambia el valor de aceleracion
void removeEspecialidad () {
  for (int i = especialidades.size ()-1; i >= 0; i--) {
    if (especialidades.get (i).x < -especialidades.get(i).diam) {   //cuando la especialidad sale de la pantalla
      especialidades.remove(i);   //eliminarla
    }
  }
}











