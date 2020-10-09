# Ledger - Java-based double-entry bookkeeping engine for personal finance management


[![pipeline status](https://gitlab.com/rollenwiese/ledger/badges/master/pipeline.svg)](https://gitlab.com/rollenwiese/ledger/commits/master)

### Build

A docker-compose file is supplied which you can use via:
```shell
docker-compose -f docker-compose.yaml up -d
```

The application will be available at [http://localhost:9090](http://localhost:9090)

### Notes

For the time being this tool simply allows the creation and modification of a hierarchy of accounts.

There are two interfaces to the account structure:

* Server rendered interface via Thymeleaf templating
* RESTful API


SSR: [http://localhost:9090/ledger/accounts](http://localhost:9090/ledger/accounts)

API: [http://localhost:9090/swagger](http://localhost:9090/swagger)
