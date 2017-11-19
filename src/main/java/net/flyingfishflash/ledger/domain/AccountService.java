package net.flyingfishflash.ledger.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.flyingfishflash.ledger.common.IdentifierFactory;

@Service
@Transactional
public class AccountService {
	
    public AccountNode newAccountNode(AccountNode p) {
    	AccountNode accountNode = new AccountNode();
    	accountNode.accountCategory = p.accountCategory;
    	accountNode.guid = IdentifierFactory.getInstance().generateIdentifier();
    	return accountNode;
    	
    }
    

}
