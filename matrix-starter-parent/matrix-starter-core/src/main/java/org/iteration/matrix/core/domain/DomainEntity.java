package org.iteration.matrix.core.domain;

import lombok.Data;
import org.iteration.matrix.core.convertor.Convertor;

@Data
public class DomainEntity implements Convertor {

    private Long version;

    protected void updateInfoEvent() {
        //触发事件等操作
    }

    protected void createInfoEvent() {
        //触发事件等操作
    }
}
