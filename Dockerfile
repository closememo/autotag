FROM reg.bitgadak.com/closememo/autotag-base:0.1

EXPOSE 10084

RUN mkdir -p /home/deployer/deploy
COPY ./build/libs/autotag.jar /home/deployer/deploy

ENTRYPOINT java -jar -Dspring.profiles.active=$PROFILE /home/deployer/deploy/autotag.jar
