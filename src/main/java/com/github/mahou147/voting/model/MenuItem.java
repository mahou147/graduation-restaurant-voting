package com.github.mahou147.voting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "title"},
        name = "menu_item_unique_title_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuItem extends BaseEntity {

    @Column(name = "title", nullable = false)
    @NotBlank
    @Size(max = 128)
    private String title;

    @NotNull
    @Column(name = "price", nullable = false)
    @DecimalMax("100000")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Menu menu;

    public MenuItem(Integer id, String title, BigDecimal price) {
        super(id);
        this.title = title;
        this.price = price;
    }
}
