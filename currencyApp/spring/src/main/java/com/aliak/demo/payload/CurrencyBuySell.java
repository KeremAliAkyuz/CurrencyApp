package com.aliak.demo.payload;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyBuySell {
    @NotBlank(message = "currencyName field can not be empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Only string values are allowed")
    @Size(max = 4,message = "List size cannot exceed 4 currency type")
    private String currencyName;


    @NotNull(message = "currencyBuyMin field can not be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "Value must be greater than 0.0")
    private Double currencyBuyMin;


    @DecimalMin(value = "0.0", inclusive = false, message = "Value must be greater than 0.0")
    @NotNull(message = "currencyBuyMax field can not be empty")
    private Double currencyBuyMax;


    @DecimalMin(value = "0.0", inclusive = false, message = "Value must be greater than 0.0")
    @NotNull(message = "currencySellMin field can not be empty")
    private Double currencySellMin;


    @DecimalMin(value = "0.0", inclusive = false, message = "Value must be greater than 0.0")
    @NotNull(message = "currencySellMax field can not be empty")
    private Double currencySellMax;

}
