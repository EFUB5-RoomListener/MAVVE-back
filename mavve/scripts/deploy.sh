#!/bin/bash

LOG_PATH=/home/ubuntu/deploy.log
ERROR_LOG_PATH=/home/ubuntu/deploy_err.log

BUILD_JAR=$(ls /home/ubuntu/app/mavve/build/libs/mavve-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)
DEPLOY_PATH=/home/ubuntu/app/

echo ">>> ✅ 빌드 파일명: $JAR_NAME" >> $LOG_PATH

echo ">>> 📦 이전 애플리케이션 중단 및 제거" >> $LOG_PATH
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -n "$CURRENT_PID" ]; then
  echo ">>> 실행중인 프로세스 종료: $CURRENT_PID" >> $LOG_PATH
  kill -15 $CURRENT_PID
  sleep 5
else
  echo ">>> 실행중인 애플리케이션이 없습니다." >> $LOG_PATH
fi

echo ">>> 📂 JAR 복사 중..." >> $LOG_PATH
cp $BUILD_JAR $DEPLOY_PATH

echo ">>> 🌱 환경변수(.env) 로딩..." >> $LOG_PATH
set -a
source /home/ubuntu/app/.env
set +a

echo ">>> 🔍 환경변수 확인" >> $LOG_PATH
env | grep DB_ >> $LOG_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo ">>> 🚀 애플리케이션 실행: $DEPLOY_JAR" >> $LOG_PATH
nohup java -jar $DEPLOY_JAR >> $LOG_PATH 2>> $ERROR_LOG_PATH &
