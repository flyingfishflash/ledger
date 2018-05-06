package net.flyingfishflash.ledger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountTreeDiscriminator;
import pl.exsio.nestedj.delegate.NestedNodeInserter;
import pl.exsio.nestedj.delegate.NestedNodeInserterImpl;
import pl.exsio.nestedj.delegate.NestedNodeMover;
import pl.exsio.nestedj.delegate.NestedNodeMoverImpl;
import pl.exsio.nestedj.delegate.NestedNodeRebuilder;
import pl.exsio.nestedj.delegate.NestedNodeRebuilderImpl;
import pl.exsio.nestedj.delegate.NestedNodeRemover;
import pl.exsio.nestedj.delegate.NestedNodeRemoverImpl;
import pl.exsio.nestedj.delegate.NestedNodeRetriever;
import pl.exsio.nestedj.delegate.NestedNodeRetrieverImpl;
import pl.exsio.nestedj.repository.NestedNodeRepositoryImpl;

@Configuration
public class LedgerConfiguration {

    @PersistenceContext
    EntityManager em;

    @Bean
    public NestedNodeRepositoryImpl<AccountNode> repository() {
        NestedNodeRepositoryImpl<AccountNode> repository = new NestedNodeRepositoryImpl<>();
        AccountTreeDiscriminator treeDiscriminator = new AccountTreeDiscriminator();
        NestedNodeInserter<AccountNode> inserter = new NestedNodeInserterImpl<>(em, treeDiscriminator);
        NestedNodeMover<AccountNode> mover = new NestedNodeMoverImpl<>(em, treeDiscriminator);
        NestedNodeRetriever<AccountNode> retriever = new NestedNodeRetrieverImpl<>(em, treeDiscriminator);
        NestedNodeRebuilder<AccountNode> rebuilder = new NestedNodeRebuilderImpl<>(em, treeDiscriminator, inserter, retriever);
        NestedNodeRemover<AccountNode> remover = new NestedNodeRemoverImpl<>(em, treeDiscriminator);

        repository.setInserter(inserter);
        repository.setMover(mover);
        repository.setRebuilder(rebuilder);
        repository.setRetriever(retriever);
        repository.setRemover(remover);

        return repository;
    }

}