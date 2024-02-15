package booksville.infrastructure.controllers;

import booksville.payload.request.RatingsAndReviewRequest;
import booksville.payload.response.ApiResponse;
import booksville.payload.response.ViewRatingsResponse;
import booksville.services.RatingAndReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ratings")
public class RatingsAndReviewController {
    private final RatingAndReviewService ratingAndReviewService;


    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> ratingABook (@PathVariable("id") Long id, @RequestBody RatingsAndReviewRequest ratingsAndReviewRequest) {
         return ratingAndReviewService.addRatingAndReview(id, ratingsAndReviewRequest);
    }


    @GetMapping("/view/{id}")
    public ResponseEntity<ApiResponse<List<ViewRatingsResponse>>> viewAllRatingsAndReviewForABook(@PathVariable("id") Long bookId){
        return ratingAndReviewService.viewAllRatingsAndReviewForABook(bookId);
    }
}
