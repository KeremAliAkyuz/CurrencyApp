package com.aliak.demo.controllers;

import com.aliak.demo.advise.TrackExecutionTime;
import com.aliak.demo.entity.Currency;
import com.aliak.demo.payload.AddFavorites;
import com.aliak.demo.payload.FavoritesResponse;
import com.aliak.demo.services.impl.FavoritesServiceImpl;
import com.aliak.demo.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("favorites")
@RequiredArgsConstructor
@Slf4j
public class FavoritesController {
    private final FavoritesServiceImpl favoritesService;

    @PreAuthorize("hasRole('currency_banker')")
    @PostMapping("/addFavorites")
    @TrackExecutionTime
    public List<Currency> addFavorites(
            @RequestBody AddFavorites addFavorites,
            @AuthenticationPrincipal Jwt principal) {
        log.debug("add favorites method has been reached");
        String usernameFromToken = principal.getClaim("preferred_username");
        return favoritesService.addFavorites(addFavorites,usernameFromToken);
    }

    @PreAuthorize("hasRole('currency_admin')")
    @GetMapping("/getAll")
    @TrackExecutionTime
    public FavoritesResponse getAllFavorites(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        log.debug("get all favorites method has been reached");
        return favoritesService.getAllFavorites(pageNo,pageSize,sortBy,sortDir);
    }

    @PreAuthorize("hasRole('currency_banker')")
    @GetMapping("/getAllByUsername")
    @TrackExecutionTime
    public FavoritesResponse getAllFavoritesByUsername(
            @AuthenticationPrincipal Jwt principal,
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        log.debug("get all favorites by username method has been reached");
        String usernameFromToken = principal.getClaim("preferred_username");
        return favoritesService.getAllFavoritesByUsername(usernameFromToken,pageNo,pageSize,sortBy,sortDir);
    }

    @PreAuthorize("hasRole('currency_banker')")
    @DeleteMapping("/deleteFavorites")
    @TrackExecutionTime
    public ResponseEntity<String> deleteFavorites(
            @RequestBody AddFavorites body,
            @AuthenticationPrincipal Jwt principal
    ){
        log.debug("delete favorites method has been reached");
        String usernameFromToken = principal.getClaim("preferred_username");
        favoritesService.deleteFavorites(body,usernameFromToken);
        return new ResponseEntity<>("Favorites entities deleted successfully.", HttpStatus.OK);
    }

}
