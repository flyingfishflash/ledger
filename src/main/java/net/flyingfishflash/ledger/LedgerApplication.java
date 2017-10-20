package net.flyingfishflash.ledger;

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

import static net.flyingfishflash.ledger.domain.AccountSide.DEBIT;



import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



@SpringBootApplication
@ComponentScan("net.flyingfishflash.ledger")
public class LedgerApplication {
	
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

//	@Bean
//	public HibernateJpaSessionFactoryBean sessionFactory() {
//	    return new HibernateJpaSessionFactoryBean();
//	}


    @PersistenceContext
    EntityManager entityManager;
    
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

}
