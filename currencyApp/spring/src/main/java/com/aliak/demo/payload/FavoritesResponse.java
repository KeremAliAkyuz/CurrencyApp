package com.aliak.demo.payload;

import com.aliak.demo.entity.Currency;
import com.aliak.demo.entity.Favorites;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesResponse {
    private List<Favorites> content;
    private int pageNo;
    private int pageSize;
    private long totalElement;
    private int totalPages;
    private boolean last;
}