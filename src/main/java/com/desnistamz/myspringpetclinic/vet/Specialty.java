package com.desnistamz.myspringpetclinic.vet;

import com.desnistamz.myspringpetclinic.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "specialties")
public class Specialty extends NamedEntity {
}
