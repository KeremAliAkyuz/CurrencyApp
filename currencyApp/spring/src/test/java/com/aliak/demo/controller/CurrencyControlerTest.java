package com.aliak.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.aliak.demo.controllers.CurrencyController;
import com.aliak.demo.entity.Currency;
import com.aliak.demo.payload.CurrencyCompare;
import com.aliak.demo.payload.CurrencyResponse;
import com.aliak.demo.payload.CurrencySearch;
import com.aliak.demo.services.impl.CurrencyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CurrencyControllerTest {

    private CurrencyServiceImpl currencyService;
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
        currencyService = mock(CurrencyServiceImpl.class);
        currencyController = new CurrencyController(currencyService);
    }

//    @Test
//    void testGetOneCurrency() {
//        Long currencyId = 1L;
//        Currency currency = new Currency(); // Create a sample Currency object
//
//        when(currencyService.getOneCurrency(currencyId)).thenReturn(currency);
//
//        ResponseEntity<Currency> responseEntity = currencyController.getOneCurrency(currencyId);
//
//        verify(currencyService).getOneCurrency(currencyId);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(currency, responseEntity.getBody());
//    }

    @Test
    void testGetAllCurrencies() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "currencyName";
        String sortDir = "asc";
        CurrencyResponse currencyResponse = new CurrencyResponse(); // Create a sample CurrencyResponse object

        when(currencyService.getAllCurrencies(pageNo, pageSize, sortBy, sortDir)).thenReturn(currencyResponse);

        CurrencyResponse result = currencyController.getAllCurrencies(pageNo, pageSize, sortBy, sortDir);

        verify(currencyService).getAllCurrencies(pageNo, pageSize, sortBy, sortDir);
        assertEquals(currencyResponse, result);
    }

    @Test
    void testSearchAllCurrencies() {
        CurrencySearch searchData = new CurrencySearch(); // Create a sample CurrencySearch object

        // Assuming the generic type T is a valid return type for currencyService.searchCurrencies
        when(currencyService.searchCurrencies(searchData)).thenReturn(searchData);

        CurrencySearch result = currencyController.searchAllCurrencies(searchData);

        verify(currencyService).searchCurrencies(searchData);
        assertEquals(searchData, result);
    }

    @Test
    void testDeleteOneCurrency() {
        Long currencyId = 1L;

        ResponseEntity<String> responseEntity = currencyController.deleteOneCurrency(currencyId);

        verify(currencyService).deleteOneCurrency(currencyId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Currency entity deleted successfully.", responseEntity.getBody());
    }

    @Test
    void testFindAllDeletedCurrency() {
        boolean isDeleted = true;
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "currencyName";
        String sortDir = "asc";
        CurrencyResponse currencyResponse = new CurrencyResponse(); // Create a sample CurrencyResponse object

        when(currencyService.findAllUndeletedCurrency(isDeleted, pageNo, pageSize, sortBy, sortDir)).thenReturn(currencyResponse);

        CurrencyResponse result = currencyController.findAllDeletedCurrency(isDeleted, pageNo, pageSize, sortBy, sortDir);

        verify(currencyService).findAllUndeletedCurrency(isDeleted, pageNo, pageSize, sortBy, sortDir);
        assertEquals(currencyResponse, result);
    }

    @Test
    void testCompareCurrencies() {
        CurrencyCompare currencyCompare = new CurrencyCompare(); // Create a sample CurrencyCompare object

        // Assuming the generic type T is a valid return type for currencyService.compareCurrencies
        when(currencyService.compareCurrencies(currencyCompare)).thenReturn(currencyCompare);

        CurrencyCompare result = currencyController.compareCurrencies(currencyCompare);

        verify(currencyService).compareCurrencies(currencyCompare);
        assertEquals(currencyCompare, result);
    }
}
