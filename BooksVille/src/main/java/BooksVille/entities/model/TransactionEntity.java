package BooksVille.entities.model;

import BooksVille.entities.enums.TransactStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_table")
public class TransactionEntity extends BaseEntity{

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactStatus status;

    @Column(length = 20)
    private String referenceId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "book_id")
    private BookEntity bookEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}

//        amount:1000
//        charge_response_code:"00"
//        charge_response_message:"Approved Successful"
//        charged_amount:1000
//        created_at:"2024-01-16T08:58:01.000Z"
//        currency:"NGN"
//        customer:{name: 'Raph Igabor', email: 'eliteibe69@gmail.com', phone_number: '08012345678'}
//        flw_ref:"MockFLWRef-1705395481608"
//        redirectstatus:undefined
//        status:"completed"
//        transaction_id:4856331
//        tx_ref:1705395472434

//        message:"Approved"
//        redirecturl:"?trxref=T055952794119028&reference=T055952794119028"
//        reference:"T055952794119028"
//        status:"success"
//        trans:"3464923294"
//        transaction:"3464923294"
//        trxref:"T055952794119028"
