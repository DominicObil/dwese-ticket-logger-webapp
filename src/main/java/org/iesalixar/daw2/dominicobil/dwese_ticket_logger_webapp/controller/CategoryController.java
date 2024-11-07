package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controller;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao.CategoryDAO;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Category;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Region;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private FileStorageService fileStorageService;


    @Autowired
    private CategoryDAO categorieDAO;

    @GetMapping
    public String listCategories(Model model) {
        // Solicitando la lista de todas las categorias...
        logger.info("Requesting the list of all categories...");
        List<Category> listCategories = categorieDAO.listAllCategory();
        // Se han cargado {} categorias.
        logger.info("Loaded {} categories.", listCategories.size());
        model.addAttribute("listCategories", listCategories);
        return "category"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        // Mostrando formulario para nueva categoria.
        logger.info("Displaying form to create a new category.");
        model.addAttribute("category", new Category()); // Crear un nuevo objeto Categoria
        List<Category> listCategories = categorieDAO.listAllCategory(); // Obtener la lista de Categorias
        model.addAttribute("listCategories", listCategories); // Pasar la lista de Categorias
        return "category-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        // Mostrando formulario de edición para la categoria con ID {}
        logger.info("Displaying edit form for the category with ID {}", id);
        Category category = categorieDAO.getCategoryById(id);
        List<Category> listCategories = categorieDAO.listAllCategory(); // Obtener la lista de categorias
        if (category == null) {
            // No se encontró la categoria con ID {}
            logger.warn("Category with ID {} not found.");
            return "redirect:/categories"; // Redirigir si no se encuentra
        }
        model.addAttribute("category", category);
        model.addAttribute("listCategories", categorieDAO.listAllCategory());
        return "category-form";
    }

    @PostMapping("/insert")
    public String insertCategory(@Valid @ModelAttribute("category") Category category, BindingResult result,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes, Locale locale, Model model) {

        // Verificar si hay errores en la validación
        if (result.hasErrors()) {
            List<Category> listCategories = categorieDAO.listAllCategory();
            model.addAttribute("listCategories", listCategories); // Pasar la lista de Categorias
            return "category-form"; // Devuelve el formulario para mostrar los errores de validación
        }

        // Insertando nueva categoria con nombre {}
        logger.info("Inserting new category with name {}", category.getName());

        // Verificar y asignar categoría padre si existe
        if (category.getParent() != null && category.getParent().getId() == null) {
            category.setParent(null); // No asignar categoría padre
        }

        // Guardar el archivo de imagen si se ha subido uno
        if (!imageFile.isEmpty()) {
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                category.setImage(fileName); // Guardar el nombre del archivo en la entidad
            }
        }

        categorieDAO.insertCategory(category); // Insertar la nueva categoria

        // Categoria {} insertada con éxito.
        logger.info("Category {} inserted successfully.", category.getName());
        return "redirect:/categories"; // Redirigir a la lista de categorias
    }

    @PostMapping("/update")
    public String updateCategory(@Valid @ModelAttribute("categorie") Category category, BindingResult result,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes, Locale locale) {

        // Actualizando categoria con ID {}
        logger.info("Updating category with ID {}", category.getId());

        if (result.hasErrors()) {
            return "category-form"; // Devuelve el formulario para mostrar los errores de validación
        }

        if (categorieDAO.existsCategoryByNameAndNotId(category.getName(), category.getId())) {
            // El nombre de la categoria {} ya existe para otra categoria.
            logger.warn("Category name {} already exists for another category.");
            String errorMessage = messageSource.getMessage("msg.categorie-controller.update.NameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/categories/edit?id=" + category.getId();
        }

        // Guardar la imagen subida
        if (!imageFile.isEmpty()) {
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                category.setImage(fileName); // Guardar el nombre del archivo en la entidad
            }
        }

        categorieDAO.updateCategory(category);
        // Categoria con ID {} actualizada con éxito.
        logger.info("Category with ID {} updated successfully.", category.getId());
        return "redirect:/categories";
    }


    @PostMapping("/delete")
    public String deleteCategorie(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        // Eliminando categoria con ID {}
        logger.info("Deleting category with ID {}", id);
        Category category = categorieDAO.getCategoryById(id);
        categorieDAO.deleteCategory(id);
        // Eliminar la imagen asociada, si existe
        if (category.getImage() != null && !category.getImage().isEmpty()) {
            fileStorageService.deleteFile(category.getImage());
        }

        // Categoria con ID {} eliminada con éxito.
        logger.info("Category with ID {} deleted successfully.", id);
        return "redirect:/categories";
    }
}
