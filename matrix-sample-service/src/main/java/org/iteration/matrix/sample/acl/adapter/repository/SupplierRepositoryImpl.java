package org.iteration.matrix.sample.acl.adapter.repository;

import org.iteration.matrix.core.convertor.Convertor;
import org.iteration.matrix.core.cqrs.CqrsQuery;
import org.iteration.matrix.sample.acl.adapter.repository.persistence.SupplierPersistence;
import org.iteration.matrix.sample.acl.adapter.repository.persistence.SupplierQualificationPersistence;
import org.iteration.matrix.sample.acl.adapter.repository.persistence.SupplierStockholderPersistence;
import org.iteration.matrix.sample.acl.factory.SupplierFactory;
import org.iteration.matrix.sample.acl.port.repository.SupplierRepository;
import org.iteration.matrix.sample.acl.port.repository.po.SupplierPO;
import org.iteration.matrix.sample.acl.port.repository.po.SupplierQualificationPO;
import org.iteration.matrix.sample.acl.port.repository.po.SupplierStockholderPO;
import org.iteration.matrix.sample.domain.supplier.entity.Supplier;
import org.iteration.matrix.sample.ohs.dto.SupplierComplexDTO;
import org.iteration.matrix.sample.ohs.dto.SupplierQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Repository方法签名出入参只能是DO(领域实体)，供domain层调用
 * 对于复杂查询(DO对象无法承载，比如关联查询)请使用CqrsQuery直接返回DTO，绕过domian层, 供app层直接调用。参考自CQRS读写分离模式
 * （domain层不允许出现DTO，PO等非实体对象）
 * <p>
 * Repository只是数据的CURD，原则上不要出现业务逻辑
 */
@Repository
public class SupplierRepositoryImpl implements SupplierRepository {

    @Autowired
    SupplierPersistence supplierPersistence;

    @Autowired
    SupplierQualificationPersistence qualificationPersistence;

    @Autowired
    SupplierStockholderPersistence stockholderPersistence;

    @Autowired
    CqrsQuery cqrsQuery;

    /**
     * 复杂查询，DO(领域实体)不能承载，返回DTO，绕过domain层供app层直接调用
     *
     * @param queryDTO
     * @param pageable
     * @return
     */
    @Override
    public Page<SupplierComplexDTO> findSupplierComplexPageList(SupplierQueryDTO queryDTO, Pageable pageable) {
        String statement = "supplier.mybatis.selectSupplier";
        return cqrsQuery.selectPage(statement, queryDTO, pageable);
    }

    /**
     * 简单查询，返回DO对象
     *
     * @param query
     * @param pageable
     * @return
     */
    @Override
    public Page<Supplier> findSupplierPageList(Supplier query, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("supplierCode", match -> match.exact())
                .withMatcher("supplierName", match -> match.startsWith());
        Example<SupplierPO> example = Example.of(query.convertTo(SupplierPO.class), matcher);
        Page<SupplierPO> pageResult = supplierPersistence.findAll(example, pageable);
        //Page<SupplierPO> pageResult = supplierPersistence.findBySupplierCodeNotNull(pageable);
        return Convertor.convertPage(pageResult, Supplier.class);
    }

    /**
     * 简单查询，返回DO对象
     *
     * @param query
     * @return
     */
    @Override
    public List<Supplier> findSupplierList(Supplier query) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("supplierCode", match -> match.exact())
                .withMatcher("supplierName", match -> match.startsWith());
        Example<SupplierPO> example = Example.of(query.convertTo(SupplierPO.class), matcher);
        List<SupplierPO> listResult = supplierPersistence.findAll(example);
        return Convertor.convertList(listResult, Supplier.class);
    }

    @Override
    public Supplier findSupplierById(Long id) {
        SupplierPO supplierPO = supplierPersistence.findById(id).orElse(null);
        if (supplierPO == null) {
            return null;
        }
        List<SupplierQualificationPO> qualificationPOList = qualificationPersistence.findBySupplierCode(supplierPO.getSupplierCode());
        List<SupplierStockholderPO> stockholderPOList = stockholderPersistence.findBySupplierCode(supplierPO.getSupplierCode());
        return SupplierFactory.toDO(supplierPO, qualificationPOList, stockholderPOList);
    }

    @Override
    public Supplier findSupplierByCode(String supplierCode) {
        SupplierPO supplierPO = supplierPersistence.findBySupplierCode(supplierCode);
        if (supplierPO == null) {
            return null;
        }
        List<SupplierQualificationPO> qualificationPOList = qualificationPersistence.findBySupplierCode(supplierPO.getSupplierCode());
        List<SupplierStockholderPO> stockholderPOList = stockholderPersistence.findBySupplierCode(supplierPO.getSupplierCode());
        return SupplierFactory.toDO(supplierPO, qualificationPOList, stockholderPOList);
    }

    @Override
    public Supplier saveOrUpdateSupplier(Supplier entity) {
        SupplierPO supplierPO = entity.convertTo(SupplierPO.class);
        String supplierCode = supplierPO.getSupplierCode();

        supplierPO = supplierPersistence.save(supplierPO);
        List<SupplierQualificationPO> qualificationPOList = null;
        List<SupplierStockholderPO> stockholderPOList = null;

        // null和empty含义不同，null代表不处理，empty代表清空
        if (entity.getQualificationList() != null) {
            Map<Long, SupplierQualificationPO> inDBMap = qualificationPersistence.findBySupplierCode(supplierCode).stream().collect(Collectors.toMap(SupplierQualificationPO::getId, Function.identity()));

            qualificationPOList = entity.getQualificationList().stream().map(itemEntity -> {
                inDBMap.remove(itemEntity.getId());
                itemEntity.setSupplierCode(supplierCode);
                return qualificationPersistence.save(itemEntity.convertTo(SupplierQualificationPO.class));
            }).collect(Collectors.toList());

            qualificationPersistence.deleteAll(new ArrayList<>(inDBMap.values()));
        }

        // null和empty含义不同，null代表不处理，empty代表清空
        if (entity.getStockholderList() != null) {
            Map<Long, SupplierStockholderPO> inDBMap = stockholderPersistence.findBySupplierCode(supplierCode).stream().collect(Collectors.toMap(SupplierStockholderPO::getId, Function.identity()));

            stockholderPOList = entity.getStockholderList().stream().map(itemEntity -> {
                inDBMap.remove(itemEntity.getId());
                itemEntity.setSupplierCode(supplierCode);
                return stockholderPersistence.save(itemEntity.convertTo(SupplierStockholderPO.class));
            }).collect(Collectors.toList());

            stockholderPersistence.deleteAll(new ArrayList<>(inDBMap.values()));
        }

        return SupplierFactory.toDO(supplierPO, qualificationPOList, stockholderPOList);
    }
}
