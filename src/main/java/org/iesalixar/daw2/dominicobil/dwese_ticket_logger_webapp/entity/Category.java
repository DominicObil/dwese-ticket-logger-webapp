package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * La clase `Category` representa una entidad que modela una categoría dentro de la base de datos.
 * Contiene tres campos: `id`, `name`, y `image`. El `id` es el identificador único de la categoría,
 * `name` es el nombre de la categoría, y `image` es la ruta de la imagen asociada a la categoría.
 *
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "categories") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor // Genera un constructor sin argumentos.
@AllArgsConstructor // Genera un constructor con todos los argumentos.
public class Category {

    // Es una clave primaria autogenerada por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Campo que almacena el nombre de la categoría. No puede estar vacío y tiene una longitud máxima.
    @NotEmpty(message = "{msg.category.name.notEmpty}") // Mensaje de error si el nombre está vacío.
    @Size(max = 255, message = "{msg.category.name.size}") // Mensaje de error si el tamaño excede 255 caracteres.
    @Column(name = "name", nullable = false, length = 255) // Define la columna correspondiente en la tabla.
    private String name;

    // Campo que almacena la ruta de la imagen asociada a la categoría. Es opcional y tiene una longitud máxima.
    @Size(max = 500, message = "{msg.category.image.size}") // Mensaje de error si el tamaño excede 500 caracteres.
    @Column(name = "image", nullable = true, length = 500) // Define la columna correspondiente en la tabla.
    private String image;

    // Relación opcional con la categoría padre, permite definir jerarquías entre categorías.
    @ManyToOne(fetch = FetchType.LAZY) // Relación de muchas categorías a una categoría padre.
    @JoinColumn(name = "parent_id", nullable = true) // Clave foránea en la tabla categories que referencia a la misma tabla.
    private Category parent;

    // Relación uno a muchos con categorías hijas. Una categoría puede tener múltiples subcategorías.
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Mapeo inverso de la relación.
    private List<Category> categories; // Lista de categorías hijas.

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Category`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarla en la base de datos).
     *
     * @param name Nombre de la categoría.
     * @param image Ruta de la imagen asociada a la categoría.
     * @param parent Categoría padre, si existe.
     */
    public Category(String name, String image, Category parent) { // Constructor específico para crear categorías.
        this.name = name;
        this.image = image;
        this.parent = parent;
    }
}
