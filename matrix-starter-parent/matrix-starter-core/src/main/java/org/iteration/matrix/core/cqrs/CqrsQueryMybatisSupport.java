package org.iteration.matrix.core.cqrs;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.iteration.matrix.core.restful.DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CqrsQueryMybatisSupport implements CqrsQuery {

    @Autowired
    SqlSession sqlSession;

    @Override
    public <T extends DTO> Page<T>  selectPage(String statement, Object parameter, Pageable pageable) {
        PageRowBounds pageRowBounds = new PageRowBounds(pageable.getPageNumber(), pageable.getPageSize());
        List<T> queryResultList = sqlSession.selectList(statement, parameter, pageRowBounds);
        PageInfo<T> pageInfo = new PageInfo(queryResultList);
        return new PageImpl<T>(pageInfo.getList(), pageable, pageInfo.getTotal());
    }

    @Override
    public <T extends DTO> List<T> selectList(String statement, Object parameter) {
        List<T> queryResultList = sqlSession.selectList(statement, parameter);
        return queryResultList;
    }

    @Override
    public <T extends DTO> T selectOne(String statement, Object parameter) {
        T result = sqlSession.selectOne(statement, parameter);
        return result;
    }

}
