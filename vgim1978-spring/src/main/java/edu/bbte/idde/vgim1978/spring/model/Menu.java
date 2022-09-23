package edu.bbte.idde.vgim1978.spring.model;

import lombok.*;
import javax.persistence.*;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "MenuTable")

public class Menu extends BaseEntity {
    @Column
    private String type;
    @Column(nullable = false)
    private String name;
    @Column
    private String description;
    @Column
    private Boolean vegetarian;
    @Column(nullable = false)
    private Long calorieWeight;
    @Column(nullable = false)
    private Long price;
    @Transient
    private Ingredient mainIngredient;

    @OneToMany(orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Collection<CrudEvent> events;
}
