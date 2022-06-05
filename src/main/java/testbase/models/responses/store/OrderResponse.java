package testbase.models.responses.store;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private int id;
    private int petId;
    private int quantity;
    private Date shipDate;
    private String status;
    private boolean complete;
}
