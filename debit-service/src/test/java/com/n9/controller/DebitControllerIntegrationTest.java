package com.n9.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("int")
@Disabled("until mysql installed on docker")
public class DebitControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String baseUrl;

    @BeforeEach
    void init() {
        baseUrl = "http://localhost:" + port + "/debit";
    }

    JSONObject getDebitJsonInfo() throws Exception {
        JSONObject debitInfoObject = new JSONObject();
        debitInfoObject.put("actId", "3");
        debitInfoObject.put("amount", "10");

        return debitInfoObject;
    }

    @Test
    public void testPerformDebit() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject debitInfoObject = getDebitJsonInfo();

        HttpEntity<String> request =
                new HttpEntity<String>(debitInfoObject.toString(), headers);

        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity(baseUrl, request, String.class);

        Assertions.assertNotNull(responseEntity);

        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        Assertions.assertNotNull(responseEntity.getBody());

        JSONObject respJsonObject = new JSONObject(responseEntity.getBody());

        Assertions.assertEquals(Integer.valueOf(debitInfoObject.get("actId").toString()), respJsonObject.get("actId"));


    }

    @Test
    public void testPerformDebitWhenAccountIdIsNull() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject debitInfoObject = new JSONObject();
        debitInfoObject.put("amount", "10");

        HttpEntity<String> request =
                new HttpEntity<String>(debitInfoObject.toString(), headers);

        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity(baseUrl, request, String.class);

        Assertions.assertNotNull(responseEntity);

        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());

        Assertions.assertNotNull(responseEntity.getBody());

        JSONObject respJsonObject = new JSONObject(responseEntity.getBody());

        Assertions.assertEquals("invalid account details", respJsonObject.get("statusMsg"));
    }

    @Test
    public void testPerformDebitWhenAccountIdNotFound() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject debitInfoObject = new JSONObject();
        debitInfoObject.put("actId", "1001");
        debitInfoObject.put("amount", "10");

        HttpEntity<String> request =
                new HttpEntity<String>(debitInfoObject.toString(), headers);

        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity(baseUrl, request, String.class);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());

        JSONObject respJsonObject = new JSONObject(responseEntity.getBody());

        Assertions.assertEquals("Account is not found for account id 1001", respJsonObject.get("statusMsg"));


    }

    @Test
    public void testPerformDebitWhenAccountHasInSufficientBalance() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject debitInfoObject = getDebitJsonInfo();
        debitInfoObject.put("amount", "1000000000");

        HttpEntity<String> request =
                new HttpEntity<String>(debitInfoObject.toString(), headers);

        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity(baseUrl, request, String.class);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());

        JSONObject respJsonObject = new JSONObject(responseEntity.getBody());

        Assertions.assertEquals("Account does not have sufficient balance", respJsonObject.get("statusMsg"));
    }


}
