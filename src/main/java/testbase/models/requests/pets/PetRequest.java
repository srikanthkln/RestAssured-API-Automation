package testbase.models.requests.pets;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetRequest {

    private int id;
    private String name;
    private Category category;
    private List<String> photoUrls;
    private List<Tag> tags;
    private String status;
}
