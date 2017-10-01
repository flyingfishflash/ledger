package net.flyingfishflash.ledger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

import com.google.common.util.concurrent.Service;

import net.flyingfishflash.ledger.domain.ChartOfAccounts;
import net.flyingfishflash.ledger.domain.ChartOfAccountsBuilder;
import net.flyingfishflash.ledger.domain.Ledger;
import net.flyingfishflash.ledger.domain.Transaction;
import net.flyingfishflash.ledger.domain.TrialBalanceResult;

//import net.flyingfishflash.ledger.domain.treeconcept.Tree;
//import net.flyingfishflash.ledger.domain.treeconcept.Node;
//import net.flyingfishflash.ledger.domain.treeconcept.TraversalStrategy;

import net.flyingfishflash.ledger.domain.nestedset.Node;
import net.flyingfishflash.ledger.domain.nestedset.NodeDAO;
import net.flyingfishflash.ledger.domain.nestedset.NodeService;
import net.flyingfishflash.ledger.domain.nestedset.NodeServiceImpl;

import static net.flyingfishflash.ledger.domain.AccountSide.DEBIT;

import java.math.BigDecimal;
import java.util.Iterator;


@SpringBootApplication
//@EnableAutoConfiguration
@ComponentScan("net.flyingfishflash.ledger")
public class LedgerApplication {
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(LedgerApplication.class, args);
		
		//NodeServiceImpl service = new msg
		
		//showLedger();
		

	}		

	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory() {
	    return new HibernateJpaSessionFactoryBean();
	}

	
	
	public static void showLedger( ) {
        String cashAccountNumber = "000001";
        String checkingAccountNumber = "000002";
        String accountsReceivableAccountNumber = "000003";

        // Setup ledger
        ChartOfAccounts coa = ChartOfAccountsBuilder.create()
                .addAccount(cashAccountNumber, "Cash", DEBIT)
                .addAccount(checkingAccountNumber, "Checking", DEBIT)
                .addAccount(accountsReceivableAccountNumber, "Accounts Receivable", DEBIT)
                .build();
        Ledger ledger = new Ledger(coa);

        // Accounts Receivable 35 was settled with cash 10 and wire transfer 25
        Transaction t = ledger.createTransaction(null)
                .debit(new BigDecimal(10), cashAccountNumber)
                .debit(new BigDecimal(25), checkingAccountNumber)
                .credit(new BigDecimal(35), accountsReceivableAccountNumber)
                .build();
        ledger.commitTransaction(t);

        // Print ledger
        System.out.println(ledger.toString());

        // Print trial balance
        TrialBalanceResult trialBalanceResult = ledger.computeTrialBalance();
        System.out.println(trialBalanceResult.toString());
	}

/*	
	
public static void tree() {

	Tree tree = new Tree();

    tree.addNode("Root");
    tree.addNode("Assets", "Root");
    tree.addNode("Liabilities", "Root");
    tree.addNode("Current Liabilities", "Liabilities");
    tree.addNode("Fixed Liabilities", "Liabilities");
    tree.addNode("Transient Liabilities", "Liabilities");
    tree.addNode("Income", "Root");
    tree.addNode("Expense", "Root");
    tree.addNode("Equity", "Root");
    tree.addNode("Financial Assets", "Assets");
    tree.addNode("Fixed Assets", "Assets");

    tree.display("Root");

    System.out.println("n***** DEPTH-FIRST ITERATION *****");

    // Default traversal strategy is 'depth-first'
    Iterator<Node> depthIterator = tree.iterator("Root");

    while (depthIterator.hasNext()) {
        Node node = depthIterator.next();
        System.out.println(node.getIdentifier());
    }

    System.out.println("n***** BREADTH-FIRST ITERATION *****");

    Iterator<Node> breadthIterator = tree.iterator("Root", TraversalStrategy.BREADTH_FIRST);

    while (breadthIterator.hasNext()) {
        Node node = breadthIterator.next();
        System.out.println(node.getIdentifier()); }
	}
*/

}
