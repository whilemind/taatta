# Taatta
It is basically a IoT device data logger to a database. Right now it is supporting PostgreSQL database. It has the capability to collect data from LoRaWAN and WiFi support devices. 

## Architecture
It is a Spring Boot application. Micro architecture methodology was followed to implement it. So you will be able to run multiple instance of the modules.

## Features
- Can decode PCR2 payload.
- Can decode TBS220 payload.
- Can decode df702 payload.

## Development 
### IDEA
- IntelliJ IDEA 2022.2.1 (Community Edition)

### Java version
- openjdk 17.0.1 2021-10-19
- OpenJDK Runtime Environment Temurin-17.0.1+12 (build 17.0.1+12)

## Deploy taatta component in Linux server as a service.
In order to run it as a service in Linux server Ubuntu 20.04 LTS you need to follow the below steps:

### Create new User and Group
- Create a new user and group in the target machine.
  To create a new user and group follow the below command:
```shell
sudo groupadd taatta
sudo usermod -g taatta
```
- Create a new folder and change the owner of folder using following command:
```shell
mkdir /usr/local/taatta
sudo chown -R taatta:taatta /usr/local/taatta
```
After creating new folder copy all the components jar file like 
collector.jar, wqm101.jar file to that folder.

### Run the application.
If you want to run any component in the commandline then use the following command.

```shell
java -jar -Dspring.profiles.active=prod /usr/local/taatta/<component-name>.jar
```

### Run the application as linux service
If you want to run the application as a linux service under systemctl then you have to
create the following file
```shell
sudo touch /lib/systemd/system/<component-name>.service
```
After creating the file you have to paste the content of the file that exists each of the 
component folder called {COMPONENT-NAME}.service. You have to enable the service if you would 
like to run it at the boot time.

```shell
sudo systemctl enable {component-name}.service
sudo systemctl start {component-name}.service
```




