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
  void mostrar() {
    if (parado) image(pathfinder, x, y, cuadrilla, altura);   //corre gif del conqui moviendose parado
  }
  void mover() {
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
    if (salto && fuerza) caida -= 0.4;   //agrega mas altura al salto mientras se mantenga presionado UP
    if (agache) {    //si se estapresionando DOWN
      altura = cuadrilla*1.5;   //cambia la altura
      image(pathfinder2, x, y, cuadrilla, altura);   //muestra gif del conqui agachado
      parado = false;
    } else {    //si NO se esta presionando DOWN
      altura = cuadrilla*3;  //vuelve la altura normal
      parado = true;
    }
    if (normal) {
      y -= cuadrilla*1.5;   //sube una vez la posicion y al soltar DOWN, para que no atraviese la tierra por un segundo
      normal = false;
    }
    if (agache && !tierra) caida += 1.5;   //hace que la caida del salto sea mas fuerte si se esta agachando el conqui
  }  
  void press(int tecla) {   //detecta cuando se presionan las teclas
    if (key == CODED) {
      if (tecla == UP) salto = true;
      if (tecla == DOWN) agache = true;
    }
  }
  void release(int tecla) {   //detecta cuando se sueltan las teclas
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

