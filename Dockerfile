FROM amazoncorretto:21-al2-jdk
LABEL AUTHOR = 'Sergio Exposito'
LABEL EMAIL = 'sjexpos@gmail.com'

# ENV JAVA_XMS             <set initial Java heap size>
# ENV JAVA_XMX             <set maximum Java heap size>
# ENV PORT                 <port to run server>
# ENV MANAGEMENT_PORT
# ENV MONITORING_URL
# ENV DATABASE_HOST        <postgres server host name>
# ENV DATABASE_PORT        <postgres server port>
# ENV DATABASE_SCHEMA      <postgres schema>
# ENV DATABASE_USER        <postgres username>
# ENV DATABASE_PASSWORD    <postgres password>
# ENV REDIS_HOST           <redis server host name>
# ENV REDIS_PORT           <redis server port>
# ENV OPENSEARCH_CONN
# ENV TRACING_URL
# ENV PRODUCTS_SERVICE_BASEURI
# ENV USERS_SERVICE_BASEURI
# ENV SCHEDULING_ENABLED   true | false

ADD infrastructure/spring-boot/target/*.jar /opt/orders-service.jar

RUN bash -c 'touch /opt/orders-service.jar'

RUN echo "#!/usr/bin/env bash" > /opt/entrypoint.sh && \
    echo "" >> /opt/entrypoint.sh && \
    echo "echo \"===============================================\" " >> /opt/entrypoint.sh && \
    echo "echo \"JAVA_XMS: \$JAVA_XMS \" " >> /opt/entrypoint.sh && \
    echo "echo \"JAVA_XMX: \$JAVA_XMX \" " >> /opt/entrypoint.sh && \
    echo "echo \"===============================================\" " >> /opt/entrypoint.sh && \
    echo "echo \"PORT: \$PORT \" " >> /opt/entrypoint.sh && \
    echo "echo \"MANAGEMENT_PORT: \$MANAGEMENT_PORT \" " >> /opt/entrypoint.sh && \
    echo "echo \"MONITORING_URL: \$MONITORING_URL\" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_HOST: \$DATABASE_HOST \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_PORT: \$DATABASE_PORT \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_SCHEMA: \$DATABASE_SCHEMA \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_USER: \$DATABASE_USER \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_PASSWORD: \$DATABASE_PASSWORD \" " >> /opt/entrypoint.sh && \
    echo "echo \"REDIS_HOST: \$REDIS_HOST \" " >> /opt/entrypoint.sh && \
    echo "echo \"REDIS_PORT: \$REDIS_PORT \" " >> /opt/entrypoint.sh && \
    echo "echo \"OPENSEARCH_CONN: \$OPENSEARCH_CONN \" " >> /opt/entrypoint.sh && \
    echo "echo \"TRACING_URL: \$TRACING_URL \" " >> /opt/entrypoint.sh && \
    echo "echo \"PRODUCTS_SERVICE_BASEURI: \$PRODUCTS_SERVICE_BASEURI \" " >> /opt/entrypoint.sh && \
    echo "echo \"USERS_SERVICE_BASEURI: \$USERS_SERVICE_BASEURI \" " >> /opt/entrypoint.sh && \
    echo "echo \"===============================================\" " >> /opt/entrypoint.sh && \
    echo "" >> /opt/entrypoint.sh && \
    echo "echo \"singleServerConfig:\" > /opt/redisson.yaml " >> /opt/entrypoint.sh && \
    echo "echo \"  address: redis://\$REDIS_HOST:\$REDIS_PORT\" >> /opt/redisson.yaml " >> /opt/entrypoint.sh && \
    echo "" >> /opt/entrypoint.sh && \
    echo "java -Xms\$JAVA_XMS -Xmx\$JAVA_XMX \
        -Dserver.port=\$PORT \
        -Dmanagement.server.port=\$MANAGEMENT_PORT \
        -Dspring.boot.admin.client.url=\$MONITORING_URL \
        -Dspring.datasource.host=\$DATABASE_HOST \
        -Dspring.datasource.port=\$DATABASE_PORT \
        -Dspring.datasource.schemaName=\$DATABASE_SCHEMA \
        -Dspring.datasource.username=\$DATABASE_USER \
        -Dspring.datasource.password=\$DATABASE_PASSWORD \
        -Dspring.data.redis.host=\$REDIS_HOST \
        -Dspring.data.redis.port=\$REDIS_PORT \
        -Dspring.jpa.properties.hibernate.cache.redisson.config=/opt/redisson.yaml \
        \$OPENSEARCH_CONN \
        -Decomm.service.tracing.url=\$TRACING_URL \
        -Decomm.service.products.baseUri=\$PRODUCTS_SERVICE_BASEURI \
        -Decomm.service.users.baseUri=\$USERS_SERVICE_BASEURI \
        -Decomm.service.orders.scheduling.enabled=\$SCHEDULING_ENABLED \
        -jar /opt/orders-service.jar" >> /opt/entrypoint.sh

RUN chmod 755 /opt/entrypoint.sh

EXPOSE ${PORT}

ENTRYPOINT [ "/opt/entrypoint.sh" ]

