package com.desnistamz.myspringpetclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitController.class)
public class VisitControllerTests {

    private static final int TEST_OWNER_ID = 1;

    private static final int TEST_PET_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerRepository owners;

    @BeforeEach
    void init(){
        Owner owner = new Owner();
        Pet pet = new Pet();
        owner.addPet(pet);
        pet.setId(TEST_PET_ID);
        given(this.owners.findById(TEST_OWNER_ID)).willReturn(owner);
    }

    @Test
    void testInitNewVisitForm() throws Exception{
        mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID,TEST_PET_ID))
                .andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdateVisitForm"));
    }

    @Test
    void testProcessNewVisitFormSuccess() throws Exception{
        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID,TEST_PET_ID)
                .param("name","George").param("description", "Visit Description"))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    void testProcessNewVisitFormHasErrors() throws Exception{
        mockMvc.perform(
                post("/owners/{ownerId}/pets/{petId}/visits/new", TEST_OWNER_ID,TEST_PET_ID).param("name","George"))
                .andExpect(model().attributeHasErrors("visit")).andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdateForm"));
    }
}
