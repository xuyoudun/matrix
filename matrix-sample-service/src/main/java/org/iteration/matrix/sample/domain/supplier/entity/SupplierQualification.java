package org.iteration.matrix.sample.domain.supplier.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.iteration.matrix.core.domain.DomainEntity;

@Data
public class SupplierQualification extends DomainEntity {

    @ApiModelProperty(value = "客户编码")
    private Long id;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "其它省略属性")
    private String typeName;
}
