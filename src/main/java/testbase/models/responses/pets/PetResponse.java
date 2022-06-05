package testbase.models.responses.pets;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponse {

    public int id;
    public String name;
    public Category category;
    public List<String> photoUrls;
    public List<Tag> tags;
    public String status;
}
