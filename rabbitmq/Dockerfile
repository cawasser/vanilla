FROM rabbitmq:3.8-management

WORKDIR /

ADD rabbitmq.conf /etc/rabbitmq/
ADD definitions.json /etc/rabbitmq/

RUN chown rabbitmq:rabbitmq /etc/rabbitmq/rabbitmq.conf /etc/rabbitmq/definitions.json

ENTRYPOINT ["rabbitmq-server"]