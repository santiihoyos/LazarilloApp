# LazarilloApp
Aplicación para personas ciegas o alguna minusvalía que les traza una ruta desde un origen a su destino evitando incidencias del camino.


Interfaz para no ciegos 50%:
![Seleccion de interfaz](http://i.imgur.com/jDoVyJk.jpg)



Ejemplos de cómo la aplicación evita las incidencias(iconos morados trazando una ruta entre el origen dado y el destino
![My image](http://i.imgur.com/guuMoWN.jpg)
![My image](http://i.imgur.com/nJEFVUM.jpg)
![My image](http://i.imgur.com/1jTW5UD.jpg)


Funcionamiento:

1-Se indica el origen y destino por parte del usuario.

2-Se lanza una peticion HTTTP a Google Maps para que trace la rutas y las devuelva.

3-Una vez se obtinene las rutas por parte de Google Maps se analiza tramo a tramo comparandolo con las incidencias
  Se calcula con un calculo de la distancia de un punto a una recta, por defecto esta puesto un margen de 10 metros.

4-Una vez se obtiene el número de incidencias por ruta se escoge la mejor y se lanzaria a maps para navegar y llevar al usuario a su destino.


TRABAJO POR HACER:
	1-Interfaz adaptada a personas ciegas fácil de navegar con Google Talkback.
	2-Lanzar a Maps para navegar con la ruta dada.
	3-Mejorar sistema de localización.
	4-Mejorar el sistema de temas de la aplicación.
	
	Avanzado:
	5-Mover el punto de paso para evitar la incidencia en caso de que todas la rutas tengas incidencias.
	6-Crear los Andrid Test. 
