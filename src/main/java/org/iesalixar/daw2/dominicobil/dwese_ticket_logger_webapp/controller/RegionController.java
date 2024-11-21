package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controller;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Region;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/regions")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listRegions(Model model) {
        logger.info("Requesting the list of all regions...");
        List<Region> listRegions = regionRepository.findAll();
        logger.info("Loaded {} regions.", listRegions.size());
        model.addAttribute("listRegions", listRegions);
        return "region";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Displaying form to create a new region.");
        model.addAttribute("region", new Region());
        return "region-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Displaying edit form for the region with ID {}", id);
        Optional<Region> regionOptional = regionRepository.findById(id);

        if (regionOptional.isEmpty()) {
            logger.warn("Region with ID {} not found.", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Region not found.");
            return "redirect:/regions";
        }

        model.addAttribute("region", regionOptional.get());
        return "region-form";
    }

    @PostMapping("/insert")
    public String insertRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        if (result.hasErrors()) {
            return "region-form";
        }

        logger.info("Inserting new region with code {}", region.getCode());

        if (regionRepository.existsRegionByCode(region.getCode())) {
            logger.warn("Region code {} already exists.", region.getCode());
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/regions/new";
        }

        regionRepository.save(region);
        logger.info("Region {} inserted successfully.", region.getCode());
        return "redirect:/regions";
    }

    @PostMapping("/update")
    public String updateRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Updating region with ID {}", region.getId());

        if (result.hasErrors()) {
            return "region-form";
        }

        if (regionRepository.existsRegionByCodeAndNotId(region.getCode(), region.getId())) {
            logger.warn("Region code {} already exists for another region.", region.getCode());
            String errorMessage = messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/regions/edit?id=" + region.getId();
        }

        regionRepository.save(region);
        logger.info("Region with ID {} updated successfully.", region.getId());
        return "redirect:/regions";
    }

    /**
     * Elimina una región de la base de datos.
     *
     * @param id                 ID de la región a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de regiones.
     */
    @PostMapping("/delete")
    public String deleteRegion(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando región con ID {}", id);
        // Obtener el objeto de autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        // Verificar si el usuario tiene el rol ADMIN
        if (auth == null || !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            String username = (auth != null) ? auth.getName() : "Usuario desconocido";
            String errorMessage = "El usuario " + username + " no tiene permisos para borrar la región.";


            logger.warn(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);


            return "redirect:/403";  // Redirige a la página de error 403 o a otra página de tu elección
        }
        try {
            regionRepository.deleteById(id);
            logger.info("Región con ID {} eliminada con éxito.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar la región con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la región.");
        }
        return "redirect:/regions"; // Redirigir a la lista de regiones
    }
}