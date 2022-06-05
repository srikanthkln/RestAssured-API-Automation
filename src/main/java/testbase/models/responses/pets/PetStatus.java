package testbase.models.responses.pets;

import lombok.Getter;

@Getter
public enum PetStatus {

    AVAILABLE("available"),
    PENDING("pending"),
    SOLD("sold");

    @Getter
    private final String value;

    public static PetStatus get(String value) {
        for (PetStatus type : PetStatus.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException();
    }

    PetStatus(String value) {
        this.value = value;
    }
}

