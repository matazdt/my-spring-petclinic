package com.desnistamz.myspringpetclinic.owner;

import com.desnistamz.myspringpetclinic.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "visits")
public class Visit extends BaseEntity {

    @Column(name = "visit_date")
    @DateTimeFormat(pattern = "dd-mm-yyyy")
    private LocalDate date;

    @NotEmpty
    private String description;

    public Visit(){
        this.date = LocalDate.now();
    }
}
