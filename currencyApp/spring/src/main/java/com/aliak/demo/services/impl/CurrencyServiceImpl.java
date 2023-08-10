package com.aliak.demo.services.impl;

import com.aliak.demo.advise.TrackExecutionTime;
import com.aliak.demo.entity.Currency;
import com.aliak.demo.exception.ResourceNotFoundException;
import com.aliak.demo.payload.CurrencyBuySell;
import com.aliak.demo.payload.CurrencyCompare;
import com.aliak.demo.payload.CurrencyResponse;
import com.aliak.demo.payload.CurrencySearch;
import com.aliak.demo.repositories.CurrencyRepository;
import com.aliak.demo.services.CurrencyService;
import com.aliak.demo.validators.ObjectsValidator;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final ObjectsValidator<CurrencySearch> currencySearchObjectsValidator;
    private final ObjectsValidator<CurrencyCompare> currencyCompareObjectsValidator;
    private final EntityManager entityManager;
    @Override
    public Currency getOneCurrency(Long id) {
        return currencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency","id",id));
    }
    @Override
    public CurrencyResponse getAllCurrencies(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Currency> currencies = currencyRepository.findAll(pageable);

        List<Currency> content = currencies.getContent();

        return new CurrencyResponse(content,
                currencies.getNumber(),
                currencies.getSize(),
                currencies.getTotalElements(),
                currencies.getTotalPages(),
                currencies.isLast()
        );
    }

    @Override
    public void deleteOneCurrency(Long id) {
        currencyRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        currencyRepository.deleteById(id);
    }

    @Override
    public CurrencyResponse findAllUndeletedCurrency(boolean isDeleted,int pageNo, int pageSize, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedCurrencyFilter");
        filter.setParameter("isDeleted", isDeleted);
        Page<Currency> currencies =  currencyRepository.findAll(pageable);
        session.disableFilter("deletedCurrencyFilter");

        List<Currency> content = currencies.getContent();

        return new CurrencyResponse(content,
                currencies.getNumber(),
                currencies.getSize(),
                currencies.getTotalElements(),
                currencies.getTotalPages(),
                currencies.isLast()
        );

    }

//    @Override
//    public <T> T compareCurrencies(CurrencyCompare body) {
//
//        var violationsSearchData= currencyCompareObjectsValidator.validate(body);
//
//        if (!violationsSearchData.isEmpty()) {
//            return (T) (String.join("\n", violationsSearchData));
//        }
//        NumberFormat percentFormat = NumberFormat.getPercentInstance();
//        percentFormat.setMinimumFractionDigits(2);
//
//        Double mainBuyRatio = 0.0;
//        Double mainSellRatio = 0.0;
//        List<String> responseString = new ArrayList<>();
//
//        List<String> allBanks = new ArrayList<>();
//        allBanks.add(body.getBank());
//        allBanks.addAll(body.getComparedBanks());
//
//        for (int i=0;i<body.getComparedBanks().size()+1;i++) {
//            Double totalBuy = 0.0;
//            Double totalSell = 0.0;
//            Double buyRatio;
//            Double sellRatio;
//
//            List<Currency> banks = currencyRepository.findAllByBankAndCurrencyAndDateBetween(
//                    allBanks.get(i),
//                    body.getCurrency(),
//                    body.getStartDate(),
//                    body.getEndDate()
//            );
//
//            for(Currency bank: banks) {
//                totalBuy = totalBuy + bank.getBuy();
//                totalSell = totalSell + bank.getSell();
//            }
//
//            buyRatio = totalBuy/banks.size();
//            sellRatio = totalSell/banks.size();
//
//            if (i==0){
//                mainBuyRatio = buyRatio;
//                mainSellRatio = sellRatio;
//            }
//
//            else{
//
//                String stringBuyRatio = String.valueOf(percentFormat.format((buyRatio-mainBuyRatio)/mainBuyRatio));
//                String stringSellRatio = String.valueOf(percentFormat.format((sellRatio-mainSellRatio)/mainSellRatio));
//                String stringBankName = body.getComparedBanks().get(i-1);
//
//                String responseBuy = "Buy " + body.getCurrency() + " ratio between " + stringBankName + " and " + body.getBank() + " = " + stringBuyRatio;
//                String responseSell = "Sell " + body.getCurrency() + " ratio between " + stringBankName + " and " + body.getBank() + " = " + stringSellRatio;
//
//                responseString.add(responseBuy);
//                responseString.add(responseSell);
//                responseBuy = null;
//                responseSell = null;
//            }
//        }
//        return (T) responseString;
//    }

    @Override
    public <T> T compareCurrencies(CurrencyCompare body) {

        var violationsSearchData = currencyCompareObjectsValidator.validate(body);

        if (!violationsSearchData.isEmpty()) {
            return (T) (String.join("\n", violationsSearchData));
        }

        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMinimumFractionDigits(2);

        Double mainBuyRatio = 0.0;
        Double mainSellRatio = 0.0;
        List<String> responseString = new ArrayList<>();

        List<String> allBanks = new ArrayList<>();
        allBanks.add(body.getBank());
        allBanks.addAll(body.getComparedBanks());

        for (int i = 0; i < body.getComparedBanks().size() + 1; i++) {
            Double totalBuy = 0.0;
            Double totalSell = 0.0;
            Double buyRatio;
            Double sellRatio;

            List<Currency> banks = currencyRepository.findAllByBankAndCurrencyAndDateBetween(
                    allBanks.get(i),
                    body.getCurrency(),
                    body.getStartDate(),
                    body.getEndDate()
            );

            if (banks != null && !banks.isEmpty()) { // Check if banks is not null and not empty
                for (Currency bank : banks) {
                    totalBuy = totalBuy + bank.getBuy();
                    totalSell = totalSell + bank.getSell();
                }

                buyRatio = totalBuy / banks.size();
                sellRatio = totalSell / banks.size();

                if (i == 0) {
                    mainBuyRatio = buyRatio;
                    mainSellRatio = sellRatio;
                } else {
                    String stringBuyRatio = String.valueOf(percentFormat.format((buyRatio - mainBuyRatio) / mainBuyRatio));
                    String stringSellRatio = String.valueOf(percentFormat.format((sellRatio - mainSellRatio) / mainSellRatio));
                    String stringBankName = body.getComparedBanks().get(i - 1);

                    String responseBuy = "Buy " + body.getCurrency() + " ratio between " + stringBankName + " and " + body.getBank() + " = " + stringBuyRatio;
                    String responseSell = "Sell " + body.getCurrency() + " ratio between " + stringBankName + " and " + body.getBank() + " = " + stringSellRatio;

                    responseString.add(responseBuy);
                    responseString.add(responseSell);
                    responseBuy = null;
                    responseSell = null;
                }
            }
        }
        return (T) responseString;
    }

    @Override
    public <T> T searchCurrencies(CurrencySearch searchData) {

        var violationsSearchData= currencySearchObjectsValidator.validate(searchData);

        if (!violationsSearchData.isEmpty()) {
            return (T) (String.join("\n", violationsSearchData));
        }

        List<Currency> content = new ArrayList<>();

        for (int i = 0; i < searchData.getCurrencyBuySell().size(); i++) {

            CurrencyBuySell currencyBuySell = searchData.getCurrencyBuySell().get(i);
            List<Currency> result = currencyRepository.searchQueries(
                    searchData.getBanks(),
                    currencyBuySell.getCurrencyName(),
                    currencyBuySell.getCurrencyBuyMin(),
                    currencyBuySell.getCurrencyBuyMax(),
                    currencyBuySell.getCurrencySellMin(),
                    currencyBuySell.getCurrencySellMax(),
                    searchData.getStartDate(),
                    searchData.getEndDate()
            );

            content.addAll(result);

        }

        PagedListHolder page = new PagedListHolder(content);
        page.setPageSize(50);
        page.setPage(0);

        return (T) new CurrencyResponse(page.getPageList(),
                page.getPage(),
                page.getPageSize(),
                content.size(),
                page.getPageCount(),
                page.isLastPage()
        );
    }
}
