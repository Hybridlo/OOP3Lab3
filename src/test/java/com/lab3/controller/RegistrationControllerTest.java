package com.lab3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab3.Application;
import com.lab3.dto.UserDTO;
import com.lab3.repository.DataRepository;
import com.lab3.repository.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
@Sql("/test-data.sql")
@DirtiesContext
public class RegistrationControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataRepository dataRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testRegistration_UserCreated() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("Ivan");
        userDTO.setPassword("password");
        userDTO.setType("patient");

        String url = "/registration";

        String json = mapper.writeValueAsString(userDTO);

        MockHttpServletRequestBuilder msb = post(url)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultRequest(get("/"))
                .build();

        ResultActions resultActions = mvc.perform(msb);

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void testRegistration_LoginAlreadyExist() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("User1");
        userDTO.setPassword("password");
        userDTO.setType("patient");

        String url = "/registration";

        String json = mapper.writeValueAsString(userDTO);

        MockHttpServletRequestBuilder msb = post(url)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultRequest(get("/"))
                .build();

        ResultActions resultActions = mvc.perform(msb);

        resultActions.andExpect(status().isBadRequest());
    }

    @After
    @Transactional
    public void clear(){
        dataRepository.deleteAll();
        userRepository.deleteAll();
    }
}
