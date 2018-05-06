# Ledger - Java-based double-entry bookkeeping engine for personal finance management


For the time being this simply allows the creating and modification of a hierarchy of accounts.


### Build

Clone my modified version of the NestedJ library, build it and add it to your local maven repostory:
```shell
git clone https://gitlab.com/rollenwiese/nestedj.git
cd nestedj
mvn clean install
mvn install:install-file -Dfile=target/NestedJ-2.1.2.RELEASE.jar -DpomFile=pom.xml
cd ..
git clone https://gitlab.com/rollenwiese/ledger.git
cd ledger
mvn clean install
```
