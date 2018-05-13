package net.flyingfishflash.ledger.service.rest;

import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountNodeDto;
import net.flyingfishflash.ledger.domain.AccountRepository;
import net.flyingfishflash.ledger.domain.AccountTypeCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("AccountServiceRest")
@Transactional
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private static final AccountTypeCategory atc = new AccountTypeCategory();

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<AccountNodeDto> getSingleAccountNodeResponse(Long id) {

        AccountNode accountNode = accountRepository.findOneById(id);
        AccountNodeDto getAccountNodeDto = new AccountNodeDto(accountNode);
        logger.debug(getAccountNodeDto.longname);



        //AccountNode getAccountNode = accountRepository.findOneById(id);
        return new ResponseEntity<AccountNodeDto>(getAccountNodeDto, HttpStatus.OK);
    }
}
