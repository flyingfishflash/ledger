#!/bin/sh

# This script can be run as a non-root user (podman rootless mode)

# to use H2 in memory database (http://localhost:53113/h2-console):
# 1) remove the podman create command found at lines 19-30 that creates the ledger-postgresql-prd container
# 2) in the command that creates the ledger-backend-prd container replace lines 43-47 with the following:
# -e SPRING_PROFILES_ACTIVE: h2
# -e SPRING_H2_CONSOLE_SETTINGS_WEB_ALLOW_OTHERS: 'true'
# -e SPRING_DATASOURCE_PLATFORM: 'POSTGRESQL'
# -e SPRING_DATASOURCE_URL: jdbc:h2:mem:ledger;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=TRUE;DATABASE_TO_LOWER=true;MODE=PostgreSQL
# -e SPRING_DATASOURCE_USERNAME: ledger
# -e SPRING_DATASOURCE_PASSWORD: ledger

podman pod inspect ledger-prd || podman pod create --name=ledger-prd
podman network inspect ledger-prd || podman network create ledger-prd

# -v postgresql_data:/var/lib/postgres/data
podman create \
 --pod=ledger-prd \
 --name=ledger-postgresql-prd \
 -e POSTGRES_USER=ledger \
 -e POSTGRES_PASSWORD=ledger \
 -e POSTGRES_DB=ledger \
 --net ledger-prd \
 --network-alias ledger-postgresql-prd \
 --log-driver=journald \
 --log-opt=tag=podman/ledger-postgresql-prd \
 -p 53113:5432 \
 docker.io/postgres:14-alpine

# If the application endpoint is proxied (traefik, etc) set the below environment variable with your URI
#  -e API_SERVER=https://ledger.my-domain.tld \
# If the application endpoint isn't proxied, set the below environment variable to include the external port
#  -e API_SERVER=http://localhost:53111 \
# Otherwise, the default value of API_SERVER is http://localhost:8181
podman create \
 --pod=ledger-prd \
 --name=ledger-backend-prd \
 -e API_SERVER=http://localhost:53111 \
 -e JDK_JAVA_OPTIONS='-Djava.security.egd=file:/dev/./urandom -Duser.timezone="UTC"' \
 -e SPRING_JPA_SHOW_SQL=false \
 -e SPRING_PROFILES_ACTIVE=postgresql \
 -e SPRING_DATASOURCE_URL=jdbc:postgresql://ledger-postgresql-prd:5432/ledger \
 -e SPRING_DATASOURCE_USERNAME=ledger \
 -e SPRING_DATASOURCE_PASSWORD=ledger \
 -e SPRING_DATASOURCE_PLATFORM=POSTGRESQL \
 --net ledger-prd \
 --network-alias ledger-backend-prd \
 --log-driver=journald \
 --log-opt=tag=podman/ledger-backend-prd \
 -p 53111:8181 \
 --healthcheck-command '/bin/sh -c "wget http://127.0.0.1:8181/api/v1/ledger/actuator/info -T 30 -q -O -"' \
 --healthcheck-interval 60s \
 --healthcheck-timeout 5s \
 --healthcheck-start-period 15s \
 --healthcheck-retries 3 \
 registry.gitlab.com/flyingfishflash/ledger:backend-latest

podman create \
 --pod=ledger-prd \
 --name=ledger-frontend-prd \
 -e UPSTREAM_SERVER=ledger-backend-prd:8181 \
 --net ledger-prd \
 --network-alias ledger-frontend-prd \
 --log-driver=journald \
 --log-opt=tag=podman/ledger-frontend-prd \
 -p 53112:80 \
 registry.gitlab.com/flyingfishflash/ledger:backend-latest

podman generate systemd ledger-prd --name --new --files --stop-timeout=60 --restart-policy=no

podman generate systemd ledger-backend-prd --name --new --files --stop-timeout=60 --restart-policy=no \
--after container-ledger-postgresql-prd.service \
--requires container-ledger-postgresql-prd.service

podman generate systemd ledger-frontend-prd --name --new --files --stop-timeout=60 --restart-policy=no \
--after container-ledger-backend-prd.service \
--requires container-ledger-backend-prd.service

mv ./*.service ~/.config/systemd/user/

systemctl --user daemon-reload
systemctl --user enable pod-ledger-prd.service
systemctl --user restart pod-ledger-prd.service
