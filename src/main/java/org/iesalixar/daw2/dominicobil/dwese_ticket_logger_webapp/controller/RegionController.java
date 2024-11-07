package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controller;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao.RegionDAO;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Region`.
 * Utiliza `RegionDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/regions")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    // DAO para gestionar las operaciones de las regiones en la base de datos
    @Autowired
    private RegionDAO regionDAO;

    /**
     * Lista todas las regiones y las pasa como atributo al modelo para que sean
     * accesibles en la vista `region.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de regiones.
     */
    @GetMapping
    public String listRegions(Model model) {
        logger.info("Requesting the list of all regions...");
        List<Region> listRegions = regionDAO.listAllRegions();
        logger.info("Loaded {} regions.", listRegions.size());
        model.addAttribute("listRegions", listRegions); // Pasar la lista de regiones al modelo
        return "region"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    /**
     * Muestra el formulario para crear una nueva región.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Displaying form to create a new region.");
        model.addAttribute("region", new Region()); // Crear un nuevo objeto Region
        return "region-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    /**
     * Muestra el formulario para editar una región existente.
     *
     * @param id    ID de la región a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        logger.info("Displaying edit form for the region with ID {}", id);
        Region region = regionDAO.getRegionById(id);
        if (region == null) {
            logger.warn("Region with ID {} not found.", id);
        }
        model.addAttribute("region", region);
        return "region-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @Autowired
    private MessageSource messageSource;

    /**
     * Inserta una nueva región en la base de datos.
     *
     * @param region              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de regiones.
     */
    @PostMapping("/insert")
    public String insertRegion(@Valid @ModelAttribute("region") Region region, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Inserting new region with code {}", region.getCode());
        if (result.hasErrors()) {
            return "region-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (regionDAO.existsRegionByCode(region.getCode())) {
            logger.warn("Region code {} already exists.", region.getCode());
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/regions/new";
        }
        regionDAO.insertRegion(region);
        logger.info("Region {} inserted successfully.", region.getCode());
        return "redirect:/regions"; // Redirigir a la lista de regiones
    }

    /**
     * Actualiza una región existente en la base de datos.
     *
     * @param region              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de regiones.
     */
    @PostMapping("/update")
    public String updateRegion(@Valid @ModelAttribute("region") Region region, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Updating region with ID {}", region.getId());
        if (result.hasErrors()) {
            return "region-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (regionDAO.existsRegionByCodeAndNotId(region.getCode(), region.getId())) {
            logger.warn("Region code {} already exists for another region.", region.getCode());
            String errorMessage = messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/regions/edit?id=" + region.getId();
        }
        regionDAO.updateRegion(region);
        logger.info("Region with ID {} updated successfully.", region.getId());
        return "redirect:/regions"; // Redirigir a la lista de regiones
    }

    /**
     * Elimina una región de la base de datos.
     *
     * @param id                 ID de la región a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de regiones.
     */
    @PostMapping("/delete")
    public String deleteRegion(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        logger.info("Deleting region with ID {}", id);
        regionDAO.deleteRegion(id);
        logger.info("Region with ID {} deleted successfully.", id);
        return "redirect:/regions"; // Redirigir a la lista de regiones
    }
}
