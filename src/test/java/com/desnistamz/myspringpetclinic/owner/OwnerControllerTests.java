package com.desnistamz.myspringpetclinic.owner;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

@WebMvcTest(OwnerController.class)
public class OwnerControllerTests {

    private  static final int TEST_OWNER_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerRepository owners;

    private Owner george(){
        Owner george = new Owner();
        george.setId(TEST_OWNER_ID);
        george.setFirst_name("George");
        george.setLast_name("Franklin");
        george.setAddress("110 W. Liberty St.");
        george.setCity("Madison");
        george.setTelephone("98775788990");
        Pet max = new Pet();
        PetType dog = new PetType();
        dog.setName("dog");
        max.setType(dog);
        max.setName("Max");
        max.setBirthDate(LocalDate.now());
        george.addPet(max);
        max.setId(1);
        return george;
    }
}
