

# Technical Devops Challenge

This repo contains a small "Hello World" webserver which simulates a small microservice

## Tasks


 ### Create a docker image for the microservice. The smaller the image, the better.

We can build docker image with the following command, the image is 9.94MB

```
docker build -t hello:scratch -f Dockerfile .
```
* golang:alpine as builder
* scratch as final base image
* since cgo is disabled, Go doesn’t link with any system library. Since it doesn’t link with any system library, it can generate a static binary. Since it generates a static binary, that binary can work in the scratch image

 ### From security perspective, make sure that the generated docker image has a small attack surface

* The scratch image is the most minimal image in Docker. This is the base ancestor for all other images, The scratch image is blank to mitigate possible attach
* Create a non-priviledged user without login shell and home directory to run the process instead of root user
* In build stage, refer to a fixed version so we know what we are using in the future
* Expose port greater than 1024, no root priviledge is need to run

 ### Create all required resources in Kubernetes to expose the microservice to the public. Make sure that the microservice has access to a volume mounted in /tmp for storing temp data.

 * Refer to deployment.yaml, the resources created includes: Deployment, ConfigMap, HPA, Service, Ingress
 * We can also use helm charts like this one https://github.com/gruntwork-io/helm-kubernetes-services

 ### Use MESSAGES env variable to configure the message displayed by the server

 * Using env "MESSAGES", which reads value from ConfigMap message-config in deployment.yaml


 ### Make sure that the health of the microservice is monitored from Kubernetes perspective

 * using readiness and liveness probes, so kubernetes controller can monitor the pod if we need to kill and recreate the pod, or add/remove the pod from service


 ### Security wise, try to follow the best practices securing all the resources in Kubernetes when possible

 * we can create the container in a dedicated namespace

 ### Create a K8S resource for scale up and down the microservice based on the CPU load

 * Using HorizontalPodAutoscaler in deployment.yaml

 ### Create a Jenkins pipeline for deploying the microservice.

 * Check Jenkinsfile, jenkins-lib should be in a repo. we can use this to create common used functions for example slack notifaction, load source code
 * There is two way for jenkinsfile - imperative programming and declarative programming

 ### Describe how to retrieve metrics from the microservice like CPU usage, memory usage...

 * We can use metrics server collects resource metrics from Kubelets and exposes them in Kubernetes apiserver through Metrics API for use by Horizontal Pod Autoscaler and Vertical Pod Autoscaler. Metrics API can also be accessed by kubectl top, making it easier to debug autoscaling pipelines
 * For application metrics, we can get the metrics from api endpoint /metrics, then it can be captured by Prometheus, searched and displayed in Grafana
 * We can also use metricbeat/filebeat to collect data and ship it to the monitoring cluster

 ### Describe how to retrieve the logs from the microservice and how to store in a central location

 * For microservice we can use https://www.elastic.co/beats/ to send over to monitoring cluster
 * For data intensive microservice, we can use kafka to produce and consume logs with kafka brokers
 * Both logstash and fluentd can be used, Logstash is centralized while FluentD is decentralized. FluentD offers better performance than Logstash. I would prefer fluentd as it provides both active-active and active-passive deployment patterns for both availability and scale
