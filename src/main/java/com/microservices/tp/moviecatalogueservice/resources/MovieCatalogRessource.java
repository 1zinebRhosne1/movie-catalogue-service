package com.microservices.tp.moviecatalogueservice.resources;


import java.util.List;
import java.util.stream.Collectors;

import com.microservices.tp.moviecatalogueservice.models.CatalogItem;
import com.microservices.tp.moviecatalogueservice.models.Movie;

import com.microservices.tp.moviecatalogueservice.models.UserRating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogRessource {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    public List<CatalogItem>getCatalog(@PathVariable("userId") String userId) {
        
        UserRating ratings = restTemplate.getForObject("http://rating-data-service/ratingsdata/users/" + userId, UserRating.class);
        /*
        List<Rating> ratings = Arrays.asList(
                new Rating("1234", 4),
                new Rating("1265", 4)//array of bunch of ratings
        );*/
        return ratings.getRatings().stream().map(rating -> {
                Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);//takes two objects
                System.out.println(movie.getMovieId());
                return new CatalogItem(movie.getName(), "Test", rating.getRating()) ;
                })
                 .collect(Collectors.toList());

    }
}
