FROM amazoncorretto:21-al2-jdk

# ENV JAVA_XMS             <set initial Java heap size>
# ENV JAVA_XMX             <set maximum Java heap size>
# ENV PORT                 <port to run server>
# ENV DATABASE_HOST        <postgres server host name>
# ENV DATABASE_PORT        <postgres server port>
# ENV DATABASE_SCHEMA      <postgres schema>
# ENV DATABASE_USER        <postgres username>
# ENV DATABASE_PASSWORD    <postgres password>
# ENV REDIS_HOST           <redis server host name>
# ENV REDIS_PORT           <redis server port>
# ENV SEARCH_AWS_SIGNING   <if AWS signing is required to connect to OpenSearch. true | false>
# ENV SEARCH_AWS_REGION    <AWS region where OpenSearchis running>
# ENV SEARCH_HOSTS         <OpenSearch domain endpoint>
# ENV SEARCH_PROTOCOL      <OpenSearch connection protocol. http | https>

ADD infrastructure/spring-boot/target/*.jar /opt/orders-service.jar

RUN bash -c 'touch /opt/orders-service.jar'

RUN echo "#!"

RUN echo "#!/usr/bin/env bash" > /opt/entrypoint.sh && \
    echo "" >> /opt/entrypoint.sh && \
    echo "echo \"===============================================\" " >> /opt/entrypoint.sh && \
    echo "echo \"JAVA_XMS: \$JAVA_XMS \" " >> /opt/entrypoint.sh && \
    echo "echo \"JAVA_XMX: \$JAVA_XMX \" " >> /opt/entrypoint.sh && \
    echo "echo \"===============================================\" " >> /opt/entrypoint.sh && \
    echo "echo \"PORT: \$PORT \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_HOST: \$DATABASE_HOST \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_PORT: \$DATABASE_PORT \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_SCHEMA: \$DATABASE_SCHEMA \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_USER: \$DATABASE_USER \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_PASSWORD: \$DATABASE_PASSWORD \" " >> /opt/entrypoint.sh && \
    echo "echo \"REDIS_HOST: \$REDIS_HOST \" " >> /opt/entrypoint.sh && \
    echo "echo \"REDIS_PORT: \$REDIS_PORT \" " >> /opt/entrypoint.sh && \
    echo "echo \"SEARCH_AWS_SIGNING: \$SEARCH_AWS_SIGNING \" " >> /opt/entrypoint.sh && \
    echo "echo \"SEARCH_AWS_REGION: \$SEARCH_AWS_REGION \" " >> /opt/entrypoint.sh && \
    echo "echo \"SEARCH_HOSTS: \$SEARCH_HOSTS \" " >> /opt/entrypoint.sh && \
    echo "echo \"SEARCH_PROTOCOL: \$SEARCH_PROTOCOL \" " >> /opt/entrypoint.sh && \
    echo "echo \"AWS_ACCESS_KEY_ID: \$AWS_ACCESS_KEY_ID \" " >> /opt/entrypoint.sh && \
    echo "echo \"AWS_SECRET_ACCESS_KEY: \$AWS_SECRET_ACCESS_KEY \" " >> /opt/entrypoint.sh && \
    echo "echo \"AWS_REGION: \$AWS_REGION \" " >> /opt/entrypoint.sh && \
    echo "echo \"PRODUCTS_SERVICE_BASEURI: \$PRODUCTS_SERVICE_BASEURI \" " >> /opt/entrypoint.sh && \
    echo "echo \"USERS_SERVICE_BASEURI: \$USERS_SERVICE_BASEURI \" " >> /opt/entrypoint.sh && \
    echo "echo \"===============================================\" " >> /opt/entrypoint.sh && \
    echo "" >> /opt/entrypoint.sh && \
    echo "echo \"singleServerConfig:\" > /opt/redisson.yaml " >> /opt/entrypoint.sh && \
    echo "echo \"  address: redis://\$REDIS_HOST:\$REDIS_PORT\" >> /opt/redisson.yaml " >> /opt/entrypoint.sh && \
    echo "" >> /opt/entrypoint.sh && \
    echo "java -Xms\$JAVA_XMS -Xmx\$JAVA_XMX \
        -Dserver.port=\$PORT \
        -Dmanagement.server.port=\$PORT \
        -Dspring.datasource.host=\$DATABASE_HOST \
        -Dspring.datasource.port=\$DATABASE_PORT \
        -Dspring.datasource.schemaName=\$DATABASE_SCHEMA \
        -Dspring.datasource.username=\$DATABASE_USER \
        -Dspring.datasource.password=\$DATABASE_PASSWORD \
        -Dspring.redis.host=\$REDIS_HOST \
        -Dspring.jpa.properties.hibernate.cache.redisson.config=/opt/redisson.yaml \
        -Dspring.jpa.properties.hibernate.search.backend.aws.signing.enabled=\$SEARCH_AWS_SIGNING \
        -Dspring.jpa.properties.hibernate.search.backend.aws.region=\$SEARCH_AWS_REGION \
        -Dspring.jpa.properties.hibernate.search.backend.hosts=\$SEARCH_HOSTS \
        -Dspring.jpa.properties.hibernate.search.backend.protocol=\$SEARCH_PROTOCOL \
        -Decomm.service.products.baseUri=\$PRODUCTS_SERVICE_BASEURI \
        -Decomm.service.users.baseUri=\$USERS_SERVICE_BASEURI \
        -jar /opt/orders-service.jar" >> /opt/entrypoint.sh

RUN chmod 755 /opt/entrypoint.sh

EXPOSE ${PORT}

ENTRYPOINT [ "/opt/entrypoint.sh" ]

