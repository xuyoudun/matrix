package org.iteration.matrix.core.cqrs;

import org.iteration.matrix.core.restful.DTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CqrsQuery {

    <T extends DTO> Page<T> selectPage(String statement, Object parameter, Pageable pageable);

    <T extends DTO> List<T> selectList(String statement, Object parameter);

    <T extends DTO> T selectOne(String statement, Object parameter);
}
