# examenParcial

***examen parcial POOM***

El ejercicio realizado, presenta una aplicación sencilla, cuya pagina principal permite acceder a una imagen desde el dispositivo o tomango una fotografía.

De esta captura cargada, se realiza el reconocimiento de texto utilizando firebase, luego el texto (que debe ser el nombre de un país) es traducido al ingles utilizando otro servicio de firebase para posteriormente buscarlo en un json.

Este json se encuentra en [geognos](http://www.geognos.com/api/en/countries/info/all.json "geognos") y en a partir de este archivo, se obtienen los datos sobre el país buscado. Una vez los datos son encontrados se cargan en la interfaz y en memoria para posteriormente visualizarlo en un mapa por medio de _Google Maps_

> Capturas de la aplicación

* ### Pagina Inicial de la aplicación

>> Vista inicial de la aplicación

>><img src="https://github.com/ANTHONYPACHAY/examenParcial/blob/master/app/src/main/res/drawable/home.png?raw=true" alt="inicio" Height="400"/>

>> Resultados de búsqueda

Los datos se han cargado y en la parte inferior se muestran los resultados, para esto el texto reconocido ha sido traducido al inglés y esta traducción se la ha buscado en la lista de paises.

>><img src="https://github.com/ANTHONYPACHAY/examenParcial/blob/master/app/src/main/res/drawable/home01.png?raw=true" alt="inicio" Height="400"/>

>> Segunda parte de los datos resultantes

>><img src="https://github.com/ANTHONYPACHAY/examenParcial/blob/master/app/src/main/res/drawable/home02.png?raw=true" alt="inicio" Height="400"/>

>>  Vista del botón visitar

>><img src="https://github.com/ANTHONYPACHAY/examenParcial/blob/master/app/src/main/res/drawable/ecuador02.png?raw=true" alt="inicio" Height="400"/>

>>Vista de la bandera, polígono en el mapa  y el punto de la capital.

>><img src="https://github.com/ANTHONYPACHAY/examenParcial/blob/master/app/src/main/res/drawable/ecuador01.png?raw=true" alt="inicio" Height="400"/>

>> Vista de los información del punto.

>><img src="https://github.com/ANTHONYPACHAY/examenParcial/blob/master/app/src/main/res/drawable/ecuador03.png?raw=true" alt="inicio" Height="400"/>

* ### Probando con el País Francia

>> Vista de resultados.

>><img src="https://github.com/ANTHONYPACHAY/examenParcial/blob/master/app/src/main/res/drawable/francia01.png?raw=true" alt="inicio" Height="400"/>

>> Vista de los límites y capital de la ciudad.

>><img src="https://github.com/ANTHONYPACHAY/examenParcial/blob/master/app/src/main/res/drawable/francia02.png?raw=true" alt="inicio" Height="400"/>

>> Vista de los datos del punto en el mapa.

>><img src="https://github.com/ANTHONYPACHAY/examenParcial/blob/master/app/src/main/res/drawable/francia03.png?raw=true" alt="inicio" Height="400"/>
