package com.n9.service;

import com.n9.dto.DebitDTO;
import com.n9.entity.Account;
import com.n9.exception.DebitAccountException;
import com.n9.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class DebitDataService {

    @Autowired
    private AccountRepository accountRepository;


    public DebitDTO performDebit(DebitDTO debitDTO) {

        Optional<Account> accountOpt = accountRepository.findById(debitDTO.getActId());
        if (!accountOpt.isPresent()) {
            throw new DebitAccountException("Account is not found for account id " + debitDTO.getActId());
        }

        Account account = accountOpt.get();

        float amount = account.getAmount() - debitDTO.getAmount();
        if (amount < 0) {
            throw new DebitAccountException("Account does not have sufficient balance");
        }

        account.setAmount(amount);
        accountRepository.save(account);

        return debitDTO;

    }

}
