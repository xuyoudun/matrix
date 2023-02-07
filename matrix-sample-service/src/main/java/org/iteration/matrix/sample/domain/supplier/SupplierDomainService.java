package org.iteration.matrix.sample.domain.supplier;

import org.iteration.matrix.sample.acl.port.repository.SupplierRepository;
import org.iteration.matrix.sample.domain.supplier.entity.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierDomainService {

    @Autowired
    SupplierRepository supplierRepository;

    public Page<Supplier> findSupplierPageList(Supplier query, Pageable pageable) {
        return supplierRepository.findSupplierPageList(query, pageable);
    }

    public List<Supplier> findSupplierList(Supplier query) {
        return supplierRepository.findSupplierList(query);
    }

    public Supplier findSupplierDetail(String supplierCode) {
        return supplierRepository.findSupplierByCode(supplierCode);
    }

    public Supplier createOrReviseSupplier(Supplier supplier) {
        return supplierRepository.saveOrUpdateSupplier(supplier);
    }
}

