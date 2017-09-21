package net.flyingfishflash.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.Iterator;

@EnableAutoConfiguration
@ComponentScan("net.flyingfishflash.ledger")
@SpringBootApplication
public class LedgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LedgerApplication.class, args);
/*		
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
            System.out.println(node.getIdentifier());
        }
*/
	}

}
