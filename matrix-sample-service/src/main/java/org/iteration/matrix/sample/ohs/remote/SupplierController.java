package org.iteration.matrix.sample.ohs.remote;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.iteration.matrix.core.restful.APIVersion;
import org.iteration.matrix.sample.ohs.dto.SupplierComplexDTO;
import org.iteration.matrix.sample.ohs.dto.SupplierCreateDTO;
import org.iteration.matrix.sample.ohs.dto.SupplierDTO;
import org.iteration.matrix.sample.ohs.dto.SupplierQueryDTO;
import org.iteration.matrix.sample.ohs.local.SupplierAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "SupplierController Manager(Site Level)")
@RestController("SupplierController.v1")
@RequestMapping("/supplier")
@APIVersion
public class SupplierController {

    @Autowired
    SupplierAppService supplierAppService;

    @ApiOperation(value = "供应商分页列表查询")
    @PostMapping("/page/complex")
    public Page<SupplierComplexDTO> findSupplierComplexPageList(@RequestBody SupplierQueryDTO queryDTO,
                                                                @PageableDefault(page = 1, size = 10) Pageable pageable) {
        return supplierAppService.findSupplierComplexPageList(queryDTO, pageable);
    }

    @ApiOperation(value = "供应商分页列表查询")
    @APIVersion({"v2", "v3"})
    @PostMapping("/page")
    public Page<SupplierDTO> findSupplierPageList(@RequestBody SupplierDTO queryDTO,
                                                  @PageableDefault(size = 20) Pageable pageable) {
        return supplierAppService.findSupplierPageList(queryDTO, pageable);
    }

    @ApiOperation(value = "供应商列表查询")
    @PostMapping("/list")
    public List<SupplierDTO> findSupplierList(@RequestBody SupplierDTO queryDTO) {
        return supplierAppService.findSupplierList(queryDTO);
    }

    @ApiOperation(value = "供应商详情")
    @GetMapping
    public SupplierDTO findSupplierDetail(@RequestParam String supplierCode) {
        return supplierAppService.findSupplierDetail(supplierCode);
    }

    @ApiOperation(value = "供应商注册接口")
    @PostMapping("/register")
    public SupplierDTO createOrReviseSupplier(@RequestBody SupplierCreateDTO createDTO) {
        return supplierAppService.createOrReviseSupplier(createDTO);
    }

    @ApiOperation(value = "供应商变更")
    @PostMapping("/change")
    public SupplierDTO changeSupplier(@RequestBody SupplierCreateDTO createDTO) {
        return supplierAppService.createOrReviseSupplier(createDTO);
    }
}
