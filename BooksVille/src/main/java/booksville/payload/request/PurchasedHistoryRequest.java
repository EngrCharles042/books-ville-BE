package booksville.payload.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchasedHistoryRequest {
    private Long id;
    private LocalDateTime dateCreated;
    private String author;
    private String bookTitle;
    private String genre;
    private String description;
    private BigDecimal price;
    private String bookCover;
}
