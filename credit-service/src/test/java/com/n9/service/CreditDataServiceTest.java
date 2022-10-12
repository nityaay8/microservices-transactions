package com.n9.service;

import com.n9.dto.CreditDTO;
import com.n9.entity.Account;
import com.n9.exception.CreditAccountException;
import com.n9.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;


@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {CreditDataService.class}
)
public class CreditDataServiceTest {


    @Autowired
    private CreditDataService creditDataService;


    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private ModelMapper modelMapper;


    private Account getAccount() {
        Account account = new Account();
        account.setAmount(10000.0f);
        account.setAccountId(1010l);
        return account;
    }


    @BeforeEach
    public void init() throws Exception {


    }

    @Test
    public void testPerformCredit() throws Exception {

        Account mockAccount = getAccount();
        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockAccount));

        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setActId(1010l);
        creditDTO.setAmount(10f);

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(mockAccount);

        CreditDTO updatedCreditDTO = creditDataService.performCredit(creditDTO);
        Assertions.assertNotNull(updatedCreditDTO);

        //verify the method call
        Mockito.verify(accountRepository).findById(Mockito.anyLong());
        Mockito.verify(accountRepository).save(Mockito.any(Account.class));

    }

    @Test
    public void testPerformCreditWhenAccountIdNotFound() throws Exception {


        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final CreditDTO creditDTO = new CreditDTO();
        creditDTO.setActId(1010l);
        creditDTO.setAmount(10f);

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(new Account());

        Exception ex = Assertions.assertThrows(CreditAccountException.class, () -> {
            CreditDTO updatedCreditDTO = creditDataService.performCredit(creditDTO);
        });

        Assertions.assertEquals("Account is not found for account id " + creditDTO.getActId(), ex.getMessage());


        //verify the method calls
        Mockito.verify(accountRepository).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any(Account.class));

    }


    @Test
    public void testPerformCreditWhenSystemErrorOccurred() throws Exception {

        Account mockAccount = getAccount();
        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockAccount));

        final CreditDTO creditDTO = new CreditDTO();
        creditDTO.setActId(1010l);
        creditDTO.setAmount(100f);

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenThrow(new RuntimeException("db connection lost"));

        Exception ex = Assertions.assertThrows(RuntimeException.class, () -> {
            CreditDTO updatedCreditDTO = creditDataService.performCredit(creditDTO);
        });

        Assertions.assertEquals("db connection lost", ex.getMessage());


        //verify the method calls
        Mockito.verify(accountRepository).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));

    }

}
