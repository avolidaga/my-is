sudo docker build -t tinder.jar .
sudo docker network prune -f
sudo docker-compose up --force-recreate --remove-orphans