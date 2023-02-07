package org.iteration.matrix.sample.domain.supplier.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.iteration.matrix.core.domain.DomainEntity;

import java.util.List;

@Data
public class Supplier extends DomainEntity {

    @ApiModelProperty(value = "客户编码")
    private Long id;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "客户编码")
    private String creditCode;

    @ApiModelProperty(value = "客户编码")
    private String certificateTypeCode;

    @ApiModelProperty(value = "客户编码")
    private String certificateTypeName;

    @ApiModelProperty(value = "客户编码")
    private String registeredAddress;

    @ApiModelProperty(value = "客户编码")
    private String registeredCapital;

    private List<SupplierQualification> qualificationList;

    private List<SupplierStockholder> stockholderList;

}
