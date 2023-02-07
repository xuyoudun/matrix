package org.iteration.matrix.sample.ohs.assembler;

import org.iteration.matrix.sample.domain.supplier.entity.Supplier;
import org.iteration.matrix.sample.domain.supplier.entity.SupplierQualification;
import org.iteration.matrix.sample.domain.supplier.entity.SupplierStockholder;
import org.iteration.matrix.sample.ohs.dto.SupplierCreateDTO;
import org.iteration.matrix.sample.ohs.dto.SupplierQualificationDTO;
import org.iteration.matrix.sample.ohs.dto.SupplierStockholderDTO;
import org.iteration.matrix.sample.ohs.dto.SupplierDTO;
import org.iteration.matrix.core.convertor.Convertor;

/**
 * DTO和DO(领域实体)相互转换
 */
public class SupplierAssembler {

    public static SupplierDTO toDTO(Supplier entity) {
        SupplierDTO dto = entity.convertTo(SupplierDTO.class);
        dto.setQualificationList(Convertor.convertList(entity.getQualificationList(), SupplierQualificationDTO.class));
        dto.setStockholderList(Convertor.convertList(entity.getStockholderList(), SupplierStockholderDTO.class));
        return dto;
    }

    public static Supplier toDO(SupplierCreateDTO createDTO) {
        Supplier entity = createDTO.convertTo(Supplier.class);
        entity.setQualificationList(Convertor.convertList(createDTO.getQualificationList(), SupplierQualification.class));
        entity.setStockholderList(Convertor.convertList(createDTO.getStockholderList(), SupplierStockholder.class));
        return entity;
    }
}
