# Elevator Emulator

This project simulates elevators in a building. The project is divided into two parts, the Server part that manipulates
objects and the Client part that draws them in real time.
___

## Development status

In work

## Install

Download source files.

```bat
  https://github.com/ranz7/Elevator.git # install
  cd ./Elevator
```

if you don't have git, just download source
from [this page](https://github.com/watislaf/chessbot/releases/tag/V1.0.1600Elo).
____

## Gradle Build / Run local
To build and run project u can either use gradle (if u have installed one)

```bat
  ./gradlew :app:build -Dorg.gradle.java.home="PATH_TO/openjdk-17.0.2"
  ./gradlew :app:run -Dorg.gradle.java.home="PATH_TO/openjdk-17.0.2"
  ./gradlew :window:build -Dorg.gradle.java.home="PATH_TO/openjdk-17.0.2"
  ./gradlew :window:run -Dorg.gradle.java.home="PATH_TO/openjdk-17.0.2"
 ```

____

## Gradle Build / Run remote

### if Docker is not installed on the server

```bat
  sudo bash ./installDocker  
```

### Run server

```bat
  sudo bash ./setupDocker  
```

### Gradle on your desktop

```bat
  ./gradlew :window:buld -Dorg.gradle.java.home="PATH_TO/openjdk-17.0.2"
  ./gradlew :window:run -Dorg.gradle.java.home="PATH_TO/openjdk-17.0.2" --args="IP_OF_YOUR_SERVER"
 ```

_____

### Authors

* Radek Mysliwiec ([@ranz7](https://github.com/ranz7))
* Michał Miziołek ([@PiJayson](https://github.com/PiJayson))
* Vladislav Kozulin ([@watislaf](https://github.com/watislaf))
