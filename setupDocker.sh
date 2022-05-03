#bin/bash

if [[ $( docker container ls | cut -d ' '  -f  1 | tail -1 ) != "CONTAINER" ]]; then
  docker stop $( docker container ls | cut -d ' '  -f  1 | tail -1 )
  docker rm $( docker container ls | cut -d ' '  -f  1 | tail -1 )
fi;

docker build ./ -t elevators
docker run -d --restart unless-stopped -p 6778:6778 elevators  gradle run &