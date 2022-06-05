package testbase.models.requests.pets;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category{
    private int id;
    private String name;
}
