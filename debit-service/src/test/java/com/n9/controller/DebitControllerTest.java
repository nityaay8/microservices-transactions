package com.n9.controller;

import com.n9.dto.DebitDTO;
import com.n9.entity.Account;
import com.n9.exception.DebitAccountException;
import com.n9.service.DebitDataService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DebitController.class)
public class DebitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DebitDataService debitDataService;

    private String baseUrl;

    @BeforeEach
    void init() {
        baseUrl = "/debit";
    }

    private Account getAccount() {
        Account account = new Account();
        account.setAmount(10000.0f);
        account.setAccountId(1010l);
        return account;
    }

    public DebitDTO getMockDebitDTO() {
        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setActId(100l);
        debitDTO.setAmount(10f);

        return debitDTO;
    }

    public JSONObject getDebitJSONObject(DebitDTO debitDTO) throws Exception {
        JSONObject debitInfoObject = new JSONObject();
        debitInfoObject.put("actId", debitDTO.getActId());
        debitInfoObject.put("amount", debitDTO.getAmount());
        return debitInfoObject;
    }

    @Test
    public void testPerformDebit() throws Exception {
        DebitDTO debitDTO = getMockDebitDTO();

        JSONObject debitInfoObject = getDebitJSONObject(debitDTO);


        //mock the method call
        Mockito.when(debitDataService.performDebit(Mockito.any(DebitDTO.class))).thenReturn(debitDTO);


        this.mockMvc.perform(post(baseUrl).content(debitInfoObject.toString()).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("100")));

        //for verification of method call
        Mockito.verify(debitDataService).performDebit(Mockito.any(DebitDTO.class));
    }

    @Test
    public void testPerformDebitWhenAccountIdIsNull() throws Exception {
        DebitDTO debitDTO = getMockDebitDTO();
        debitDTO.setActId(null);

        JSONObject debitInfoObject = getDebitJSONObject(debitDTO);

        this.mockMvc.perform(post(baseUrl).content(debitInfoObject.toString()).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid account details")));
    }

    @Test
    public void testPerformDebitWhenAccountIdNotFound() throws Exception {
        DebitDTO debitDTO = getMockDebitDTO();

        JSONObject debitInfoObject = getDebitJSONObject(debitDTO);

        //mock the method call
        Mockito.when(debitDataService.performDebit(Mockito.any(DebitDTO.class))).thenThrow(new DebitAccountException("Account is not found for account id " + debitDTO.getActId()));

        this.mockMvc.perform(post(baseUrl).content(debitInfoObject.toString()).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Account is not found for account id " + debitDTO.getActId())));
    }

    @Test
    public void testPerformDebitWhenAccountHasInSufficientBalance() throws Exception {
        DebitDTO debitDTO = getMockDebitDTO();

        JSONObject debitInfoObject = getDebitJSONObject(debitDTO);

        //mock the method call
        Mockito.when(debitDataService.performDebit(Mockito.any(DebitDTO.class))).thenThrow(new DebitAccountException("Account does not have sufficient balance"));

        this.mockMvc.perform(post(baseUrl).content(debitInfoObject.toString()).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Account does not have sufficient balance")));
    }


}
