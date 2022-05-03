#bin/bash

sudo apt-get update
sudo snap install docker
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
docker pull gradle
