package com.example.demo.mapper;

import com.example.demo.domain.Manager;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ManagerMapper {

  @Select("SELECT * FROM manager WHERE id = #{id}")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "firstName", column = "first_name"),
      @Result(property = "middleName", column = "middle_name"),
      @Result(property = "lastName", column = "last_name"),
      @Result(property = "alternate", column = "alternate_id", one = @One(select = "findById"))
  })
  Manager findById(@Param("id") int id);

  @Select("SELECT * FROM manager")
  @Results(value = {
      @Result(property = "id", column = "id"),
      @Result(property = "firstName", column = "first_name"),
      @Result(property = "middleName", column = "middle_name"),
      @Result(property = "lastName", column = "last_name"),
      @Result(property = "alternate", column = "alternate_id", one = @One(select = "findById"))
  })
  List<Manager> findAll();

  @Insert("INSERT INTO manager(first_name, middle_name, last_name, phone, alternate_id) VALUES (#{firstName}, #{middleName}, #{lastName}, #{phone}, #{alternate.id})")
  int save(Manager manager);

  @Update("UPDATE manager SET first_name = #{firstName}, middle_name = #{middleName}, last_name = #{lastName}, phone = #{phone}, alternate_id = #{alternate.id} WHERE id = #{id}")
  int update(Manager manager);

  @Select("SELECT COUNT(*) FROM manager")
  int count();

  @Delete("DELETE FROM manager WHERE id = #{id}")
  int delete(int id);

}
