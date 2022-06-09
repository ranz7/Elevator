# Elevator Emulator ![](https://us-central1-progress-markdown.cloudfunctions.net/progress/1042) [![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Franz7%2FElevator%2F&count_bg=%237AAA56&title_bg=%236F1C1C&icon=github.svg&icon_color=%23C17878&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
This project simulates elevators in a building. The project is divided into two parts, the Server part that manipulates
objects and the Client part that draws them in real time.

https://app.creately.com/d/XYkWBnkDy0x/view
___
 
## Install

Download source files.

```bat
  git clone https://github.com/ranz7/Elevator.git # install
  cd ./Elevator
  chmod +x prepare_env.sh
  ./prepare_env.sh
```

if you don't have git, just download source
from [this page](https://github.com/watislaf/chessbot/releases/tag/V1.0.1600Elo).
____

## Gradle Build / Run local
To build and run project u can either use gradle (if u have installed one)

```bat
  ./gradlew :app:build  
  ./gradlew :app:run  
  ./gradlew :window:build  
  ./gradlew :window:run 
 ```

If it cant find jdk 17 u can add flag -Dorg.gradle.java.home="PATH_TO/openjdk-17.0.2" 
_____

### Authors

* Radek Mysliwiec ([@ranz7](https://github.com/ranz7))  100%
* Michał Miziołek ([@PiJayson](https://github.com/PiJayson)) 100%
* Vladislav Kozulin ([@watislaf](https://github.com/watislaf)) 100%
 
