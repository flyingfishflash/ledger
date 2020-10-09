package net.flyingfishflash.ledger.transactions;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import net.flyingfishflash.ledger.commodities.data.Commodity;
import net.flyingfishflash.ledger.transactions.data.Entry;
import net.flyingfishflash.ledger.transactions.data.Transaction;

@Configuration
@ComponentScan
@EntityScan(basePackageClasses = {Transaction.class, Entry.class, Commodity.class})
public class TransactionConfiguration {}
