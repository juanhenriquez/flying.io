# flying.io
A sample android app for a travel agency

## ¿En que consiste?
En Flying.io puedes reservar vuelos a traves de una aplicación de Android, el uso es muy sencillo 
ya que la aplicación consta de 3 partes principales: 

### Origen y Destino: 
Se puede escoger la ciudad de origen y la fecha de partida del pasajero, lo mismo aplica para el destino.

La aplicación valida que la fecha de destino no pueda ser menor que la fecha de origen por obvias razones
para ello(y para hacer el uso de un calendario) se ha hecho el uso de una libreria llamada [MaterialDateTimePicker](https://github.com/wdullaer/MaterialDateTimePicker)
esta permite mostrar el calendario siguiendo la guia de diseño de material design.

### Selcción de Boleteria:

Aqui el usuario puede escoger cuantos *adultos, niños y bebes* viajaran y se puede escoger 
hasta un maximo de 6 pasajeros por edades. El costo del pasaje del niño y los bebes tienen un descuento de 30% y 10% respectivamente.

Ademas de poder seleccionar las edades de los pasajeros, también se puede seleccionar la clase del boleto. Las clases disponibles son:
Business, Economica, Economica Premium y Primera Clase.

### Ver total de la compra:

Una vez que se hayan rellenado todos los datos correctamente, el botón 
de "Comprar Boletos" estara disponible para hacer la compra. Una vez que se genere, se mostrara
el detalle de la compra con los respectivos gastos y descuentos.

## Base de datos usada:

Para la capa de permanencia de esta aplicación se uso Firebase la cual es 
una plataforma móvil que te permite desarrollar rápidamente apps de alta calidad.
Uno de los tantos usos que se le puede dar(y el que se uso para esta aplicación)
fuel una base da datos no relacional en real-time. Donde el esquema es usado para los datos
es el siguiente:


