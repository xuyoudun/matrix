package org.iteration.matrix.sample.acl.adapter.repository.persistence;

import org.iteration.matrix.sample.acl.port.repository.po.SupplierQualificationPO;
import org.iteration.matrix.core.cqrs.CqrsPersistence;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierQualificationPersistence extends CqrsPersistence<SupplierQualificationPO, Long> {

    List<SupplierQualificationPO> findBySupplierCode(String supplierCode);

}
