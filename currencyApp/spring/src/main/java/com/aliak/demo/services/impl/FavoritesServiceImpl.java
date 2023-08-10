package com.aliak.demo.services.impl;

import com.aliak.demo.entity.Currency;
import com.aliak.demo.entity.Favorites;
import com.aliak.demo.exception.CurrencyAPIException;
import com.aliak.demo.exception.ResourceNotFoundException;
import com.aliak.demo.payload.AddFavorites;
import com.aliak.demo.payload.FavoritesResponse;
import com.aliak.demo.repositories.CurrencyRepository;
import com.aliak.demo.repositories.FavoritesRepository;
import com.aliak.demo.services.FavoritesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritesServiceImpl implements FavoritesService {
    private final FavoritesRepository favoritesRepository;
    private final CurrencyRepository currencyRepository;
    @Override
    public List<Currency> addFavorites(@Valid AddFavorites addFavorites, String usernameFromToken) {

        for (Long id:addFavorites.getCurrencyIds()) {
            Currency currency = currencyRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Currency","id",id));

            if(favoritesRepository.findFavoritesByUsernameAndCurrencyId(usernameFromToken,id) == null) {
                Favorites favorite = new Favorites();
                favorite.setCurrency(currency);
                favorite.setUsername(usernameFromToken);
                Favorites newFavorite = favoritesRepository.save(favorite);
                favorite = null;
            }

        }
        List<Favorites> favoriteCurrencies = new ArrayList<>(favoritesRepository.findAllByUsername(usernameFromToken));

        return favoriteCurrencies.stream()
                .map(Favorites::getCurrency)
                .toList();

    }

    @Override
    public FavoritesResponse getAllFavorites(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Favorites> currencies = favoritesRepository.findAll(pageable);

        List<Favorites> content = currencies.getContent();

        return new FavoritesResponse(content,
                currencies.getNumber(),
                currencies.getSize(),
                currencies.getTotalElements(),
                currencies.getTotalPages(),
                currencies.isLast()
        );
    }

    @Override
    public FavoritesResponse getAllFavoritesByUsername(String usernameFromToken, int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Favorites> currencies = favoritesRepository.findAllByUsername(usernameFromToken,pageable);

        List<Favorites> content = currencies.getContent();

        return new FavoritesResponse(content,
                currencies.getNumber(),
                currencies.getSize(),
                currencies.getTotalElements(),
                currencies.getTotalPages(),
                currencies.isLast()
        );
    }

    @Override
    public void deleteFavorites(@Valid AddFavorites body, String usernameFromToken) {

        for (Long id:body.getCurrencyIds()) {
            currencyRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Currency","id",id));
            if(favoritesRepository.findFavoritesByUsernameAndCurrencyId(usernameFromToken,id) == null){
                throw new ResourceNotFoundException("Favorites","id",id);
            };

            if(favoritesRepository.findFavoritesByUsernameAndCurrencyId(usernameFromToken,id) != null) {
                favoritesRepository.deleteById(id);
            }
        }
    }






}



