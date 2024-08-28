class fondos {
  int x, y;
  

  void rejilla() {   
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
  // Asigno y creo los arrays con las imágenes que estarían en bucle
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
  void dibujarPantallaJuego() {
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

  // Función que dibuja el MENU
  void dibujarPantallaMenu() {    
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

  // Función que dibuja el la HISTORIA
  void dibujarPantallaLore() {    
    image(lorebackground, 0, 0);
    if (mousePressed && mouseX > 548 && mouseX < 735 && mouseY > 55 && mouseY < 92) {   //detecta si se clickeo en el objeto
      estado=0;   //cambio de estado a pantalla inicio
    }
  }

  // Función que dibuja el apartado de AYUDA y CONTROLES
  void dibujarPantallaHelp() {
    image(helpbackground, 0, 0);   
    if (mousePressed && mouseX > 548 && mouseX < 735 && mouseY > 55 && mouseY < 92) {   //detecta si se clickeo en el objeto
      estado=0;   //cambio de estado a pantalla inicio
    }
  }

  // Función que dibuja el GAME_OVER
  void dibujarPantallaFinal() {

    image(gameoverbackground, 0, 0);
    stroke(0);
    strokeWeight(10);
    text(int(points.puntaje), 595, 395);
    text(points.especialidades, 595, 450);
    text(int(points.puntaje) + points.especialidades*50, 595, 528);   //calcula y muestra puntaje final
  }
  void volverMenu() {  //cuando se aprieta en la pantalla final "volver"
    if (mousePressed && mouseX > 548 && mouseX < 735 && mouseY > 55 && mouseY < 92 ) {
      pantallaFinal.valoresIniciales(); //reinicio de los valores
      estado=0;
      
    }
  }
  void votonReiniciar () {
    if (key == 'r' || key == 'R') {   //si se apreta la tecla "r"
    pantallaFinal.valoresIniciales();   //reiniciar valores
    estado = 3;   //recomenzar el juego
  }
}
  void valoresIniciales() {
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

