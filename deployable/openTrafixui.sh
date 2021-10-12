export SPRING_PROFILES_ACTIVE=dev,swagger
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/trafix?useSSL=false&&createDatabaseIfNotExist=true&&useUnicode=true&&characterEncoding=utf8
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=trafix
#export SPRING_DATASOURCE_URL=jdbc:mysql://89.19.30.126:3306/u0221508_trafix
#export SPRING_DATASOURCE_USERNAME=u0221508_ramazan
#export SPRING_DATASOURCE_PASSWORD=ra5699mo
export APPLICATION_FTPDIRECTORY=/home/trafix/Desktop/videos
export APPLICATION_AISCRIPTPATH=/home/trafix/AI/AI_Backend_http_multiple_2_GPUs
export APPLICATION_AISCRIPTNAME=multiple_pipelines.py
export APPLICATION_AISCRIPTENDPOINT=http://localhost:8088
export APPLICATION_LOGDIRECTORY=/home/trafix/log
export APPLICATION_REPORTINTERVAL=3
export APPLICATION_TRAFIXVIEWERJARLOCATION=/home/trafix/Desktop/trafix/trafixviewer-0.0.1-SNAPSHOT.jar
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/snap/vlc/current/usr/lib

java -jar trafix_v1.0.0.war
