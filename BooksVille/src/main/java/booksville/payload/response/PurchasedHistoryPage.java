package booksville.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchasedHistoryPage {
    private List<UserPurchasedResponse> purchasedResponseList;
    private int pageNo;
    private int pageSize;
    private boolean last;

}
