

# Techincal Devops Challenge

This repo contains a small "Hello World" webserver which simulates a small microservice

## Tasks


 - Create a docker image for the microservice. The smaller the image, the better.
 - Create all required resources in Kubernetes to expose the microservice to the public. Make sure that the microservice has access to a volume mounted in /tmp for storing temp data
 - Make sure that the health of the microservice is monitored from Kubernetes perspective
 - Create a K8S resource for scale up and down the microservice based on the CPU load
 - Create a Jenkins pipeline for deploying the microservice.
 - Describe how to retrieve metrics from the microservice like CPU usage, memory usage...
 - Describe how to retrieve the log from the microservice and how to store in a central location
