package org.iteration.matrix.sample.ohs.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.iteration.matrix.core.restful.DTO;

@Data
@ApiModel("供应商中心-供应商股东信息")
public class SupplierStockholderDTO extends DTO {

    @ApiModelProperty(value = "客户编码")
    private Long id;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "其它省略属性")
    private String typeName;
}
