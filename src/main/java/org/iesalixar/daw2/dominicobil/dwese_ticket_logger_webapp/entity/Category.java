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

    @Column(name = "image", length = 500)
    private String image;

    @ManyToOne
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_parent_category"))
    @JsonBackReference // Evita la recursión al serializar la relación padre
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference // Serializa la lista de subcategorías
    private List<Category> subcategories;

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", parent=" + (parent != null ? parent.getName() : "null") + // Solo muestra el nombre del padre
                ", subcategoriesCount=" + (subcategories != null ? subcategories.size() : 0) + // Cantidad de subcategorías
                '}';
    }
}
