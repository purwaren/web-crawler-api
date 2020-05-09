package id.corei.crawler.model.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface BaseMapper<T, PK, E> {
    public int countByExample(E example);

    public int deleteByExample(E example);

    public int deleteByPrimaryKey(PK id);

    public int insert(T record);

    public int insertSelective(T record);

    public List<T> selectByExample(E example);

    public List<T> selectByExampleWithBLOBs(E example);

    public List<T> selectByExample(E example, RowBounds rowBounds);

    public List<T> selectByExampleWithBLOBs(E example, RowBounds rowBounds);

    public T selectByPrimaryKey(PK id);

    public int updateByExampleSelective(@Param("record") T record, @Param("example") E example);

    public int updateByExample(@Param("record") T record, @Param("example") E example);

    public int updateByExampleWithBLOBs(@Param("record") T record, @Param("example") E example);

    public int updateByPrimaryKeySelective(T record);

    public int updateByPrimaryKey(T record);

    public int updateByPrimaryKeyWithBLOBs(T record);
}
