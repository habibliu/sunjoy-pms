# 基础镜像
FROM openjdk:17-jdk
LABEL maintainer=Habib


# 挂载目录
VOLUME /home/sunjoy
# 创建目录
RUN mkdir -p /home/sunjoy
# 指定路径
WORKDIR /home/sunjoy
COPY target/*.jar /home/sunjoy/app.jar


#docker run -e PARAMS="--server.port 9090"
ENV PARAMS="--server.port=8080 --spring.profiles.active=prod \
           --spring.cloud.nacos.discovery.server-addr=nacos-server.dev.svc.cluster.local.:8848 \
           --spring.cloud.nacos.config.server-addr=nacos-server.dev.svc.cluster.local:8848 \
           --spring.cloud.nacos.config.shared-configs[0]=application-prod.yaml \
           --spring.cloud.nacos.config.shared-configs[1]=sunjoy-system-prod.yaml \
           --spring.cloud.nacos.config.namespace=sunjoy --spring.cloud.nacos.config.file-extension=yaml"
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
#
ENTRYPOINT ["/bin/sh","-c","java -Dfile.encoding=utf8 -Djava.security.egd=file:/dev/./urandom -jar app.jar ${PARAMS}"]