package com.example.demo.controller;

import com.example.demo.domain.Manager;
import com.example.demo.exception.ManagerNotFoundException;
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
@RequestMapping("/manager")
public class ManagerController {

  @Autowired
  private ManagerService managerService;

  @GetMapping("/one/{id}")
  public Manager one(@PathVariable("id") int id) {
    Manager manager = managerService.findById(id);

    if (manager == null) {
      throw new ManagerNotFoundException(id);
    }

    return manager;
  }

  @GetMapping("/all")
  public List<Map<String, Object>> all() {
    List<Map<String, Object>> result = new ArrayList<>();

    List<Manager> managers = managerService.findAll();

    managers.forEach(m -> {
      Map<String, Object> managersMap = new HashMap<>();
      managersMap.put("id", m.getId());
      managersMap.put("firstName", m.getFirstName());
      managersMap.put("middleName", m.getMiddleName());
      managersMap.put("lastName", m.getLastName());
      managersMap.put("phone", m.getPhone());

      Manager alternate = m.getAlternate();
      if (alternate != null) {
        managersMap.put("alternateFullName", alternate.getFullName());
        managersMap.put("alternatePhone", alternate.getPhone());
      }

      result.add(managersMap);
    });

    return result;
  }

  @PostMapping("/save")
  public void save(@RequestBody Manager manager, HttpServletResponse response) {
    managerService.save(manager);
    response.setStatus(HttpServletResponse.SC_CREATED);
  }

  @PutMapping("/update/{id}")
  public void update(@RequestBody Manager manager, @PathVariable int id) {
    Manager managerToUpdate = managerService.findById(id);

    if (managerToUpdate == null) {
      throw new ManagerNotFoundException(id);
    }

    Manager alternate = manager.getAlternate();
    if (alternate != null) {
      if (managerService.findById(alternate.getId()) == null) {
        throw new ManagerNotFoundException(alternate.getId());
      }
    }

    managerToUpdate.setFirstName(manager.getFirstName());
    managerToUpdate.setMiddleName(manager.getMiddleName());
    managerToUpdate.setLastName(manager.getLastName());
    managerToUpdate.setPhone(manager.getPhone());
    managerToUpdate.setAlternate(alternate);

    managerService.update(managerToUpdate);
  }

  @DeleteMapping("/delete/{id}")
  public void delete(@PathVariable int id) {
    Manager manager = managerService.findById(id);
    if (manager == null) {
      throw new ManagerNotFoundException(id);
    }

    managerService.delete(id);
  }

}
