void Colision () {
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

