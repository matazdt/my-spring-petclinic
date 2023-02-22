package com.desnistamz.myspringpetclinic.system;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Disabled
@WebMvcTest(controllers = CrashController.class)
public class CrashControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testTriggerException() throws  Exception{
        mockMvc.perform(get("/oups")).andExpect(view().name("exception"))
                .andExpect(model().attributeExists("exception")).andExpect(forwardedUrl("exception"))
                .andExpect(status().isOk());
    }
}
