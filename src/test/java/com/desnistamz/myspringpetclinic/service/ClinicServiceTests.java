package com.desnistamz.myspringpetclinic.service;


import com.desnistamz.myspringpetclinic.owner.*;
import com.desnistamz.myspringpetclinic.vet.Vet;
import com.desnistamz.myspringpetclinic.vet.VetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;

/*Integration test of the Service and the Repository layer. ...*/
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
//Ensure that if the mysql profile is active we connect to the real database:
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ClinicServiceTests {

    @Autowired
    protected OwnerRepository owners;

    @Autowired
    protected VetRepository vets;

    Pageable pageable;

    @Test
    void shouldFindOwnerByLastName(){
        Page<Owner> owners = this.owners.findByLastName("Davis", pageable);
        assertThat(owners).hasSize(2);

        owners = this.owners.findByLastName("Daviss", pageable);
        assertThat(owners).isEmpty();
    }

    @Test
    void shouldFindSingleOwnerWithPet(){
        Owner owner = this.owners.findById(1);
        assertThat(owner.getLast_name()).startsWith("Franklin");
        assertThat(owner.getPets()).hasSize(1);
        assertThat(owner.getPets().get(0).getType()).isNotNull();
        assertThat(owner.getPets().get(0).getType().getName()).isEqualTo("cat");
    }

    @Test
    @Transactional
    void shouldInsertOwner(){
        Page<Owner> owners = this.owners.findByLastName("Schultz", pageable);
        int found = (int) owners.getTotalElements();

        Owner owner = new Owner();
        owner.setFirst_name("Sam");
        owner.setLast_name("Schultz");
        owner.setAddress("4, Evans Street");
        owner.setCity("Wollongong");
        owner.setTelephone("4444444444");
        this.owners.save(owner);
        assertThat(owner.getId().longValue()).isNotEqualTo(0);

        owners = this.owners.findByLastName("Schultz", pageable);
        assertThat(owners.getTotalElements()).isEqualTo(found + 1);
    }

    @Test
    @Transactional
    void shouldUpdateOwner(){
        Owner owner = this.owners.findById(1);
        String oldLastName = owner.getLast_name();
        String newLastName = oldLastName + "X";

        owner.setLast_name(newLastName);
        this.owners.save(owner);

        //retrieving new name from database
        owner = this.owners.findById(1);
        assertThat(owner.getLast_name()).isEqualTo(newLastName);
    }

    @Test
    void shouldFindAllPetTypes(){
        Collection<PetType> petTypes = this.owners.findPetTypes();

        PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
        assertThat(petType1.getName()).isEqualTo("cat");
        PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
        assertThat(petType1.getName()).isEqualTo("snake");
    }

    @Test
    @Transactional
    void shouldInsertPetIntoDatabaseAndGenerateId(){
        Owner owner6 = this.owners.findById(6);
        int found = owner6.getPets().size();

        Pet pet = new Pet();
        pet.setName("bowser");
        Collection<PetType> types = this.owners.findPetTypes();
        pet.setType(EntityUtils.getById(types, PetType.class,2));
        pet.setBirthDate(LocalDate.now());
        owner6.addPet(pet);
        assertThat(owner6.getPets().size()).isEqualTo(found + 1);

        this.owners.save(owner6);

        owner6 = this.owners.findById(6);
        assertThat(owner6.getPets().size()).isEqualTo(found + 1);
        //checks that id hsa been generated
        pet = owner6.getPet("bowser");
        assertThat(pet.getId()).isNotNull();
    }

    @Test
    @Transactional
    void shouldUpdatePetName() throws Exception{
        Owner owner6 = this.owners.findById(6);
        Pet pet7 = owner6.getPet(7);
        String oldName = pet7.getName();

        String newName = oldName + "X";
        pet7.setName(newName);
        this.owners.save(owner6);

        owner6 = this.owners.findById(6);
        pet7 = owner6.getPet(7);
        assertThat(pet7.getName()).isEqualTo(newName);
    }

    @Test
    void shouldFindVets(){
        Collection<Vet> vets = this.vets.findAll();

        Vet vet = EntityUtils.getById(vets, Vet.class, 3);
        assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
        assertThat(vet.getSpecialties().get(0).getName()).isEqualTo("dentistry");
        assertThat(vet.getSpecialties().get(1).getName()).isEqualTo("surgery");
    }

    @Test
    @Transactional
    void shouldAddNewVisitForPets(){
        Owner owner6 = this.owners.findById(6);
        Pet pet7 = owner6.getPet(7);
        int found = pet7.getVisits().size();
        Visit visit = new Visit();
        visit.setDescription("test");

        owner6.addVisit(pet7.getId(), visit);
        this.owners.save(owner6);

        owner6 = this.owners.findById(6);

        assertThat(pet7.getVisits())
                .hasSize(found + 1)
                .allMatch(value -> value.getId() != null);
    }

    @Test
    void shouldFindVisitsByPetId() throws Exception{
        Owner owner6 = this.owners.findById(6);
        Pet pet7 = owner6.getPet(7);
        Collection<Visit> visits = pet7.getVisits();

        assertThat(visits)
                .hasSize(2)
                .element(0).extracting(Visit::getDate).isNotNull();
    }
}
