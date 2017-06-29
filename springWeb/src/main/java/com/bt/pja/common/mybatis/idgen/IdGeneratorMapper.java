package com.bt.pja.common.mybatis.idgen;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by chenjiapeng on 2015/6/24 0024.
 */
public interface IdGeneratorMapper {

    @Select("select name from ${tableName}")
    List<String> selectAllNames(@Param("tableName") String tableName);

    @Select("select current, step from ${tableName} where name = #{name} FOR UPDATE")
    IdGenInfo selectByName(@Param("tableName") String tableName, @Param("name") String name);

    @Update("update ${tableName} set current = current + step where name = #{name}")
    boolean updateIdByName(@Param("tableName") String tableName, @Param("name") String name);
}
