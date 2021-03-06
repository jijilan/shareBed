package com.sharebedapp.jijl.mapper.base;


import java.util.List;

/**
 * @Author jijl
 * @Descrition:
 * @Date: Create in ${Date}
 **/
public interface BaseMapper<T,PK> {
    int deleteByPrimaryKey(PK key);

    int insert(T record);

    int insertSelective(T record);

    List<T> selectByExample(T record);

    T selectByPrimaryKey(PK key);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
