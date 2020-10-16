
# DESPLIEGUE DE LA APLICACION

1. Clone el proyecto y ubique el archivo dockerfile.
2. Desde una terminal ejecute el siguiente comando:

  > docker build -t ip-validation .
  
3. La aplicacion utiliza una base de datos MySql por lo cual es necesario configurar tambien un contenedor, para lo cual ejecute los siguientes comandos:

  > docker pull mysql:5.7
  
  > docker run --name mysql-standalone -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=ip_db -e MYSQL_USER=admin1 -e MYSQL_PASSWORD=admin123 -d mysql:5.7
  
4. Por ultimo despliegue el contenedor:

  > docker run -d -p 10001:10001 --name ip-validation --link mysql-standalone:mysql ip-validation

En el siguiente archivo encontrara informacion para las pruebas, endpoints y screenshots de pruebas. 
https://docs.google.com/document/d/1e7TdFZIqjVDX2LVlw8cFBwegpkYFSVwoBIvmwQt2pp4/edit?usp=sharing
