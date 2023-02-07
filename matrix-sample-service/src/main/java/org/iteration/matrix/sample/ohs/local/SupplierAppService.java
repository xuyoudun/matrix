package org.iteration.matrix.sample.ohs.local;

import org.apache.commons.lang3.StringUtils;
import org.iteration.matrix.sample.domain.supplier.SupplierDomainService;
import org.iteration.matrix.sample.domain.supplier.entity.Supplier;
import org.iteration.matrix.sample.ohs.dto.SupplierComplexDTO;
import org.iteration.matrix.sample.ohs.dto.SupplierCreateDTO;
import org.iteration.matrix.sample.acl.port.repository.SupplierRepository;
import org.iteration.matrix.sample.ohs.assembler.SupplierAssembler;
import org.iteration.matrix.sample.ohs.dto.SupplierDTO;
import org.iteration.matrix.sample.ohs.dto.SupplierQueryDTO;
import org.iteration.matrix.core.convertor.Convertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SupplierAppService {

    @Autowired
    SupplierDomainService supplierDomainService;

    @Autowired
    SupplierRepository supplierRepository;// 复杂查询可以绕过domain层, 借鉴CQRS读写分离模式

    public Page<SupplierComplexDTO> findSupplierComplexPageList(SupplierQueryDTO queryDTO, Pageable pageable) {
        return supplierRepository.findSupplierComplexPageList(queryDTO, pageable);
    }

    public Page<SupplierDTO> findSupplierPageList(SupplierDTO queryDTO, Pageable pageable) {
        Page<Supplier> pageResult = supplierDomainService.findSupplierPageList(queryDTO.convertTo(Supplier.class), pageable);
        return Convertor.convertPage(pageResult, SupplierDTO.class);
    }

    public List<SupplierDTO> findSupplierList(SupplierDTO queryDTO) {
        List<Supplier> listResult = supplierDomainService.findSupplierList(queryDTO.convertTo(Supplier.class));
        return Convertor.convertList(listResult, SupplierDTO.class);
    }

    public SupplierDTO findSupplierDetail(String supplierCode) {
        Supplier supplier = supplierDomainService.findSupplierDetail(supplierCode);
        return SupplierAssembler.toDTO(supplier);
    }

    public SupplierDTO createOrReviseSupplier(SupplierCreateDTO createDTO) {
        Supplier createDO = SupplierAssembler.toDO(createDTO);
        if (StringUtils.isEmpty(createDO.getSupplierCode())) {
            createDO.setSupplierCode(UUID.randomUUID().toString());//模拟调用编码生成器生成供应商编码,在appService层编排
        }
        Supplier supplier = supplierDomainService.createOrReviseSupplier(createDO);
        return SupplierAssembler.toDTO(supplier);
    }


}
