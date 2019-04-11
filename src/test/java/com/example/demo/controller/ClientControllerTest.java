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

import com.example.demo.controller.ClientController;
import com.example.demo.domain.Client;
import com.example.demo.domain.Manager;
import com.example.demo.mapper.ClientMapper;
import com.example.demo.mapper.ManagerMapper;
import com.example.demo.service.ClientService;
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
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ClientService clientService;

  @MockBean
  private ManagerService managerService;

  @MockBean
  private ClientMapper clientMapper;

  @MockBean
  private ManagerMapper managerMapper;

  @Test
  public void getOneClient_ok() throws Exception {
    // setup
    Client client = new Client();
    client.setId(1);
    client.setName("Name");
    client.setAddress("Address");

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

    client.setManager(manager);

    when(clientService.findById(1)).thenReturn(client);

    // act & verify
    mockMvc.perform(get("/client/one/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString(
                "{\"id\":1,\"name\":\"Name\",\"address\":\"Address\",\"manager\":{\"id\":1,\"firstName\":\"fN\",\"middleName\":\"mN\",\"lastName\":\"lN\",\"phone\":\"111\",\"alternate\":{\"id\":2,\"firstName\":\"fNa\",\"middleName\":\"mNa\",\"lastName\":\"lNa\",\"phone\":\"222\",\"alternate\":null}}")))
        .andExpect(status().isOk());
  }

  @Test
  public void getOneClient_notFound() throws Exception {
    // setup

    // act & verify
    mockMvc.perform(get("/client/one/333")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString("Could not find client with id 333")))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getAllClients_ok() throws Exception {
    // setup
    Client client1 = new Client();
    client1.setId(1);
    client1.setName("Name1");
    client1.setAddress("Address1");

    Client client2 = new Client();
    client2.setId(2);
    client2.setName("Name2");
    client2.setAddress("Address2");

    Client client3 = new Client();
    client3.setId(3);
    client3.setName("Name3");
    client3.setAddress("Address3");
    client3.setDeleted(true);

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

    client1.setManager(manager);
    client2.setManager(alternate);
    client3.setManager(manager);

    when(clientService.findAll()).thenReturn(Arrays.asList(client1, client2));

    // act & verify
    mockMvc.perform(get("/client/all")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString(
                "[{\"managerFullName\":\"fN mN lN\",\"address\":\"Address1\",\"managerPhone\":\"111\",\"name\":\"Name1\",\"id\":1,\"managerAlternatePhone\":\"222\"},{\"managerFullName\":\"fNa mNa lNa\",\"address\":\"Address2\",\"managerPhone\":\"222\",\"name\":\"Name2\",\"id\":2}]")))
        .andExpect(status().isOk());
  }

  @Test
  public void save() throws Exception {
    // setup
    Client client = new Client();
    client.setId(1);
    client.setName("Name");
    client.setAddress("Address");

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

    client.setManager(manager);

    // act & verify
    mockMvc.perform(post("/client/save")
        .content(asJsonString(client))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    verify(clientService).save(client);
  }

  @Test
  public void update_ok() throws Exception {
    // setup
    Client client = new Client();
    client.setId(1);
    client.setName("Name");
    client.setAddress("Address");

    when(clientService.findById(1)).thenReturn(client);

    // act & verify
    mockMvc.perform(put("/client/update/1")
        .content(asJsonString(client))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(clientService).update(client);
  }

  @Test
  public void update_notFound() throws Exception {
    // setup
    Client client = new Client();
    client.setId(1);
    client.setName("Name");
    client.setAddress("Address");

    when(clientService.findById(1)).thenReturn(null);

    // act & verify
    mockMvc.perform(put("/client/update/1")
        .content(asJsonString(client))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString("Could not find client with id 1")))
        .andExpect(status().isNotFound());

    verify(clientService, never()).update(client);
  }

  @Test
  public void delete_notFound() throws Exception {
    // setup
    Client client = new Client();
    client.setId(1);
    client.setName("Name");
    client.setAddress("Address");

    when(clientService.findById(1)).thenReturn(null);

    // act & verify
    mockMvc.perform(delete("/client/delete/1")
        .content(asJsonString(client))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString("Could not find client with id 1")))
        .andExpect(status().isNotFound());

    verify(clientService, never()).delete(anyInt());
  }

  @Test
  public void delete_ok() throws Exception {
    // setup
    Client client = new Client();
    client.setId(1);
    client.setName("Name");
    client.setAddress("Address");

    when(clientService.findById(1)).thenReturn(client);

    // act & verify
    mockMvc.perform(delete("/client/delete/1")
        .content(asJsonString(client))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(clientService).delete(1);
  }

  @Test
  public void clientsForManager_ok() throws Exception {
    // setup
    Client client1 = new Client();
    client1.setId(1);
    client1.setName("Name1");
    client1.setAddress("Address1");

    Client client2 = new Client();
    client2.setId(2);
    client2.setName("Name2");
    client2.setAddress("Address2");

    Client client3 = new Client();
    client3.setId(3);
    client3.setName("Name3");
    client3.setAddress("Address3");

    Manager manager = new Manager();
    manager.setId(1);
    manager.setPhone("111");
    manager.setFirstName("fN");
    manager.setMiddleName("mN");
    manager.setLastName("lN");

    client1.setManager(manager);
    client2.setManager(manager);

    when(managerService.findById(1)).thenReturn(manager);
    when(clientService.findClientsForManager(1)).thenReturn(Arrays.asList(client1, client2));

    // act & verify
    mockMvc.perform(get("/client/clientsForManager/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString(
                "[{\"name\":\"Name1\",\"id\":1},{\"name\":\"Name2\",\"id\":2}]")))
        .andExpect(status().isOk());
  }

  @Test
  public void clientsForManager_managerNotFound() throws Exception {
    // setup
    when(managerService.findById(1)).thenReturn(null);

    // act & verify
    mockMvc.perform(get("/client/clientsForManager/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().string(
            containsString(
                "Could not find manager with id 1")))
        .andExpect(status().isNotFound());
  }

}
