package com.example.demo.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.domain.Client;
import com.example.demo.domain.Manager;
import com.example.demo.mapper.ClientMapper;
import com.example.demo.mapper.ManagerMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ManagerServiceTest {

  @Mock
  private ClientMapper clientMapper;

  @Mock
  private ManagerMapper managerMapper;

  @InjectMocks
  private ManagerService managerService = new ManagerService();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void findById() {
    // setup
    Manager manager = new Manager();
    int id = 1;
    manager.setId(id);

    when(managerMapper.findById(id)).thenReturn(manager);

    // act
    Manager result = managerService.findById(id);

    // verify
    assertThat(result, is(manager));
  }

  @Test
  public void findAll() {
    // setup
    Manager manager1 = new Manager();
    manager1.setId(1);
    Manager manager2 = new Manager();
    manager2.setId(2);

    when(managerMapper.findAll()).thenReturn(Arrays.asList(manager1, manager2));

    // act
    List<Manager> result = managerService.findAll();

    // verify
    assertThat(result, containsInAnyOrder(manager1, manager2));
  }

  @Test
  public void save() {
    // setup
    Manager manager = new Manager();
    manager.setId(1);

    // act
    managerService.save(manager);

    // verify
    verify(managerMapper).save(manager);
  }

  @Test
  public void update() {
    // setup
    Manager manager = new Manager();
    manager.setId(11);
    manager.setLastName("nLastName");

    // act
    managerService.update(manager);

    // verify
    verify(managerMapper).update(manager);
  }

  @Test
  public void delete_whenManagerHasNoClients() {
    // setup
    int managerId = 1;
    when(clientMapper.findClientsForManager(managerId)).thenReturn(Collections.emptyList());

    // act
    managerService.delete(managerId);

    // verify
    verify(managerMapper).delete(managerId);
  }

  @Test
  public void delete_whenManagerHasClients_setAlternateToManagerClientsAndUpdate_thenDeleteManager() {
    // setup
    int managerId = 1;
    Manager manager = new Manager();
    manager.setId(managerId);

    Manager alternate = new Manager();
    alternate.setId(33);
    manager.setAlternate(alternate);

    Client client1 = new Client();
    client1.setId(1);
    client1.setManager(manager);

    Client client2 = new Client();
    client2.setId(2);
    client2.setManager(manager);

    when(clientMapper.findClientsForManager(managerId)).thenReturn(Arrays.asList(client1, client2));

    // act
    managerService.delete(managerId);

    // verify
    assertThat(client1.getManager().getId(), is(alternate.getId()));
    assertThat(client2.getManager().getId(), is(alternate.getId()));

    InOrder inOrder = inOrder(clientMapper, managerMapper);
    inOrder.verify(clientMapper).update(client1);
    inOrder.verify(clientMapper).update(client2);
    inOrder.verify(managerMapper).delete(managerId);
    inOrder.verifyNoMoreInteractions();
  }
}
