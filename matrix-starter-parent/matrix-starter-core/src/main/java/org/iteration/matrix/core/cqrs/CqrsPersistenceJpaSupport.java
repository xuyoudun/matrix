package org.iteration.matrix.core.cqrs;

import org.iteration.matrix.core.persistent.AuditPO;
import org.iteration.matrix.core.persistent.Auditable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Path;
import java.io.Serializable;

/**
 * @param <T>
 * @param <ID>
 * @author udun.xu@hotmail.com
 */
public class CqrsPersistenceJpaSupport<T extends Auditable<ID>, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CqrsPersistence<T, ID> {

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;
    private final PersistenceProvider provider;

    public CqrsPersistenceJpaSupport(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
        this.provider = PersistenceProvider.fromEntityManager(entityManager);
    }

    protected <S extends T> TypedQuery<S> getQuery(@Nullable Specification<S> spec, Class<S> domainClass, Sort sort) {
        Specification enabledSpec = (root, query, criteriaBuilder) -> {
            Path enabled = root.get("enabled");
            return criteriaBuilder.equal(enabled, "Y");
        };

        return super.getQuery(spec.and(enabledSpec), domainClass, sort);
    }

    protected <S extends T> TypedQuery<Long> getCountQuery(@Nullable Specification<S> spec, Class<S> domainClass) {
        Specification enabledSpec = (root, query, criteriaBuilder) -> {
            Path enabled = root.get("enabled");
            return criteriaBuilder.equal(enabled, "Y");
        };
        return super.getCountQuery(spec.and(enabledSpec), domainClass);
    }

    public void delete(T entity) {
        Assert.notNull(entity, "Entity must not be null!");
        if (entityInformation.isNew(entity)) {
            return;
        }
        if (entity.getClass() == AuditPO.class) {
            AuditPO po = (AuditPO) entity;
            po.setEnabled("N");
            this.save(entity);
        } else {
            super.delete(entity);
        }

    }

}
