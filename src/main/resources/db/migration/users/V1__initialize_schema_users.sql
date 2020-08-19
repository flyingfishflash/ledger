CREATE sequence hibernate_sequence START WITH 1 increment BY 1;

CREATE TABLE account (
	id BIGINT NOT NULL
	,category VARCHAR(255)
	,code VARCHAR(255)
	,currency VARCHAR(255)
	,description VARCHAR(2048)
	,discriminator VARCHAR(255) NOT NULL
	,guid VARCHAR(255)
	,hidden boolean
	,longName VARCHAR(4096)
	,name VARCHAR(255)
	,normalBalance VARCHAR(255)
	,note VARCHAR(4096)
	,parentId BIGINT
	,placeholder boolean
	,taxRelated boolean
	,treeLeft BIGINT NOT NULL
	,treeLevel BIGINT NOT NULL
	,treeRight BIGINT NOT NULL
	,type VARCHAR(255)
	,commodity_id BIGINT
	,CONSTRAINT account_pk PRIMARY KEY (id)
	);

CREATE TABLE commodity (
	id BIGINT NOT NULL
	,cusip VARCHAR(9)
	,custom_identifier VARCHAR(255)
	,fraction INTEGER
	,fullname VARCHAR(255)
	,guid VARCHAR(32)
	,mnemonic VARCHAR(255)
	,namespace VARCHAR(255)
	,quote_remote boolean
	,quote_remote_source VARCHAR(255)
	,CONSTRAINT commodity_pk PRIMARY KEY (id)
	);

CREATE TABLE entry (
	id BIGINT NOT NULL
	,action VARCHAR(255)
	,enterDate TIMESTAMP
	,guid VARCHAR(32)
	,memo VARCHAR(255)
	,quantity DECIMAL(38, 18)
	,quantitySigned DECIMAL(38, 18)
	,type VARCHAR(255)
	,value_currency VARCHAR(3)
	,value DECIMAL(38, 18)
	,valueSigned DECIMAL(38, 18)
	,account_id BIGINT
	,transaction_id BIGINT
	,CONSTRAINT entry_pk PRIMARY KEY (id)
	);

CREATE TABLE price (
	id BIGINT NOT NULL
	,currency VARCHAR(3)
	,DATE TIMESTAMP
	,denominator BIGINT
	,guid VARCHAR(32)
	,numerator BIGINT
	,price DECIMAL(38, 18)
	,source VARCHAR(255)
	,type VARCHAR(255)
	,commodity_id BIGINT
	,CONSTRAINT price_pk PRIMARY KEY (id)
	);

CREATE TABLE transaction (
	id BIGINT NOT NULL
	,currency VARCHAR(255)
	,description VARCHAR(255)
	,enterDate TIMESTAMP
	,guid VARCHAR(32)
	,num VARCHAR(255)
	,postDate DATE
	,CONSTRAINT transaction_pk PRIMARY KEY (id)
	);

ALTER TABLE account ADD CONSTRAINT account_guid_uk UNIQUE (guid);

ALTER TABLE commodity ADD CONSTRAINT commodity_namespace_mnemonic_uk UNIQUE (
	namespace
	,mnemonic
	);

ALTER TABLE commodity ADD CONSTRAINT commodity_guid_uk UNIQUE (guid);

ALTER TABLE entry ADD CONSTRAINT entry_guid_uk UNIQUE (guid);

ALTER TABLE price ADD CONSTRAINT price_guid_uk UNIQUE (guid);

ALTER TABLE transaction ADD CONSTRAINT transaction_guid_uk UNIQUE (guid);

ALTER TABLE account ADD CONSTRAINT account_commodity_id_fk FOREIGN KEY (commodity_id) REFERENCES commodity;

ALTER TABLE entry ADD CONSTRAINT entry_account_id_fk FOREIGN KEY (account_id) REFERENCES account;

ALTER TABLE entry ADD CONSTRAINT entry_transaction_id_fk FOREIGN KEY (transaction_id) REFERENCES transaction;

ALTER TABLE price ADD CONSTRAINT price_commodity_id_fk FOREIGN KEY (commodity_id) REFERENCES commodity;
