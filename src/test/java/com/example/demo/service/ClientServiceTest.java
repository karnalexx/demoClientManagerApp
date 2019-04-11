package com.example.demo.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.domain.Client;
import com.example.demo.domain.Manager;
import com.example.demo.mapper.ClientMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ClientServiceTest {

  @Mock
  private ClientMapper clientMapper;

  @InjectMocks
  private ClientService clientService = new ClientService();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void findById() {
    // setup
    Client client = new Client();
    int id = 1;
    client.setId(id);
    when(clientMapper.findById(id)).thenReturn(client);

    // act
    Client result = clientService.findById(id);

    // verify
    assertThat(result.getId(), is(client.getId()));
  }

  @Test
  public void findAll() {
    // setup
    Client client1 = new Client();
    client1.setId(1);

    Client client2 = new Client();
    client2.setId(2);

    when(clientMapper.findAll()).thenReturn(Arrays.asList(client1, client2));

    // act
    List<Client> result = clientService.findAll();

    // verify
    assertThat(result, containsInAnyOrder(client1, client2));
  }

  @Test
  public void findClientsForManager() {
    // setup
    Manager manager = new Manager();
    int managerId = 1;
    manager.setId(managerId);

    Client client1 = new Client();
    client1.setId(11);

    Client client2 = new Client();
    client2.setId(12);

    when(clientMapper.findClientsForManager(managerId)).thenReturn(Arrays.asList(client1, client2));

    // act
    List<Client> result = clientService.findClientsForManager(managerId);

    // verify
    assertThat(result, containsInAnyOrder(client1, client2));
  }

  @Test
  public void save() {
    // setup
    Client client = new Client();
    client.setId(1);

    // act
    clientService.save(client);

    // verify
    verify(clientMapper).save(client);
  }

  @Test
  public void update() {
    // setup
    Client client = new Client();
    client.setId(1);

    // act
    clientService.update(client);

    // verify
    verify(clientMapper).update(client);
  }

  @Test
  public void delete() {
    // setup

    // act
    clientService.delete(1);

    // verify
    verify(clientMapper).delete(1);
  }

}