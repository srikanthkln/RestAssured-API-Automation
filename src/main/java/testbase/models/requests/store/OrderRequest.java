package testbase.models.requests.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    private int id;
    private int petId;
    private int quantity;
    private Date shipDate;
    private String status;
    private boolean complete;
}
