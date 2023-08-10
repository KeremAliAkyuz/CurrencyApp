package com.aliak.demo.services;

import com.aliak.demo.entity.Currency;
import com.aliak.demo.payload.AddFavorites;
import com.aliak.demo.payload.CurrencyCompare;
import com.aliak.demo.payload.CurrencyResponse;
import com.aliak.demo.payload.CurrencySearch;

import java.util.List;

public interface CurrencyService {
    Currency getOneCurrency(Long id);

    CurrencyResponse getAllCurrencies(int pageNo, int pageSize, String sortBy, String sortDir);

    void deleteOneCurrency(Long id);

     <T> T searchCurrencies(CurrencySearch searchData);

     CurrencyResponse findAllUndeletedCurrency(boolean isDeleted, int pageNo, int pageSize, String sortBy, String sortDir);

     <T> T compareCurrencies(CurrencyCompare body);


}
