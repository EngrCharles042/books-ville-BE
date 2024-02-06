package booksville.payload.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPurchasedResponse {

    private Long id;
    private LocalDateTime dateCreated;
    private String author;
    private String bookTitle;
    private String genre;
    private String description;
    private BigDecimal price;
    private String bookCover;
}
