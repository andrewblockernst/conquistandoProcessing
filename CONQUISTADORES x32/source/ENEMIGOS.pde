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
  void dibujar() {
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
  void mover() {
    x -= velocidad;   //movimiento
  }
}

void enemigo () {
  for (Enemigo e : enemigos) {   //recorre array de enemigos y ejecuta las funciones de la clase Enemigo
    e.dibujar();  
    e.mover();
  }
}
void addEnemigo () {
  
  int periodo;
  frecuencia += 1;   //aumenta con cada frame, sirve para medir el paso del tiempo en el juego
  periodo = 150 - int(frecuencia/500)*5;
  println(periodo);
    if (frecuencia % periodo == 0) {   //siempre que frecuencia sea divisble por 150
      enemigos.add(new Enemigo(int(random(0, 4)), frecuencia/500));  //crea un elemento del ArrayList enemigos
  }  //se crea un random para el tipo, y tambien se cambia el valor de aceleracion
}
void removeEnemigo() {
  for (int i = enemigos.size ()-1; i >= 0; i--) {
    if (enemigos.get (i).x < -enemigos.get(i).ancho) {   //cuando el enemigo sale de la pantalla
      enemigos.remove(i);   //eliminarlo
    }
  }
}

