package testbase.models.responses.pets;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    public int id;
    public String name;
}
