# Ledger

[![Build Status (main)](https://drone.flyingfishflash.net/api/badges/flyingfishflash/ledger/status.svg)](https://drone.flyingfishflash.net/flyingfishflash/ledger) [![Quality Gate Status](https://sonarqube.flyingfishflash.net/api/project_badges/measure?project=ledger&metric=alert_status&token=0f4facebb37a49e9103010bcef828ae6cbf5dee1)](https://sonarqube.flyingfishflash.net/dashboard?id=ledger) [![Coverage](https://sonarqube.flyingfishflash.net/api/project_badges/measure?project=ledger&metric=coverage&token=0f4facebb37a49e9103010bcef828ae6cbf5dee1)](https://sonarqube.flyingfishflash.net/dashboard?id=ledger)

## Description

A Java based bookkeeping engine and API server with an Angular based web application for personal finance management.

## Run

- Several Intellij run [configurations](.idea/runConfigurations) are included.
- Container [stacks](docs/installation)
  - Shell script to create a rootless Podman pod
  - Docker composition (untested)

## API

* [http://localhost:52300/](http://localhost:52300/api/)
* Documentation: [http://localhost:52300/swagger-ui/](http://localhost:52300/swagger-ui/)

## Web Application

* [http://localhost:52301](https://localhost:52301)
