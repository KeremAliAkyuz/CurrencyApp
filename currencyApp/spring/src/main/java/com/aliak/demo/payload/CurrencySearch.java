package com.aliak.demo.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencySearch {
    @NotEmpty(message = "must have at least one bank")
    @Size(max = 4, message = "List size cannot exceed 4 banks")
    private List<String> banks;

    @NotEmpty(message = "must have at least one currency object")
    @Valid
    @Size(max = 4, message = "List size cannot exceed 4 objects")
    private List<CurrencyBuySell> currencyBuySell;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;
}

//    @NotEmpty(message = "must have at least one currency")
//    private List<String> currencies;
//    @Size(min = 2,max = 2,message = "enter only minimum buy price and maximum buy price")
////    @AssertTrue(message = "minimum buy price must be lower than maximum buy price")
////    public boolean isConditionTrue(){
////        return buy.get(0) < buy.get(1);
////    }
//    private List<Double> buy;
//
//    private List<Double> sell;