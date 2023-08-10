package com.aliak.demo.services;

import com.aliak.demo.entity.Currency;
import com.aliak.demo.entity.Favorites;
import com.aliak.demo.payload.AddFavorites;
import com.aliak.demo.payload.CurrencyResponse;
import com.aliak.demo.payload.FavoritesResponse;

import java.util.List;

public interface FavoritesService {
    List<Currency> addFavorites(AddFavorites addFavorites, String usernameFromToken);

    FavoritesResponse getAllFavorites(int pageNo, int pageSize, String sortBy, String sortDir);

    FavoritesResponse getAllFavoritesByUsername(String usernameFromToken, int pageNo, int pageSize, String sortBy, String sortDir);

    void deleteFavorites(AddFavorites body, String usernameFromToken);
}
