package org.iteration.matrix.sample.acl.port.repository.po;

import lombok.Data;
import lombok.ToString;
import org.iteration.matrix.core.persistent.AuditPO;

import javax.persistence.*;

@Entity()
@Data
@ToString
@Table(name = "supplier_info")
public class SupplierPO extends AuditPO<Long> {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    /* 供应商编码 */
    private String supplierCode;

    /* 供应商名称 */
    private String supplierName;

    /* 客户编码 */
    private String creditCode;

    /* 客户编码 */
    private String certificateTypeCode;

    /* 客户编码 */
    private String certificateTypeName;

    /* 客户编码 */
    private String registeredAddress;

    /* 客户编码 */
    private String registeredCapital;

}
