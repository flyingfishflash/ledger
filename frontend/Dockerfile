FROM nginx:alpine
COPY ./docker/docker-entrypoint-custom.sh /docker-entrypoint-custom.sh
COPY ./docker/ledger.nginx.conf.template /etc/nginx/templates/ledger.conf.template
RUN rm -rf /usr/share/nginx/html/* && \
rm /etc/nginx/conf.d/default.conf && \
chmod +x /docker-entrypoint-custom.sh
COPY ./dist/ledger-frontend/ /usr/share/nginx/html/
ENTRYPOINT ["/docker-entrypoint-custom.sh"]
