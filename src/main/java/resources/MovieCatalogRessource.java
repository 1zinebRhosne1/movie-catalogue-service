package resources;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import models.CatalogItem;
import models.Movie;
import models.Rating;
import models.UserRating;
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


        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating ->{
            //pour chaque movieID,fait appel à movie info et get the details
                 Movie movie = restTemplate.getForObject("localhost:8081/movies/"+rating.getMovieId(), Movie.class);//takes two objects
            //rassembler le tt
                return new CatalogItem(movie.getName(), "Test", rating.getRating()) ;
    })
                 .collect(Collectors.toList());

    }
}
