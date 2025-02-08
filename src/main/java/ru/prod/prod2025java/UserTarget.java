package ru.prod.prod2025java;

import jakarta.persistence.Embeddable;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import ru.prod.prod2025java.validators.CountryName;

@Data
@Embeddable
public class UserTarget {
    @Range(min = 0, max = 100)
    private int age;

    @CountryName
    private String country;
}
