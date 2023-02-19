package com.desnistamz.myspringpetclinic.owner;

import com.desnistamz.myspringpetclinic.model.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "owners")
@Getter
@Setter
public class Owner extends Person {

    @Column(name = "address")
    @NotEmpty
    private String address;

    @Column(name = "city")
    @NotEmpty
    private String city;

    @Column(name = "telephone")
    @NotEmpty
    @Digits(fraction = 0, integer = 10)
    private String telephone;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @OrderBy("name")
    private List<Pet> pets = new ArrayList<>();


    public void addPet(Pet pet){
        if (pet.isNew()){
            getPets().add(pet);
        }
    }

    public Pet getPet(String name){
        return getPet(name, false);
    }

    public Pet getPet(Integer id){
        for (Pet pet : getPets()){
            if (!pet.isNew()){
                Integer compId = pet.getId();
                if (compId.equals(id)){
                    return pet;
                }
            }
        }
        return null;
    }

    public Pet getPet(String name, boolean ignoreNew){
        name = name.toLowerCase();
        for (Pet pet : getPets()){
            if (!ignoreNew || !pet.isNew()){
                String compName = pet.getName();
                compName = compName == null? "" : compName.toLowerCase();
                if (compName.equals(name)){
                    return pet;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this).append("id", this.getId()).append("new", this.isNew())
                .append("lastName", this.getLast_name()).append("firstName", this.getFirst_name())
                .append("address", this.address).append("city", this.city).append("telephone",this.telephone)
                .toString();
    }

    public Owner addVisit(Integer petId, Visit visit){

        Assert.notNull(petId, "Pet identifier must not be null!");
        Assert.notNull(visit, "Visit must not be null!");

        Pet pet = getPet(petId);

        Assert.notNull(pet, "Invalid Pet identifier!");

        pet.addVisit(visit);

        return this;
    }
}
