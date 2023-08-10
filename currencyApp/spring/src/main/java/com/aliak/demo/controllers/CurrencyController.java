package com.aliak.demo.controllers;

import com.aliak.demo.advise.TrackExecutionTime;
import com.aliak.demo.entity.Currency;
import com.aliak.demo.payload.CurrencyCompare;
import com.aliak.demo.payload.CurrencyResponse;
import com.aliak.demo.payload.CurrencySearch;
import com.aliak.demo.services.impl.CurrencyServiceImpl;
import com.aliak.demo.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("currency")
@RequiredArgsConstructor
@Slf4j
public class CurrencyController {
    private final CurrencyServiceImpl currencyService;


    @GetMapping("/find/{id}")
    @PreAuthorize("hasRole('currency_user')")
    @TrackExecutionTime
    public ResponseEntity<Currency> getOneCurrency(@PathVariable Long id){
        log.debug("get one currency method has been reached");
        return ResponseEntity.ok(currencyService.getOneCurrency(id));
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('currency_user')")
    @TrackExecutionTime
    public CurrencyResponse getAllCurrencies(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        log.debug("get all currency method has been reached");
        return currencyService.getAllCurrencies(pageNo,pageSize,sortBy,sortDir);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('currency_user')")
    @TrackExecutionTime
    public <T> T searchAllCurrencies(@RequestBody CurrencySearch searchData){
        log.debug("search all currency method has been reached");
        return (T) currencyService.searchCurrencies(searchData);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('currency_admin')")
    @TrackExecutionTime
    public ResponseEntity<String> deleteOneCurrency(@PathVariable Long id){
        log.debug("delete one currency method has been reached");
        currencyService.deleteOneCurrency(id);
        return new ResponseEntity<>("Currency entity deleted successfully.", HttpStatus.OK);
    }

    @GetMapping("/findAllSafeDeleted")
    @PreAuthorize("hasRole('currency_admin')")
    @TrackExecutionTime
    public CurrencyResponse findAllDeletedCurrency(
            @RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted,
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ) {
        log.debug("find all without safe deleted currency method has been reached");
        return currencyService.findAllUndeletedCurrency(isDeleted,pageNo,pageSize,sortBy,sortDir);
    }

    @PostMapping("/compare")
    @PreAuthorize("hasRole('currency_user')")
    @TrackExecutionTime
    public <T> T compareCurrencies(@RequestBody CurrencyCompare body){
        log.debug("compare currency method has been reached");
        return (T) currencyService.compareCurrencies(body);
    }


}
