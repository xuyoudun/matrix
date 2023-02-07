package org.iteration.matrix.sample.acl.port.repository.po;

import lombok.Data;
import lombok.ToString;
import org.iteration.matrix.core.persistent.AuditPO;

import javax.persistence.*;

@Entity()
@Data
@ToString
@Table(name = "supplier_stockholder")
public class SupplierStockholderPO extends AuditPO<Long> {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /* 供应商编码 */
    private String supplierCode;

    /* 其它省略属性 */
    private String typeName;
}
