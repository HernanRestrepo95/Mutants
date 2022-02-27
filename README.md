# Mutants

Mutants MercadoLibre

## Aplicacion para validar ADN y confirmar si es mutante o no

- Api expuesta en AWS EC2, Ubuntu 20.0, Maven 3.8.4, SpringBoot 2.6.4, JDK 17, MariaDB, Jacoco 0.8.5, Swagger 3.0, Junit, Mockito

Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar
contra los X-Men.
Te ha contratado a ti para que desarrolles un proyecto que detecte si un
humano es mutante basándose en su secuencia de ADN.
Para eso te ha pedido crear un programa con un método o función con la siguiente forma.

- boolean isMutant(String[] dna); // Ejemplo Java

En donde recibirás como parámetro un array de Strings que representan cada fila de una tabla
de (NxN) con la secuencia del ADN. Las letras de los Strings solo pueden ser: (A,T,C,G), las
cuales representa cada base nitrogenada del ADN.

## No Mutante

| A   | T   | G   | C   | G   | A   |
| --- | --- | --- | --- | --- | --- |
| C   | A   | G   | T   | G   | C   |
| T   | T   | A   | T   | T   | T   |
| A   | G   | A   | C   | G   | G   |
| G   | C   | G   | T   | C   | A   |
| T   | C   | A   | C   | T   | G   |

## Mutante

| A   | T   | G   | C   | G   | A   |
| --- | --- | --- | --- | --- | --- |
| C   | A   | G   | T   | G   | C   |
| T   | T   | A   | T   | G   | T   |
| A   | G   | A   | A   | G   | G   |
| C   | C   | C   | C   | T   | A   |
| T   | C   | A   | C   | T   | G   |

Sabrás si un humano es mutante, si encuentras más de una secuencia de cuatro letras iguales​, de forma oblicua, horizontal o vertical.

- Ejemplo (Caso mutante):
  String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
  En este caso el llamado a la función isMutant(dna) devuelve “true”.

Desarrolla el algoritmo de la manera más eficiente posible.

## Compilacion y despliegue

- local
  
  $ruta/-> mvn clean install
  
  $ruta_jar/-> java -jar mutants-0.0.1-SNAPSHOT.jar

La aplicacion apunta a una base de datos montada en la nube, sin embargo se puede configurar el properties para apuntar a una base de datos local, se adjunta dump.

Se adjunta captura de la cobertura del codigo, reporte generado con Jacoco.
Pruebas unitarias automaticas creadas con la ayuda de JUnit y Mockito.

## Rutas y consumos

- Ruta Swagger (Documentacion EndPoints)
  
  http://ec2-34-234-75-196.compute-1.amazonaws.com:8081/swagger-ui.html#/

- GET - Metodo que devuelve un Json con las estadísticas de las verificaciones de ADN:
  
  http://ec2-34-234-75-196.compute-1.amazonaws.com:8081/stats

- POST - Metodo que detecta si un humano es mutante, enviando la secuencia de ADN
  
  http://ec2-34-234-75-196.compute-1.amazonaws.com:8081/mutant
  
  {
  “dna”:["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
  }
