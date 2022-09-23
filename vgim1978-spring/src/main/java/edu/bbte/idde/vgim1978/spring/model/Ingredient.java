package edu.bbte.idde.vgim1978.spring.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@Table(name = "IngredientTable")
public class Ingredient extends BaseEntity {

    @Column
    private String name;
    @Column
    private Long calorieWeight;

    @OneToMany(orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Collection<Menu> menus;

    public Ingredient(Long id, String name, Long calorieWeight) {
        super(id);
        this.name = name;
        this.calorieWeight = calorieWeight;
    }

}
