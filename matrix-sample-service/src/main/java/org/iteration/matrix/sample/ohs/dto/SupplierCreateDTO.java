package org.iteration.matrix.sample.ohs.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.iteration.matrix.core.restful.DTO;

import java.util.List;

@Data
@ApiModel("供应商中心-供应商")
public class SupplierCreateDTO extends DTO {

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

    private List<SupplierQualificationCreateDTO> qualificationList;

    private List<SupplierStockholderCreateDTO> stockholderList;
}
