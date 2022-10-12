package com.n9.service;

import com.n9.dto.AccountDTO;
import com.n9.dto.CreditDTO;
import com.n9.entity.Account;
import com.n9.exception.CreditAccountException;
import com.n9.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CreditDataService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AccountDTO saveAccount(AccountDTO accountDTO) {
        Account account = modelMapper.map(accountDTO, Account.class);

        account.setUpdatedBy("system");
        account.setCreatedBy("system");

        account.setUpdatedDate(new Date());
        account.setCreatedDate(new Date());
        Account savedAccount = accountRepository.save(account);

        accountDTO = modelMapper.map(savedAccount, AccountDTO.class);

        return accountDTO;
    }


    public CreditDTO performCredit(CreditDTO debitDTO) {
        log.debug("performCredit start");
        log.info("credit operation started");
        Optional<Account> accountOpt = accountRepository.findById(debitDTO.getActId());
        if (!accountOpt.isPresent()) {
            throw new CreditAccountException("Account is not found for account id " + debitDTO.getActId());
        }


        Account account = accountOpt.get();
        log.info("got credit account details {} ", account);
        account.setUpdatedDate(new Date());
        account.setUpdatedBy("user");

        float amount = account.getAmount() + debitDTO.getAmount();


        account.setAmount(amount);
        accountRepository.save(account);
        log.info("credit operation success");
        log.debug("performCredit end");
        return debitDTO;

    }

}
