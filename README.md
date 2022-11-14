# GPN Intelligence Cup

## Details

RESTful Java service to get the full name of the VK user, and attribute of the VK group member.
VKontakte API is used to work with VK.

## Available Scripts

### `docker build -t gpn .`
Build docker image from a Dockerfile. 

### `docker-compose up`
Create and start containers.

### `kubectl -n gpn-dev apply -f k8s.yaml`
Apply configuration from a file.\
Before you need to start minikube `minikube start`,
create gpn-dev namespace `kubectl create namespace gpn-dev`.
After that check image name of app (gpn-...) `kubectl get po`
and forward ports to check on browser `kubectl -n gpn-dev port-forward image-name 8080:8080`.

Check on Swagger-UI
[localhost](http://localhost:8080/swagger-ui/index.html)
