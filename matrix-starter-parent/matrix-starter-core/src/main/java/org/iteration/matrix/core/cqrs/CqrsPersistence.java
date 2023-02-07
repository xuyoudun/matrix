package org.iteration.matrix.core.cqrs;

import org.iteration.matrix.core.persistent.Auditable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CqrsPersistence<T extends Auditable<ID>, ID extends Serializable> extends Repository<T, ID> {

    // Save
    <S extends T> S save(S entity);

    <S extends T> S saveAndFlush(S entity);

    void flush();

    <S extends T> List<S> saveAll(Iterable<S> entities);

    // Crud
    T getOne(ID id);

    Optional<T> findById(ID id);

    List<T> findAllById(Iterable<ID> ids);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    List<T> findAll(Sort sort);

    boolean existsById(ID id);

    long count();

    // Example
    <S extends T> Optional<S> findOne(Example<S> example);

    <S extends T> List<S> findAll(Example<S> example);

    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    <S extends T> boolean exists(Example<S> example);

    <S extends T> long count(Example<S> example);

    // Specification
    Optional<T> findOne(@Nullable Specification<T> spec);

    List<T> findAll(@Nullable Specification<T> spec);

    List<T> findAll(@Nullable Specification<T> spec, Sort sort);

    Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable);

    long count(@Nullable Specification<T> spec);

    // Delete
    void delete(T entity);

    void deleteAll();

    void deleteAll(Iterable<? extends T> entities);

    void deleteAllById(Iterable<? extends ID> ids);

    void deleteAllByIdInBatch(Iterable<ID> ids);

    void deleteAllInBatch();

    void deleteAllInBatch(Iterable<T> entities);

    void deleteById(ID id);

}
