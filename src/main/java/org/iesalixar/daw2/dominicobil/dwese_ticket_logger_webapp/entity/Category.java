package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "image", nullable = true, length = 500) // Permite valores nulos y longitud máxima de 500
    private String image;

    @ManyToOne(optional = true) // `optional = true` permite que `parent_id` sea `null`
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_parent_category"))
    @JsonBackReference // Evita la recursión en la serialización JSON
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference // Gestiona la serialización de subcategorías
    private List<Category> subcategories;

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", parent=" + (parent != null ? parent.getId() : "null") + // Muestra solo el ID del padre
                ", subcategoriesCount=" + (subcategories != null ? subcategories.size() : 0) +
                '}';
    }
}
