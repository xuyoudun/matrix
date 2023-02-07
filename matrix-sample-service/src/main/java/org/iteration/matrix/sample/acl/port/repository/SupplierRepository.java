package org.iteration.matrix.sample.acl.port.repository;

import org.iteration.matrix.sample.domain.supplier.entity.Supplier;
import org.iteration.matrix.sample.ohs.dto.SupplierComplexDTO;
import org.iteration.matrix.sample.ohs.dto.SupplierQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierRepository {

    Page<SupplierComplexDTO> findSupplierComplexPageList(SupplierQueryDTO queryDTO, Pageable pageable);

    Page<Supplier> findSupplierPageList(Supplier query, Pageable pageable);

    List<Supplier> findSupplierList(Supplier query);

    Supplier findSupplierById(Long id);

    Supplier findSupplierByCode(String supplierCode);

    Supplier saveOrUpdateSupplier(Supplier supplier);
}
