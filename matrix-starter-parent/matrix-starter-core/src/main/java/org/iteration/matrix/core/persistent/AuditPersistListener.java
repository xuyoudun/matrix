package org.iteration.matrix.core.persistent;

import lombok.extern.slf4j.Slf4j;
import org.iteration.matrix.core.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Date;

@Slf4j
public class AuditPersistListener {
    private static final Logger logger = LoggerFactory.getLogger(AuditPersistListener.class);

    @PrePersist
    public void onPrePersist(Object object) {
        logger.trace("onPrePersist >>> [{}]", object);
        updateTracingInformation(object);
    }

    @PreUpdate
    public void onPreUpdate(Object object) {
        logger.trace("onPreUpdate >>> [{}]", object);
        updateTracingInformation(object);
    }

    @PreRemove
    public void onPreRemove(Object object) {
        logger.trace("onPreRemove >>> [{}]", object);
    }

    @PostPersist
    public void onPostPersist(Object object) {
        logger.trace("onPostPersist >>> [{}]", object);
    }

    @PostUpdate
    public void onPostUpdate(Object object) {
        logger.trace("onPostUpdate >>> [{}]", object);
    }

    private void updateTracingInformation(Object object) {
        if (object instanceof Auditable) {
            Auditable auditableEntity = (Auditable) object;
            Date now = DateUtil.now();// TODO
            String createUserId = "10000";//RequestContext.getCurrentUserId()
            String createUserName = "AUDITABLE";//RequestContext.getCurrentUserName()

            if ((auditableEntity.getId() == null) || (auditableEntity.getVersion() == null) || (auditableEntity.getVersion() <= auditableEntity.DEFAULT_VERSION)) {
                auditableEntity.setVersion(1L);
                auditableEntity.setCreateUserId(createUserId);
                auditableEntity.setCreateUserName(createUserName);
                auditableEntity.setCreateTime(now);
            }

            auditableEntity.setUpdateTime(now);
            auditableEntity.setCreateUserId(createUserId);
            auditableEntity.setCreateUserName(createUserName);
        }
    }
}
