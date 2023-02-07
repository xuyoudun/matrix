package org.iteration.matrix.sample.acl.adapter.repository.persistence;

import org.iteration.matrix.sample.acl.port.repository.po.SupplierPO;
import org.iteration.matrix.core.cqrs.CqrsPersistence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierPersistence extends CqrsPersistence<SupplierPO, Long> {

    SupplierPO findBySupplierCode(String supplierCode);

    //Page<SupplierPO> findBySupplierCodeNotNull(Pageable pageable);
}
