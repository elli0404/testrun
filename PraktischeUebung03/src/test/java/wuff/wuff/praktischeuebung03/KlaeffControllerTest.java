package wuff.wuff.praktischeuebung03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@WebMvcTest
class KlaeffControllerTest {

    @Autowired
    MockMvc mvc;


    @Test
    @DisplayName("GET / zeigt homepage mit OK")
    void getHome() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    @DisplayName("POST auf / hat Attribute username und text")
    void sendHome() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/")
                .param("username", "Peter")
                .param("text", "Hallo ich bin Peter"))
                .andExpect(model().attribute("username", "Peter"))
                .andExpect(model().attribute("text", "Hallo ich bin Peter"));
    }


    @Test
    @DisplayName("bei GET / wird username automatisch im Formular hinzugefügt")
    void usernameImFormular() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/")
                .param("username", "Peter")
                .param("text", "Hallo ich bin Peter"));

        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(model().attribute("username", "Peter"));
    }
}