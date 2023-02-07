package org.iteration.matrix.core.persistent;

import lombok.Data;
import org.iteration.matrix.core.convertor.Convertor;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditPersistListener.class)
public abstract class AuditPO<ID> implements Convertor, Auditable<ID> {

    private static final long serialVersionUID = 8478392447163913140L;

    /**
     * 版本号
     */
    @Version
    //@NotNull
    @Column(name = "version")
    private Long version = DEFAULT_VERSION;

    /**
     * 用户名称
     */
    @Column(name = "create_user_name", updatable = false)
    private String createUserName;

    /**
     * 创建用户id
     */
    @Column(name = "create_user_id", updatable = false)
    private String createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time", updatable = false)
    private Date createTime;

    /**
     * 修改用户名称
     */
    @Column(name = "update_user_name", updatable = true)
    private String updateUserName;

    /**
     * 修改用户id
     */
    @Column(name = "update_user_id", updatable = true)
    private String updateUserId;

    /**
     * 修改时间
     */
    @Column(name = "update_time", updatable = true)
    private Date updateTime;

    @Column(name = "enabled", updatable = false)
    private String enabled;

    public int hashCode() {
        int i = 17;
        i += getId() != null ? getId().hashCode() * 31 : super.hashCode();
        return i;
    }

    public boolean equals(Object obj) {
        boolean isEquals = false;
        if (null == obj) {
            isEquals = false;
        } else {
            if (obj instanceof AuditPO) {
                AuditPO<ID> other = (AuditPO<ID>) obj;
                if (this.getId() != null && other.getId() != null) {
                    isEquals = (this.getId().equals(other.getId()));
                } else {
                    isEquals = (this == obj);
                }
            } else {
                isEquals = false;
            }
        }
        return isEquals;
    }

}
