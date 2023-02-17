package com.desnistamz.myspringpetclinic.owner;

import com.desnistamz.myspringpetclinic.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name ="types")
public class PetType extends NamedEntity {
}
