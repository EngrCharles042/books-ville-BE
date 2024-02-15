package booksville.services.implementation;

import booksville.entities.model.BookEntity;
import booksville.entities.model.RatingsAndReviewEntity;
import booksville.entities.model.UserEntity;
import booksville.infrastructure.exceptions.ApplicationException;
import booksville.infrastructure.security.JWTGenerator;
import booksville.payload.request.RatingsAndReviewRequest;
import booksville.payload.response.ApiResponse;
import booksville.payload.response.ViewRatingsResponse;
import booksville.repositories.BookEntityRepository;
import booksville.repositories.RatingsAndReviewRepository;
import booksville.repositories.UserEntityRepository;
import booksville.services.RatingAndReviewService;
import booksville.utils.HelperClass;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingsAndReviewServiceImpl implements RatingAndReviewService {
    private final RatingsAndReviewRepository ratingsAndReviewRepository;
    private final BookEntityRepository bookEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final HelperClass helperClass;
    private final HttpServletRequest httpServletRequest;
    private final JWTGenerator jwtGenerator;


    @Override
    public ResponseEntity<ApiResponse<String>> addRatingAndReview(Long bookId, RatingsAndReviewRequest ratingsAndReviewRequest) {
        BookEntity bookEntity = bookEntityRepository.findById(bookId)
                .orElseThrow(() -> new ApplicationException("Book with " + bookId + " not found"));

        String email = jwtGenerator.getEmailFromJWT(helperClass.getTokenFromHttpRequest(httpServletRequest));

        UserEntity userEntity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException("User with " + email + " not found"));
        RatingsAndReviewEntity ratingsAndReviewEntity = RatingsAndReviewEntity
                .builder()
                .rating(ratingsAndReviewRequest.getRating())
                .review(ratingsAndReviewRequest.getReview())
                .bookEntity(bookEntity)
                .userEntity(userEntity)
                .build();

        ratingsAndReviewRepository.save(ratingsAndReviewEntity);
        return ResponseEntity.ok(new ApiResponse<>("Your feedback was highly appreciated"));
    }

    @Override
    public ResponseEntity<ApiResponse<List<ViewRatingsResponse>>> viewAllRatingsAndReviewForABook(Long bookId) {
        List<RatingsAndReviewEntity> ratingsAndReviews = ratingsAndReviewRepository.findByBookEntityId(bookId);
        List<ViewRatingsResponse> viewRatingsResponseList = ratingsAndReviews.stream().map(ratingAndReview -> ViewRatingsResponse
                .builder()
                .rating(ratingAndReview.getRating())
                .review(ratingAndReview.getReview())
                .email(ratingAndReview.getUserEntity().getEmail())
                .dateCreated(ratingAndReview.getDateCreated())
                .build()).toList();
        return ResponseEntity.ok(new ApiResponse<>("successfully retrieved data", viewRatingsResponseList));
    }
}
