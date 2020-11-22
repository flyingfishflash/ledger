FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/* && \
rm /etc/nginx/conf.d/default.conf
COPY ledger.nginx.conf.template /etc/nginx/templates/ledger.conf.template
COPY ./dist/ledger-frontend/ /usr/share/nginx/html/
