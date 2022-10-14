package com.n9.service;

import com.n9.dto.AccountDTO;
import com.n9.entity.Account;
import com.n9.exception.AccountException;
import com.n9.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AccountDataService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;


    public AccountDTO save(AccountDTO accountDTO) {

        Account account = modelMapper.map(accountDTO, Account.class);

        account.setCreatedDate(new Date());
        account.setUpdatedDate(new Date());

        account.setCreatedBy("user");
        account.setUpdatedBy("user");

        account = accountRepository.save(account);

        AccountDTO savedAccountDTO = modelMapper.map(account, AccountDTO.class);

        return savedAccountDTO;
    }

    public AccountDTO update(AccountDTO accountDTO) {

        Optional<Account> accountOpt = accountRepository.findById(accountDTO.getAccountId());
        if (!accountOpt.isPresent()) {
            throw new AccountException("Account is not found for account id " +
                    accountDTO.getAccountId());
        }

        Account account = accountOpt.get();

        modelMapper.map(accountDTO, account);


        account.setUpdatedDate(new Date());
        account.setUpdatedBy("user");

        accountRepository.save(account);

        return accountDTO;

    }

    public AccountDTO getAccount(Long accountId) {


        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (!accountOpt.isPresent()) {
            throw new AccountException("Account is not found for account id " +
                    accountId);
        }

        AccountDTO accountDTO = modelMapper.map(accountOpt.get(), AccountDTO.class);

        return accountDTO;
    }

    public void removeAccount(Long accountId) {

        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (!accountOpt.isPresent()) {
            throw new AccountException("Account is not found for account id " +
                    accountId);
        }

        accountRepository.deleteById(accountId);

    }

    public List<AccountDTO> getAllAccount() {

        log.debug("get all accounts start");
        List<Account> accountList = accountRepository.findAll();
        log.info("get all accounts from db, size = {} " + accountList.size());

        List<AccountDTO> accountDTOList = accountList.stream().
                map(act -> modelMapper.map(act, AccountDTO.class)).collect(Collectors.toList());

        log.debug("get all accounts end");
        return accountDTOList;
    }

}
