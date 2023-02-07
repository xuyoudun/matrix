package org.iteration.matrix.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ApplyTypeEnum {

    // 申请单类型: NEW_AUDIT-新建，CHAHGE_AUDIT-变更
    NEW_AUDIT("NEW_AUDIT", "新建"),
    CHAHGE_AUDIT("CHAHGE_AUDIT", "变更"),
    INACTIVE_AUDIT("INACTIVE_AUDIT", "失效"),
    ;

    private String code;
    private String name;

    public static ApplyTypeEnum of(String code) {
        return Arrays.stream(values())
                .filter(f -> f.code.equals(code))
                .findAny()
                .orElse(null);
    }
}
