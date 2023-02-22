package com.desnistamz.myspringpetclinic.vet;

import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

import static org.assertj.core.api.Assertions.assertThat;

class VetTests {

    @Test
    void testSerialization(){
        Vet vet = new Vet();
        vet.setFirst_name("Zaphod");
        vet.setLast_name("Beeblebrox");
        vet.setId(123);
        Vet other = (Vet) SerializationUtils.deserialize(SerializationUtils.serialize(vet));
        assertThat(other.getFirst_name()).isEqualTo(vet.getFirst_name());
        assertThat(other.getLast_name()).isEqualTo(vet.getLast_name());
        assertThat(other.getId()).isEqualTo(vet.getId());
    }
}
