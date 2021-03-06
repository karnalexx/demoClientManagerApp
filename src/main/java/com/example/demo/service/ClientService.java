package com.example.demo.service;


import com.example.demo.domain.Client;
import com.example.demo.mapper.ClientMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientService {

  @Autowired
  private ClientMapper clientMapper;

  public Client findById(int id) {
    log.info("ClientService#findById(" + id + ")");
    return clientMapper.findById(id);
  }

  public List<Client> findAll() {
    log.info("findAll()");
    return clientMapper.findAll();
  }

  public List<Client> findClientsForManager(int managerId) {
    log.info("ClientService#findClientsForManager(" + managerId + ")");
    return clientMapper.findClientsForManager(managerId);
  }

  public void save(Client client) {
    log.info("ClientService#save client with id: " + client.getId());
    clientMapper.save(client);
  }

  public void update(Client client) {
    log.info("ClientService#update client with id: " + client.getId());
    clientMapper.update(client);
  }

  public void delete(int id) {
    log.info("ClientService#delete client with id: " + id);
    clientMapper.delete(id);
  }
}
