package net.flyingfishflash.ledger.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.flyingfishflash.ledger.common.IdentifierFactory;
import net.flyingfishflash.ledger.domain.AccountCategory;
import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountRepository;
import net.flyingfishflash.ledger.domain.AccountType;
import net.flyingfishflash.ledger.domain.AccountTypeCategory;
import net.flyingfishflash.ledger.web.AccountController;

@Service
@Transactional
public class AccountService {
	
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private static final AccountTypeCategory atc = new AccountTypeCategory();
    
    @Autowired
    private AccountRepository accountRepository;
	
    
    public AccountNode newAccountNode(AccountNode p) {
    
    	AccountNode accountNode = accountRepository.newAccountNode();

    	accountNode.setGuid(IdentifierFactory.getInstance().generateIdentifier());
   	
    	if (p.getAccountCategory().equals(AccountCategory.Root)) {
    		accountNode.setAccountCategory(AccountCategory.Asset);
    		accountNode.setAccountType(AccountType.Asset);
    	} else {
    		accountNode.setAccountCategory(p.getAccountCategory());
    		accountNode.setAccountType(p.getAccountType());
    	}
    	
    	return accountNode;
  	
    }
    

    public AccountNode findRoot() {
    	
    	return accountRepository.findRoot();
    	
    }

    
    public AccountNode findOneById(Long id) {
    	
    	return accountRepository.findOneById(id);

    }
    
    
    public Iterable<AccountNode> findWholeTree() {
    	
    	return accountRepository.findWholeTree();
    
    }
    
    
    public Optional<AccountNode> getPrevSibling(AccountNode account) {
    	
    	return accountRepository.getPrevSibling(account);
    	
    }
    
    
    public Optional<AccountNode> getNextSibling(AccountNode account) {
    	
    	return accountRepository.getNextSibling(account);
    	
    }
    
    
    public void insertAsFirstChildOf(AccountNode account, AccountNode parent) {
    	
    	accountRepository.insertAsFirstChildOf(account, parent);
    
    }
    

    public void insertAsLastChildOf(AccountNode account, AccountNode parent) {
    	
    	accountRepository.insertAsLastChildOf(account, parent);
    
    }
    

    public void insertAsNextSiblingOf(AccountNode account, AccountNode parent) {
    	
    	accountRepository.insertAsNextSiblingOf(account, parent);
    
    }

    
    public void insertAsPrevSiblingOf(AccountNode account, AccountNode parent) {
    	
    	accountRepository.insertAsPrevSiblingOf(account, parent);
    
    }

    
    public void removeSingle(AccountNode account) {
    	
    	accountRepository.removeSingle(account);
    
    }

    
    public void removeSubTree(AccountNode account) {
    	
    	accountRepository.removeSubTree(account);
    
    }
    
    
    public List<AccountCategory> getCategories() {

        return atc.getCategories();

    }

    
    public List<AccountCategory> getCategoriesByType(String type) {

        return atc.getCategoriesByType(type);

    }


    public List<AccountType> getTypesByCategory(String category) {

        return atc.getTypesByCategory(category);

    }

    
}
