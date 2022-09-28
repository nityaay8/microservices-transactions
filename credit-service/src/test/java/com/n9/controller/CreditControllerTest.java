package com.n9.controller;

import com.n9.dto.CreditDTO;
import com.n9.entity.Account;
import com.n9.exception.CreditAccountException;
import com.n9.service.CreditDataService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditController.class)
public class CreditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditDataService creditDataService;

    private String baseUrl;

    @BeforeEach
    void init() {
        baseUrl = "/credit";
    }

    private Account getAccount() {
        Account account = new Account();
        account.setAmount(10000.0f);
        account.setAccountId(1010l);
        return account;
    }

    public CreditDTO getMockCreditDTO() {
        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setActId(100l);
        creditDTO.setAmount(10f);

        return creditDTO;
    }

    public JSONObject getDebitJSONObject(CreditDTO creditDTO) throws Exception {
        JSONObject creditInfoObject = new JSONObject();
        creditInfoObject.put("actId", creditDTO.getActId());
        creditInfoObject.put("amount", creditDTO.getAmount());
        return creditInfoObject;
    }

    @Test
    public void testPerformCredit() throws Exception {
        CreditDTO creditDTO = getMockCreditDTO();

        JSONObject creditInfoObject = getDebitJSONObject(creditDTO);


        //mock the method call
        Mockito.when(creditDataService.performCredit(Mockito.any(CreditDTO.class))).thenReturn(creditDTO);


        this.mockMvc.perform(post(baseUrl).content(creditInfoObject.toString()).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("100")));

        //for verification of method call
        Mockito.verify(creditDataService).performCredit(Mockito.any(CreditDTO.class));
    }

    @Test
    public void testPerformCreditWhenAccountIdIsNull() throws Exception {
        CreditDTO creditDTO = getMockCreditDTO();
        creditDTO.setActId(null);

        JSONObject creditInfoObject = getDebitJSONObject(creditDTO);

        this.mockMvc.perform(post(baseUrl).content(creditInfoObject.toString()).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid account details")));
    }

    @Test
    public void testPerformCreditWhenAccountIdNotFound() throws Exception {
        CreditDTO creditDTO = getMockCreditDTO();

        JSONObject creditInfoObject = getDebitJSONObject(creditDTO);

        //mock the method call
        Mockito.when(creditDataService.performCredit(Mockito.any(CreditDTO.class))).thenThrow(new CreditAccountException("Account is not found for account id " + creditDTO.getActId()));

        this.mockMvc.perform(post(baseUrl).content(creditInfoObject.toString()).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Account is not found for account id " + creditDTO.getActId())));
    }


}
