package com.desnistamz.myspringpetclinic.owner;


import org.assertj.core.util.Lists;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @BeforeEach
    void setup(){

        Owner george = george();
        given(this.owners.findByLastName(eq("Franklin"), any(Pageable.class)))
                .willReturn(new PageImpl<Owner>(Lists.newArrayList(george)));

        given(this.owners.findAll(any(Pageable.class))).willReturn(new PageImpl<Owner>(Lists.newArrayList(george)));

        given(this.owners.findById(TEST_OWNER_ID)).willReturn(george);
        Visit visit = new Visit();
        visit.setDate(LocalDate.now());
        george.getPet("Max").getVisits().add(visit);

    }

    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/owners/new")).andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void testProcessCreationFormSuccess() throws Exception{
        mockMvc.perform(post("/owners/new").param("firstName", "Joe").param("lastName", "Bloggs")
                .param("address", "123 Caramel Street").param("city", "London").param("telephone", "23680427497"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testProcessCreationFormHasErrors() throws Exception{
        mockMvc.perform(
                post("/owners/new").param("firstName", "Joe").param("lastName","Bloggs")
                        .param("city","London"))
                .andExpect(status().isOk()).andExpect(model().attributeHasErrors("owners"))
                .andExpect(model().attributeHasFieldErrors("owner", "address"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void testInitFindForm() throws Exception{
        mockMvc.perform(get("/owners/find")).andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testProcessFindFormSuccess() throws Exception{
        Page<Owner> tasks = new PageImpl<Owner>(Lists.newArrayList(george(), new Owner()));
        Mockito.when(this.owners.findByLastName(anyString(), any(Pageable.class))).thenReturn(tasks);
        mockMvc.perform(get("/owners?page=1")).andExpect(status().isOk()).andExpect(view().name("owners/ownersList"));
    }

    @Test
    void testProcessFindFormByLastName() throws Exception{
        Page<Owner> tasks = new PageImpl<Owner>(Lists.newArrayList(george()));
        Mockito.when(this.owners.findByLastName(eq("Franklin"), any(Pageable.class))).thenReturn(tasks);
        mockMvc.perform(get("/owners?page=1").param("lastName", "Unknown Surname")).andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("owner","lastName"))
                .andExpect(model().attributeHasFieldErrorCode("owner", "lastName","notFound"))
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testInitUpdateOwnerForm() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/edit", TEST_OWNER_ID)).andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
                .andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
                .andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
                .andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
                .andExpect(model().attribute("owner", hasProperty("telephone", is("8978765647"))))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void testProcessUpdateOwnerFormSuccess() throws Exception{
        mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID).param("firstName", "Joe")
                .param("lastName", "Bloggs").param("address", "123 Caramel Street").param("city", "London")
                .param("telephone", "7643982767")).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    void testProcessUpdateOwnerFormUnchangedSuccess() throws Exception{
        mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID)).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/{ownersId}"));
    }

    @Test
    void testProcessUpdateOwnerFormHasErrors() throws Exception{
        mockMvc.perform(post("/owners/{ownerId}/edit",TEST_OWNER_ID).param("firstName", "Joe")
                .param("lastName", "Bloggs").param("address", "").param("telephone", "")).andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owners"))
                .andExpect(model().attributeHasFieldErrors("owner","address"))
                .andExpect(model().attributeHasFieldErrors("owner","telephone"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void testShowOwner() throws Exception{
        mockMvc.perform(get("/owners/{ownerId}",TEST_OWNER_ID)).andExpect(status().isOk())
                .andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
                .andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
                .andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
                .andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
                .andExpect(model().attribute("owner", hasProperty("telephone", is("8978765647"))))
                .andExpect(model().attribute("owner", hasProperty("pets", not(empty()))))
                .andExpect(model().attribute("owner", hasProperty("pets", new BaseMatcher<List<Pet>>() {

                    @Override
                    public boolean matches(Object item){
                        @SuppressWarnings("unchecked")
                                List<Pet> pets = (List<Pet>) item;
                        Pet pet = pets.get(0);
                        if (pet.getVisits().isEmpty()){
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public void describeTo(Description description){
                        description.appendText("Max did not have any visits");
                    }
                }))).andExpect(view().name("owners/ownerDetails"));

    }
}
