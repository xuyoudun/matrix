package org.iteration.matrix.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ApplyStatusEnum {

    // 申请单状态:DRAFT-草稿,INPROCESS-审批中,APPROVED-已审批,REJECTED-已驳回
    DRAFT("DRAFT", "草稿"),
    INPROCESS("INPROCESS", "审批中"),
    APPROVED("APPROVED", "已审批"),
    REJECTED("REJECTED", "已驳回"),
    ;

    private String code;
    private String name;

    public static ApplyStatusEnum of(String code) {
        return Arrays.stream(values())
                .filter(f -> f.code.equals(code))
                .findAny()
                .orElse(null);
    }
}
