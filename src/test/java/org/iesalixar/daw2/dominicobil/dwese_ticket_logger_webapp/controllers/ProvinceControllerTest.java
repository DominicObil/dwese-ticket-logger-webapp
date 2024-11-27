package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controllers;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controller.ProvinceController;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Province;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entities.Region;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.ProvinceRepository;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProvinceControllerTest {

    @Mock
    private ProvinceRepository provinceRepository;

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private ProvinceController provinceController;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListProvinces() {
        // Configuración de datos simulados
        List<Province> provinces = new ArrayList<>();
        provinces.add(new Province());
        provinces.add(new Province());

        when(provinceRepository.findAll()).thenReturn(provinces);

        // Llamada al método del controlador
        String viewName = provinceController.listProvinces(model);

        // Verificaciones
        verify(model).addAttribute("listProvinces", provinces); // Ajustado al plural
        assertEquals("province", viewName); // Ajustado al nombre correcto de la vista
    }


    @Test
    public void testShowNewForm() {
        // Crear una lista de regiones simulada
        List<Region> regions = new ArrayList<>();
        regions.add(new Region()); // Se añade una región
        regions.add(new Region()); // Se añade otra región

        // Configurar el comportamiento del mock
        when(regionRepository.findAll()).thenReturn(regions);

        // Llamar al método bajo prueba
        String viewName = provinceController.showNewForm(model);

        // Verificar el comportamiento esperado
        verify(model).addAttribute(eq("province"), any(Province.class));
        verify(model).addAttribute("listRegions", regions);
        assertEquals("province-form", viewName);
    }

    @Test
    public void testInsertProvince() {
        // Crear una provincia válida
        Province province = new Province();
        province.setCode("TEST");

        // No hay errores de validación
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        // Configurar el comportamiento del mock
        when(provinceRepository.existsProvinceByCode(province.getCode())).thenReturn(false);

        // Llamar al método bajo prueba
        String redirectView = provinceController.insertProvince(province, result, mock(RedirectAttributes.class), Locale.getDefault(), model);

        // Verificar que se guarda la provincia y la vista es la correcta
        verify(provinceRepository).save(province);
        assertEquals("redirect:/provinces", redirectView);
    }

    @Test
    public void testShowEditForm() {
        Long provinceId = 1L;

        // Crear una provincia simulada
        Province province = new Province();
        province.setId(provinceId);
        province.setCode("TEST");

        // Crear una lista de regiones simulada
        List<Region> regions = new ArrayList<>();
        regions.add(new Region());

        // Configurar el comportamiento del mock
        when(provinceRepository.findById(provinceId)).thenReturn(Optional.of(province));
        when(regionRepository.findAll()).thenReturn(regions);

        // Llamar al método bajo prueba
        String viewName = provinceController.showEditForm(provinceId, model);

        // Verificar que se añaden los atributos correctos al modelo
        verify(model).addAttribute("province", Optional.of(province));  // Expect Optional
        verify(model).addAttribute("listRegions", regions);

        // Verificar que la vista devuelta sea la correcta
        assertEquals("province-form", viewName);
    }


}