package com.aliak.demo.repositories;

import com.aliak.demo.entity.Currency;
import com.aliak.demo.entity.Favorites;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites,Long> {


    List<Favorites> findAllByUsername(String usernameFromToken);

    Page<Favorites> findAllByUsername(String usernameFromToken, Pageable pageable);

    Favorites findFavoritesByUsernameAndCurrencyId(String usernameFromToken,Long currencyId);


}
