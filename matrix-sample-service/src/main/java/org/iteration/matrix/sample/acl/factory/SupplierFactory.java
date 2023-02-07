package org.iteration.matrix.sample.acl.factory;

import org.iteration.matrix.sample.domain.supplier.entity.Supplier;
import org.iteration.matrix.sample.domain.supplier.entity.SupplierQualification;
import org.iteration.matrix.sample.domain.supplier.entity.SupplierStockholder;
import org.iteration.matrix.sample.acl.port.repository.po.SupplierPO;
import org.iteration.matrix.sample.acl.port.repository.po.SupplierQualificationPO;
import org.iteration.matrix.sample.acl.port.repository.po.SupplierStockholderPO;
import org.iteration.matrix.core.convertor.Convertor;

import java.util.List;

/**
 * Factory用于构建复杂的DO（领域实体）对象
 * 或PO与DO(领域实体)互相转换
 */
public class SupplierFactory {

    public static Supplier toDO(SupplierPO supplierPO, List<SupplierQualificationPO> qualificationPOList, List<SupplierStockholderPO> stockholderPOList) {
        Supplier entity = supplierPO.convertTo(Supplier.class);
        entity.setQualificationList(Convertor.convertList(qualificationPOList, SupplierQualification.class));
        entity.setStockholderList(Convertor.convertList(stockholderPOList, SupplierStockholder.class));
        return entity;
    }
}
