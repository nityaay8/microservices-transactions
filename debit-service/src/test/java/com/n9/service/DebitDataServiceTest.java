package com.n9.service;

import com.n9.dto.DebitDTO;
import com.n9.entity.Account;
import com.n9.exception.DebitAccountException;
import com.n9.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;


@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {DebitDataService.class}
)
public class DebitDataServiceTest {


    @Autowired
    private DebitDataService debitDataService;


    @MockBean
    private AccountRepository accountRepository;


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
    public void testPerformDebit() throws Exception {

        Account mockAccount = getAccount();
        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockAccount));

        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setActId(1010l);
        debitDTO.setAmount(10f);

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(mockAccount);

        DebitDTO updatedDebitDTO = debitDataService.performDebit(debitDTO);
        Assertions.assertNotNull(updatedDebitDTO);

        //verify the method call
        Mockito.verify(accountRepository).findById(Mockito.anyLong());
        Mockito.verify(accountRepository).save(Mockito.any(Account.class));

    }

    @Test
    public void testPerformDebitWhenAccountIdNotFound() throws Exception {


        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final DebitDTO debitDTO = new DebitDTO();
        debitDTO.setActId(1010l);
        debitDTO.setAmount(10f);

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(new Account());

        Exception ex = Assertions.assertThrows(DebitAccountException.class, () -> {
            DebitDTO updatedDebitDTO = debitDataService.performDebit(debitDTO);
        });

        Assertions.assertEquals("Account is not found for account id " + debitDTO.getActId(), ex.getMessage());


        //verify the method calls
        Mockito.verify(accountRepository).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any(Account.class));

    }

    @Test
    public void testPerformDebitWhenAccountHasInSufficientBalance() throws Exception {

        Account mockAccount = getAccount();
        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockAccount));

        final DebitDTO debitDTO = new DebitDTO();
        debitDTO.setActId(1010l);
        debitDTO.setAmount(100000f);

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(mockAccount);

        Exception ex = Assertions.assertThrows(RuntimeException.class, () -> {
            DebitDTO updatedDebitDTO = debitDataService.performDebit(debitDTO);
        });

        Assertions.assertEquals("Account does not have sufficient balance", ex.getMessage());


        //verify the method calls
        Mockito.verify(accountRepository).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any(Account.class));

    }

    @Test
    public void testPerformDebitWhenSystemErrorOccurred() throws Exception {

        Account mockAccount = getAccount();
        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockAccount));

        final DebitDTO debitDTO = new DebitDTO();
        debitDTO.setActId(1010l);
        debitDTO.setAmount(100f);

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenThrow(new RuntimeException("db connection lost"));

        Exception ex = Assertions.assertThrows(RuntimeException.class, () -> {
            DebitDTO updatedDebitDTO = debitDataService.performDebit(debitDTO);
        });

        Assertions.assertEquals("db connection lost", ex.getMessage());


        //verify the method calls
        Mockito.verify(accountRepository).findById(Mockito.anyLong());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));

    }

}
