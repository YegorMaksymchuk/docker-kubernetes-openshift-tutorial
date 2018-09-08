# Docker demo
 
docker run --name=pod-demo -d -p 8081:8081 yemax/pod-demo:1

# Kubernetes demo

minikube start 

minikube dashboard

kubectl run  --image=yemax/pod-demo:1 pod-demo --port=8081

kubectl get pods

kubectl logs pod-demo

kubectl expose deployment pod-demo --port=8081 --name=pod-demo --type=LoadBalancer

kubectl get services

minikube service pod-demo

minikube stop

# Openshift demo

oc cluster up

oc login -u developer -p developer

oc new-project qa-fest-2018 

oc new-app yemax/pod-demo:1 --name=pod-demo

oc expose svc/pod-demo-oc

oc get pods

oc logs <pod name>

oc delete all -all

oc delete project qa-fest-2018

# Fabric8 Kubernetes (Openshift) client deployment demo


cd deployment

mvn test
