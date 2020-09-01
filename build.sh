#!/usr/bin/env bash

mvn clean package -Dmaven.test.skip=true -U

docker build -t registry.cn-hangzhou.aliyuncs.com/gz_projects/romeole .

docker push registry.cn-hangzhou.aliyuncs.com/gz_projects/romeole