package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controllers;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.controller.ProvinceController;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.ProvinceRepository;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.repositories.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.plaf.nimbus.AbstractRegionPainter;

public class ProvinceControllerTest {


    @Mock
    private ProvinceRepository provinceRepository;

   @Mock
   private RegionRepository regionRepository;


   @InjectMocks
    private ProvinceController provinceController;


   @Mock
    private Model model;


   @Mock
    private RedirectAttributes redirectAttributes;

@BeforeEach

public void setUp(){
    MockitoAnnotations.openMocks(this);


}


}
