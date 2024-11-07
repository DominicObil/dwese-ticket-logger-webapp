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
        logger.info("Solicitando la lista de todas las categorias...");
        List<Category> listCategories = categorieDAO.listAllCategory();
        logger.info("Se han cargado {} categorias.", listCategories.size());
        model.addAttribute("listCategories", listCategories);
        return "category"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva categoria.");
        model.addAttribute("categorie", new Category());
        List<Category> listCategories = categorieDAO.listAllCategory(); // Obtener la lista de Categorias
        model.addAttribute("listCategories", categorieDAO.listAllCategory());
        return "category-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Mostrando formulario de edición para la categoria con ID {}", id);
        Category category = categorieDAO.getCategoryById(id);
        List<Category> listCategories = categorieDAO.listAllCategory(); // Obtener la lista de categorias
        if (category == null) {
            logger.warn("No se encontró la categoria con ID {}", id);
            return "redirect:/categories"; // Redirigir si no se encuentra
        }
        model.addAttribute("category", category);
        model.addAttribute("listCategories", categorieDAO.listAllCategory());
        return "category-form";
    }

    @PostMapping("/insert")
    public String insertCategory(@Valid @ModelAttribute("category") Category category, BindingResult result,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes, Locale locale) {

        logger.info("Insertando nueva categoria con nombre {}", category.getName());

        if (result.hasErrors()) {
            return "category-form"; // Devuelve el formulario para mostrar los errores de validación
        }

        if (categorieDAO.existsCategoryByName(category.getName())) {
            logger.warn("El nombre de la categoria {} ya existe.", category.getName());
            String errorMessage = messageSource.getMessage("msg.categorie-controller.insert.NameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/categories/new";
        }

        // Guardar la imagen subida
        if (!imageFile.isEmpty()) {
            String fileName = fileStorageService.saveFile(imageFile);
            if (fileName != null) {
                category.setImage(fileName); // Guardar el nombre del archivo en la entidad
            }
        }

        categorieDAO.insertCategory(category);
        logger.info("Categoria {} insertada con éxito.", category.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Categoria creada con éxito.");
        return "redirect:/categories";
    }

    @PostMapping("/update")
    public String updateCategory(@Valid @ModelAttribute("categorie") Category category, BindingResult result,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes, Locale locale) {

        logger.info("Actualizando categoria con ID {}", category.getId());

        if (result.hasErrors()) {
            return "category-form"; // Devuelve el formulario para mostrar los errores de validación
        }

        if (categorieDAO.existsCategoryByNameAndNotId(category.getName(), category.getId())) {
            logger.warn("El nombre de la categoria {} ya existe para otra categoria.", category.getName());
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
        logger.info("Categoria con ID {} actualizada con éxito.", category.getId());
        return "redirect:/categories";
    }


    @PostMapping("/delete")
    public String deleteCategorie(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando categoria con ID {}", id);
        Category category = categorieDAO.getCategoryById(id);
        categorieDAO.deleteCategory(id);
        // Eliminar la imagen asociada, si existe
        if (category.getImage() != null && !category.getImage().isEmpty()) {
            fileStorageService.deleteFile(category.getImage());
        }

        logger.info("Categoria con ID {} eliminada con éxito.", id);
        return "redirect:/categories";
    }
}
