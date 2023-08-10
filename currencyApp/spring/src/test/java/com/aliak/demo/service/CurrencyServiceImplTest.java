package com.aliak.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.aliak.demo.entity.Currency;
import com.aliak.demo.payload.CurrencyCompare;
import com.aliak.demo.payload.CurrencyResponse;
import com.aliak.demo.payload.CurrencySearch;
import com.aliak.demo.repositories.CurrencyRepository;
import com.aliak.demo.services.impl.CurrencyServiceImpl;
import com.aliak.demo.validators.ObjectsValidator;
import jakarta.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;



import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;

class CurrencyServiceImplTest {

    private CurrencyRepository currencyRepository;
    private ObjectsValidator<CurrencySearch> currencySearchObjectsValidator;
    private ObjectsValidator<CurrencyCompare> currencyCompareObjectsValidator;
    private EntityManager entityManager;
    private CurrencyServiceImpl currencyService;

    @BeforeEach
    void setUp() {
        currencyRepository = mock(CurrencyRepository.class);
        currencySearchObjectsValidator = mock(ObjectsValidator.class);
        currencyCompareObjectsValidator = mock(ObjectsValidator.class);
        entityManager = mock(EntityManager.class);

        currencyService = new CurrencyServiceImpl(
                currencyRepository,
                currencySearchObjectsValidator,
                currencyCompareObjectsValidator,
                entityManager
        );
    }

    @Test
    void testGetOneCurrency() {
        Long currencyId = 1L;
        Currency currency = new Currency(); // Create a sample Currency object

        when(currencyRepository.findById(currencyId)).thenReturn(java.util.Optional.of(currency));

        Currency result = currencyService.getOneCurrency(currencyId);

        verify(currencyRepository).findById(currencyId);
        assertEquals(currency, result);
    }

    @Test
    void testGetAllCurrencies() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "currencyName";
        String sortDir = "asc";
        Currency currency1 = new Currency();
        Currency currency2 = new Currency();
        List<Currency> currencyList = new ArrayList<>();
        currencyList.add(currency1);
        currencyList.add(currency2);

        Page<Currency> currencyPage = mock(Page.class);
        when(currencyPage.getContent()).thenReturn(currencyList);
        when(currencyPage.getNumber()).thenReturn(pageNo);
        when(currencyPage.getSize()).thenReturn(pageSize);
        when(currencyPage.getTotalElements()).thenReturn((long) currencyList.size());
        when(currencyPage.getTotalPages()).thenReturn(1);
        when(currencyPage.isLast()).thenReturn(true);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        when(currencyRepository.findAll(pageable)).thenReturn(currencyPage);

        CurrencyResponse result = currencyService.getAllCurrencies(pageNo, pageSize, sortBy, sortDir);

        verify(currencyRepository).findAll(pageable);
        assertEquals(currencyList, result.getContent());
        assertEquals(pageNo, result.getPageNo());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(currencyList.size(), result.getTotalElement());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());
    }

    @Test
    void testDeleteOneCurrency() {
        Long currencyId = 1L;
        Currency currency = new Currency();

        when(currencyRepository.findById(currencyId)).thenReturn(java.util.Optional.of(currency));

        currencyService.deleteOneCurrency(currencyId);

        verify(currencyRepository).findById(currencyId);
        verify(currencyRepository).deleteById(currencyId);
    }

    @Test
    void testFindAllUndeletedCurrency() {
        boolean isDeleted = false;
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "currencyName";
        String sortDir = "asc";
        Currency currency1 = new Currency();
        Currency currency2 = new Currency();
        List<Currency> currencyList = new ArrayList<>();
        currencyList.add(currency1);
        currencyList.add(currency2);

        Page<Currency> currencyPage = mock(Page.class);
        when(currencyPage.getContent()).thenReturn(currencyList);
        when(currencyPage.getNumber()).thenReturn(pageNo);
        when(currencyPage.getSize()).thenReturn(pageSize);
        when(currencyPage.getTotalElements()).thenReturn((long) currencyList.size());
        when(currencyPage.getTotalPages()).thenReturn(1);
        when(currencyPage.isLast()).thenReturn(true);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        when(currencyRepository.findAll(pageable)).thenReturn(currencyPage);

        Session session = mock(Session.class);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        Filter filter = mock(Filter.class);
        when(session.enableFilter("deletedCurrencyFilter")).thenReturn(filter);
        when(filter.setParameter("isDeleted", isDeleted)).thenReturn(filter);

        CurrencyResponse result = currencyService.findAllUndeletedCurrency(isDeleted, pageNo, pageSize, sortBy, sortDir);

        verify(currencyRepository).findAll(pageable);
        verify(session).enableFilter("deletedCurrencyFilter");
        verify(filter).setParameter("isDeleted", isDeleted);
        verify(session).disableFilter("deletedCurrencyFilter");

        assertEquals(currencyList, result.getContent());
        assertEquals(pageNo, result.getPageNo());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(currencyList.size(), result.getTotalElement());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());
    }


    @Test
    void testSearchCurrencies() {
        CurrencySearch searchData = new CurrencySearch(); // Create a sample CurrencySearch object

        // Mocking the validate method with a proper generic type (Set)
        when(currencySearchObjectsValidator.validate(searchData)).thenReturn(Collections.emptySet());

        // In case the getCurrencyBuySell() returns null, we can set it to an empty list
        if (searchData.getCurrencyBuySell() == null) {
            searchData.setCurrencyBuySell(new ArrayList<>());
        }

        List<Currency> content = new ArrayList<>();
        // Add relevant currencies to the 'content' list for testing

        when(currencyRepository.searchQueries(
                anyList(),
                anyString(),
                anyDouble(),
                anyDouble(),
                anyDouble(),
                anyDouble(),
                any(),
                any())).thenReturn(content);

        CurrencyResponse result = currencyService.searchCurrencies(searchData);

        // Verifying that the validate method was called
        verify(currencySearchObjectsValidator).validate(searchData);
        verify(currencyRepository, times(searchData.getCurrencyBuySell().size())).searchQueries(
                anyList(),
                anyString(),
                anyDouble(),
                anyDouble(),
                anyDouble(),
                anyDouble(),
                any(),
                any()
        );

        // Perform your assertions based on the logic in the method.
        // Make sure to test various scenarios for the result.

        // For example:
        assertNotNull(result);
        assertEquals(content, result.getContent());
        // Add more assertions based on your specific logic.
    }

}