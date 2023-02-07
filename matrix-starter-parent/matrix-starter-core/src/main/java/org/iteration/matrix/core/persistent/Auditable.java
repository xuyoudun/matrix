package org.iteration.matrix.core.persistent;

import org.iteration.matrix.core.convertor.Convertor;

import java.util.Date;

public interface Auditable<ID> extends Convertor, Persistable<ID> {

    public static final Long DEFAULT_VERSION = 1L;

    public Long getVersion();

    public void setVersion(Long version);

    public String getCreateUserName();

    public void setCreateUserName(String createUserName);

    public String getCreateUserId();

    public void setCreateUserId(String createUserId);

    public Date getCreateTime();

    public void setCreateTime(Date createTime);

    public String getUpdateUserName();

    public void setUpdateUserName(String updateUserName);

    public String getUpdateUserId();

    public void setUpdateUserId(String updateUserId);

    public Date getUpdateTime();

    public void setUpdateTime(Date updateTime);
}
