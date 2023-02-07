package org.iteration.matrix.sample.acl.adapter.repository.persistence;

import org.iteration.matrix.sample.acl.port.repository.po.SupplierStockholderPO;
import org.iteration.matrix.core.cqrs.CqrsPersistence;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierStockholderPersistence extends CqrsPersistence<SupplierStockholderPO, Long> {

    List<SupplierStockholderPO> findBySupplierCode(String supplierCode);

}
