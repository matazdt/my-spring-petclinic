package com.desnistamz.myspringpetclinic.owner;

import com.desnistamz.myspringpetclinic.model.NamedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "pets")
@Getter
@Setter
public class Pet extends NamedEntity {

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "dd-mm-yyyy")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PetType type;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_id")
    @OrderBy("visit_date ASC")
    private Set<Visit> visits = new LinkedHashSet<>();


    public void addVisit(Visit visit){
        getVisits().add(visit);
    }
}
