package com.example.demo.controller;

import static com.example.demo.TestUtils.asJsonString;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.domain.Manager;
import com.example.demo.mapper.ClientMapper;
import com.example.demo.mapper.ManagerMapper;
import com.example.demo.service.ManagerService;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ManagerController.class)
public class ManagerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ManagerService managerService;

  @MockBean
  private ClientMapper clientMapper;

  @MockBean
  private ManagerMapper managerMapper;

  @Test
  public void getOneManager_ok() throws Exception {
    // setup
    Manager manager = new Manager();
    manager.setId(1);
    manager.setPhone("111");
    manager.setFirstName("fN");
    manager.setMiddleName("mN");
    manager.setLastName("lN");

    Manager alternate = new Manager();
    alternate.setId(2);
    alternate.setPhone("222");
    alternate.setFirstName("fNa");
    alternate.setMiddleName("mNa");
    alternate.setLastName("lNa");

    manager.setAlternate(alternate);

    when(managerService.findById(1)).thenReturn(manager);

    // act & verify
    mockMvc.perform(get("/manager/one/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString(
                "{\"id\":1,\"firstName\":\"fN\",\"middleName\":\"mN\",\"lastName\":\"lN\",\"phone\":\"111\",\"alternate\":{\"id\":2,\"firstName\":\"fNa\",\"middleName\":\"mNa\",\"lastName\":\"lNa\",\"phone\":\"222\",\"alternate\":null}}")))
        .andExpect(status().isOk());
  }

  @Test
  public void getOneManager_notFound() throws Exception {
    // setup

    // act & verify
    mockMvc.perform(get("/manager/one/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString(
                "Could not find manager with id 1")))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getAllManagers_ok() throws Exception {
    // setup
    Manager manager1 = new Manager();
    manager1.setId(1);
    manager1.setPhone("111");
    manager1.setFirstName("fN1");
    manager1.setMiddleName("mN1");
    manager1.setLastName("lN1");

    Manager manager2 = new Manager();
    manager2.setId(2);
    manager2.setPhone("222");
    manager2.setFirstName("fN2");
    manager2.setMiddleName("mN2");
    manager2.setLastName("lN2");

    Manager alternate = new Manager();
    alternate.setId(3);
    alternate.setPhone("333");
    alternate.setFirstName("fNa");
    alternate.setMiddleName("mNa");
    alternate.setLastName("lNa");

    manager1.setAlternate(alternate);

    when(managerService.findAll()).thenReturn(Arrays.asList(manager1, manager2, alternate));

    // act & verify
    mockMvc.perform(get("/manager/all")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString(
                "[{\"firstName\":\"fN1\",\"lastName\":\"lN1\",\"phone\":\"111\",\"alternatePhone\":\"333\",\"alternateFullName\":\"fNa mNa lNa\",\"middleName\":\"mN1\",\"id\":1},{\"firstName\":\"fN2\",\"lastName\":\"lN2\",\"phone\":\"222\",\"middleName\":\"mN2\",\"id\":2},{\"firstName\":\"fNa\",\"lastName\":\"lNa\",\"phone\":\"333\",\"middleName\":\"mNa\",\"id\":3}]")))
        .andExpect(status().isOk());
  }

  @Test
  public void update_ok() throws Exception {
    // setup
    Manager manager = new Manager();
    manager.setId(1);
    manager.setPhone("111");
    manager.setFirstName("fN");
    manager.setMiddleName("mN");
    manager.setLastName("lN");

    Manager alternate = new Manager();
    alternate.setId(33);
    alternate.setPhone("333");
    alternate.setFirstName("fNa");
    alternate.setMiddleName("mNa");
    alternate.setLastName("lNa");

    manager.setAlternate(alternate);

    when(managerService.findById(1)).thenReturn(manager);
    when(managerService.findById(33)).thenReturn(alternate);

    // act & verify
    mockMvc.perform(put("/manager/update/1")
        .content(asJsonString(manager))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(managerService).update(manager);
  }

  @Test
  public void update_managerNotFound() throws Exception {
    // setup
    Manager manager = new Manager();
    manager.setId(1);
    manager.setPhone("111");
    manager.setFirstName("fN");
    manager.setMiddleName("mN");
    manager.setLastName("lN");

    when(managerService.findById(1)).thenReturn(null);

    // act & verify
    mockMvc.perform(put("/manager/update/1")
        .content(asJsonString(manager))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString("Could not find manager with id 1")))
        .andExpect(status().isNotFound());

    verify(managerService, never()).update(manager);
  }

  @Test
  public void update_alternateNotFound() throws Exception {
    // setup
    Manager manager = new Manager();
    manager.setId(1);
    manager.setPhone("111");
    manager.setFirstName("fN");
    manager.setMiddleName("mN");
    manager.setLastName("lN");

    Manager alternate = new Manager();
    alternate.setId(33);
    alternate.setPhone("333");
    alternate.setFirstName("fNa");
    alternate.setMiddleName("mNa");
    alternate.setLastName("lNa");

    manager.setAlternate(alternate);

    when(managerService.findById(1)).thenReturn(manager);
    when(managerService.findById(33)).thenReturn(null);

    // act & verify
    mockMvc.perform(put("/manager/update/1")
        .content(asJsonString(manager))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString("Could not find manager with id 33")))
        .andExpect(status().isNotFound());

    verify(managerService, never()).update(manager);
  }

  @Test
  public void save() throws Exception {
    // setup
    Manager manager = new Manager();
    manager.setId(1);
    manager.setPhone("111");
    manager.setFirstName("fN");
    manager.setMiddleName("mN");
    manager.setLastName("lN");

    Manager alternate = new Manager();
    alternate.setId(33);
    alternate.setPhone("333");
    alternate.setFirstName("fNa");
    alternate.setMiddleName("mNa");
    alternate.setLastName("lNa");

    manager.setAlternate(alternate);

    // act & verify
    mockMvc.perform(post("/manager/save")
        .content(asJsonString(manager))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    verify(managerService).save(manager);
  }

  @Test
  public void delete_notFound() throws Exception {
    // setup
    Manager manager = new Manager();
    manager.setId(1);
    manager.setPhone("111");
    manager.setFirstName("fN");
    manager.setMiddleName("mN");
    manager.setLastName("lN");

    when(managerService.findById(1)).thenReturn(null);

    // act & verify
    mockMvc.perform(delete("/manager/delete/1")
        .content(asJsonString(manager))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString("Could not find manager with id 1")))
        .andExpect(status().isNotFound());

    verify(managerService, never()).delete(anyInt());
  }

  @Test
  public void delete_ok() throws Exception {
    // setup
    Manager manager = new Manager();
    manager.setId(1);
    manager.setPhone("111");
    manager.setFirstName("fN");
    manager.setMiddleName("mN");
    manager.setLastName("lN");

    when(managerService.findById(1)).thenReturn(manager);

    // act & verify
    mockMvc.perform(delete("/manager/delete/1")
        .content(asJsonString(manager))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(managerService).delete(1);
  }

}
