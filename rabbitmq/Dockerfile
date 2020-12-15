FROM rabbitmq:3-management

USER root:root

RUN rabbitmq-plugins enable --offline rabbitmq_management
RUN rabbitmq-plugins enable --offline rabbitmq_stomp
RUN rabbitmq-plugins enable --offline rabbitmq_web_stomp

COPY rabbitmq.conf /etc/rabbitmq/rabbitmq.conf