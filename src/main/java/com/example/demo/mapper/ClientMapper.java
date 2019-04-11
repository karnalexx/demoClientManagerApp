package com.example.demo.mapper;

import com.example.demo.domain.Client;
import com.example.demo.domain.Manager;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ClientMapper {

  @Select("SELECT * FROM client WHERE id = #{id}")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "address", column = "address"),
      @Result(property = "manager", column = "manager_id", one = @One(select = "findManagerById"))
  })
  Client findById(@Param("id") int id);

  @Select("SELECT * FROM manager WHERE id = #{id}")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "firstName", column = "first_name"),
      @Result(property = "middleName", column = "middle_name"),
      @Result(property = "lastName", column = "last_name"),
      @Result(property = "alternate", column = "alternate_id", one = @One(select = "findManagerById"))
  })
  Manager findManagerById(@Param("id") int id);

  @Select("SELECT * FROM client WHERE deleted = 'FALSE'")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "address", column = "address"),
      @Result(property = "manager", column = "manager_id", one = @One(select = "findManagerById"))
  })
  List<Client> findAll();

  @Select("SELECT * FROM client WHERE deleted = 'FALSE' AND manager_id = #{managerId}")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"),
      @Result(property = "address", column = "address"),
      @Result(property = "manager", column = "manager_id", one = @One(select = "findManagerById"))
  })
  List<Client> findClientsForManager(int managerId);

  @Insert("INSERT INTO client(name, address, manager_id) VALUES (#{name}, #{address}, #{manager.id})")
  int save(Client client);

  @Select("SELECT COUNT(*) FROM client")
  int count();

  @Update("UPDATE client SET name = #{name}, address = #{address}, manager_id = #{manager.id} WHERE id = #{id}")
  void update(Client client);

  @Delete("UPDATE client SET deleted = true WHERE id = #{id}")
  int delete(int id);
}
