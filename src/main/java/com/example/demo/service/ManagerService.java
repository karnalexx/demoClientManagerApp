package com.example.demo.service;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

import com.example.demo.domain.Client;
import com.example.demo.domain.Manager;
import com.example.demo.mapper.ClientMapper;
import com.example.demo.mapper.ManagerMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ManagerService {

  @Autowired
  private ManagerMapper managerMapper;

  @Autowired
  private ClientMapper clientMapper;

  public Manager findById(int id) {
    LOGGER.info("ManagerService#findById(" + id + ")");
    return managerMapper.findById(id);
  }

  public List<Manager> findAll() {
    LOGGER.info("ManagerService#findAll()");
    return managerMapper.findAll();
  }

  public void save(Manager manager) {
    LOGGER.info("ManagerService#save manager with id: " + manager.getId());
    managerMapper.save(manager);
  }

  public void update(Manager manager) {
    LOGGER.info("ManagerService#update manager with id: " + manager.getId());
    managerMapper.update(manager);
  }

  public void delete(int id) {
    LOGGER.info("ManagerService#delete manager with id: " + id);

    List<Client> clients = clientMapper.findClientsForManager(id);

    clients.forEach(c -> {
      Manager alternate = c.getManager().getAlternate();
      if (alternate != null) {
        c.setManager(alternate);
        clientMapper.update(c);
      }
    });

    managerMapper.delete(id);
  }

}

