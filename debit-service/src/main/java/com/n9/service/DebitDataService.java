package com.n9.service;

import com.n9.dto.DebitDTO;
import com.n9.entity.Account;
import com.n9.exception.DebitAccountException;
import com.n9.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class DebitDataService {

    @Autowired
    private AccountRepository accountRepository;


    public DebitDTO performDebit(DebitDTO debitDTO) {

        log.debug("performDebit start");
        log.info("debit operation started");
        Optional<Account> accountOpt = accountRepository.findById(debitDTO.getActId());
        if (!accountOpt.isPresent()) {
            log.error("unable to get the account");
            log.warn("debit process failed");
            throw new DebitAccountException("Account is not found for account id " + debitDTO.getActId());
        }

        Account account = accountOpt.get();

        float amount = account.getAmount() - debitDTO.getAmount();
        if (amount < 0) {
            log.warn("debit failed due to insufficient balance");
            log.warn("debit process failed");
            throw new DebitAccountException("Account does not have sufficient balance");
        }

        account.setUpdatedDate(new Date());
        account.setUpdatedBy("user");
        account.setAmount(amount);
        accountRepository.save(account);
        log.info("debit operation success");
        log.info("debit operation completed");
        log.debug("performDebit end");
        return debitDTO;

    }

}
