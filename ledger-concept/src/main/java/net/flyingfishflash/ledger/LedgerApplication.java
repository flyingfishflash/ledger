package net.flyingfishflash.ledger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

//import com.google.common.util.concurrent.Service;

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
import net.flyingfishflash.ledger.domain.nestedset.NodeDAOImpl;
import net.flyingfishflash.ledger.domain.nestedset.NodeService;
import net.flyingfishflash.ledger.domain.nestedset.NodeServiceImpl;

import static net.flyingfishflash.ledger.domain.AccountSide.DEBIT;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@SpringBootApplication
@ComponentScan("net.flyingfishflash.ledger")
public class LedgerApplication {
	
	//@Autowired
	public static NodeService service;
	@Autowired
	private NodeDAO dao;
	
	private static Integer defaultValue = 10;

	public static void init() throws Exception {
	    if (service.findRoot() == null) {
	        Node root = service.createNode();
	        root.makeRoot();
	        service.save(root);
	        service.addChildAsLast(root, "child1", defaultValue);
	    }
		
	}
	
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(LedgerApplication.class, args);

		//NodeDAO nDao = new NodeDAOImpl();
		//List<Node> nodes = new ArrayList<Node>(nDao.findWholeTree());
		
		//	init();
		//showLedger();

/*		
		Node mynode = new Node();
		NodeService service = new NodeServiceImpl();
		mynode = service.createNode();
		mynode.makeRoot();
		System.out.println(mynode.getId());
		System.out.println(mynode.getLft());
		System.out.println(mynode.getRgt());
		System.out.println(mynode.getName());
*/		
	

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
