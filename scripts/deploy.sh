#!/bin/bash

# 빌드된 JAR 파일 경로 설정
BUILD_JAR=$(ls /home/ubuntu/app/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)

# 로그 파일에 빌드 파일명 기록
echo ">>> build 파일명: $JAR_NAME" >> /home/ubuntu/deploy.log

# 빌드 파일을 배포 경로로 복사
echo ">>> build 파일 복사" >> /home/ubuntu/deploy.log
DEPLOY_PATH=/home/ubuntu/app/
cp $BUILD_JAR $DEPLOY_PATH

# 현재 실행 중인 애플리케이션을 모두 종료
echo ">>> 현재 실행중인 애플리케이션 pid 확인 후 일괄 종료" >> /home/ubuntu/deploy.log
sudo ps -ef | grep java | awk '{print $2}' | xargs kill -15

# 배포된 JAR 파일 실행
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo ">>> DEPLOY_JAR 배포" >> /home/ubuntu/deploy.log
echo ">>> $DEPLOY_JAR의 $JAR_NAME를 실행합니다" >> /home/ubuntu/deploy.log
nohup java -jar $DEPLOY_JAR >> /home/ubuntu/deploy.log 2> /home/ubuntu/deploy_err.log &
