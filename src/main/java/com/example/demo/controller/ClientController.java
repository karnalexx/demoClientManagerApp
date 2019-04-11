package com.example.demo.controller;

import com.example.demo.domain.Client;
import com.example.demo.domain.Manager;
import com.example.demo.exception.ClientNotFoundException;
import com.example.demo.exception.ManagerNotFoundException;
import com.example.demo.service.ClientService;
import com.example.demo.service.ManagerService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

  @Autowired
  private ClientService clientService;

  @Autowired
  private ManagerService managerService;

  @GetMapping("/one/{id}")
  public Client one(@PathVariable("id") int id) {
    Client client = clientService.findById(id);

    if (client == null) {
      throw new ClientNotFoundException(id);
    }

    return client;
  }

  @GetMapping("/all")
  public List<Map<String, Object>> all() {
    List<Client> clients = clientService.findAll();

    List<Map<String, Object>> result = new ArrayList<>();

    clients.forEach(c -> {
      Map<String, Object> clientsMap = new HashMap<>();
      clientsMap.put("id", c.getId());
      clientsMap.put("name", c.getName());
      clientsMap.put("address", c.getAddress());
      Manager manager = c.getManager();
      clientsMap.put("managerFullName", manager.getFullName());
      clientsMap.put("managerPhone", manager.getPhone());
      if (manager.getAlternate() != null) {
        clientsMap.put("managerAlternatePhone", manager.getAlternate().getPhone());
      }

      result.add(clientsMap);
    });

    return result;
  }

  @PostMapping("/save")
  public void save(@RequestBody Client client, HttpServletResponse response) {
    clientService.save(client);
    response.setStatus(HttpServletResponse.SC_CREATED);
  }

  @PutMapping("/update/{id}")
  public void update(@RequestBody Client client, @PathVariable int id) {
    Client clientToUpdate = clientService.findById(id);

    if (clientToUpdate == null) {
      throw new ClientNotFoundException(id);
    }

    clientToUpdate.setName(client.getName());
    clientToUpdate.setAddress(client.getAddress());
    clientToUpdate.setManager(client.getManager());

    clientService.update(clientToUpdate);
  }

  @DeleteMapping("/delete/{id}")
  public void delete(@PathVariable int id) {
    Client client = clientService.findById(id);

    if (client == null) {
      throw new ClientNotFoundException(id);
    }

    clientService.delete(id);
  }

  @GetMapping("/clientsForManager/{managerId}")
  public List<Map<String, Object>> clientsForManager(@PathVariable int managerId) {
    Manager manager = managerService.findById(managerId);
    if (manager == null) {
      throw new ManagerNotFoundException(managerId);
    }

    List<Client> clients = clientService.findClientsForManager(managerId);

    List<Map<String, Object>> result = new ArrayList<>();

    clients.forEach(c -> {
      Map<String, Object> clientsMap = new HashMap<>();
      clientsMap.put("id", c.getId());
      clientsMap.put("name", c.getName());
      result.add(clientsMap);
    });

    return result;
  }
}
