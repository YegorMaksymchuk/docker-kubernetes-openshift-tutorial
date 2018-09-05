# Docker demo
 
docker run --name=pod-demo -d -p 8081:8081 yemax/pod-demo:1

# Kubernetes demo

minikube start --vm 

minikube dashboard

minikube ip

kubectl run  --image=yemax/pod-demo:1 pod-demo --port=8081

kubectl get pods

kubectl logs pod-demo

kubectl expose deployment pod-demo --port=8081 --name=pod-demo --type=LoadBalancer

kubectl get services

minikube service pod-demo

kubectl cluster-info

# Openshift demo

oc cluster up

oc login -u developer -p developer

oc new-app yemax/pod-demo:1 --name=pod-demo

oc expose svc/pod-demo-oc

oc get pods

oc logs <pod name>

oc delete all -all