#!/bin/bash

echo ########### Building jar file ###########
gradle build

REGISTRY=[ INSERT_AWS_ACCOUNT_ID ].dkr.ecr.eu-north-1.amazonaws.com
REPO=userrating
TAG=latest
CLUSTER_NAME=user-rating-api
PROFILE=prod
AWS_PROFILE=[ INSERT_AWS_CLI_PROFILE_NAME ]

echo ########### Update local kubectl config ###########
aws eks --region eu-north-1 update-kubeconfig --name ${CLUSTER_NAME} --profile ${AWS_PROFILE}

echo ########### Building docker file ###########
docker build -f Dockerfile.${PROFILE} -t ${REGISTRY}/${REPO} . --platform=linux/amd64

echo ########### Authenticating docker against AWS ECR repository ###########
aws ecr get-login-password --region eu-north-1 --profile ${AWS_PROFILE} | docker login --username AWS --password-stdin ${REGISTRY}

echo ########### Pushing build to repository ###########
docker push ${REGISTRY}/${REPO}:${TAG}

echo ########### Deploying to Kubernetes ###########
kubectl apply -f k8/deployment.yaml

