package org.iteration.matrix.sample.acl.port.client;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class ExposeService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Map<String, Object> queryView(String entityName) {

        MetaEntity metaEntity = metadata(entityName);
        List<MetaEntityField> metaEntityFields = metaEntity.fields;

        List<FormItem> formItems = metaEntityFields.stream()
                .filter(field -> isNotEmpty(field.filterStrategy))
                .map(field -> {
                    FormItem formItem = new FormItem();
                    formItem.label = field.fieldLabel;
                    formItem.name = field.fieldName;
                    formItem.valueType = field.formItemType;
                    if (isNotEmpty(field.optionData) && isEmpty(field.optionDependencies)) {
                        List<Map<String, Object>> options = jdbcTemplate.queryForList(field.optionData);
                        formItem.options = options;
                    }
                    return formItem;
                }).collect(Collectors.toList());


        List<Column> columns = metaEntityFields.stream()
                .filter(field -> Whether.T.name().equals(field.enabled))
                .map(filed -> {
                    Column column = new Column();
                    column.dataIndex = filed.fieldName;
                    column.title = filed.fieldLabel;
                    column.width = filed.width;
                    return column;
                }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("formItems", formItems);
        result.put("columns", columns);

        return result;
    }

    public Map<String, Object> listOrPageEntityData(String entityName, Map<String, Object> formData, Long pageNum, Long pageSize) {

        MetaEntity metaEntity = metadata(entityName);
        List<MetaEntityField> metaEntityFields = metaEntity.fields;

        List<MetaEntityField> enabledFields = metaEntityFields.stream()
                .filter(field -> Whether.T.name().equals(field.enabled))
                .collect(Collectors.toList());
        List<MetaEntityField> filterStrategyFields = metaEntityFields.stream().filter(field -> isNotEmpty(field.filterStrategy)).collect(Collectors.toList());

        String baseSql = String.format("select %s from %s", enabledFields.stream().map(a -> a.fieldName).collect(Collectors.joining(",")), entityName);

        List<Object> params = new ArrayList<>();
        StringBuffer whereClause = new StringBuffer("where 1 = 1");

        for (MetaEntityField fieldDefine : filterStrategyFields) {
            Object value = formData.get(fieldDefine.fieldName);
            if (isNotEmpty(value)) {
                Strategy strategy = FilterStrategy.valueOf(fieldDefine.filterStrategy);
                strategy.criteriaBuilder(whereClause, fieldDefine, params, value);
            }
        }

        Map<String, Object> result = new HashMap<>();
        if (pageSize > 0) { // 分页查询
            String querySql = String.format("%s %s limit %d, %d", baseSql, whereClause.toString(), (pageNum - 1) * pageSize, pageSize);
            List<Map<String, Object>> dataSource = jdbcTemplate.queryForList(querySql, params.toArray());

            String countSql = String.format("select count(*) from %s %s", entityName, whereClause.toString());
            Integer total = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);

            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);
            result.put("total", total);
            result.put("dataSource", dataSource);
        } else {
            String querySql = String.format("%s %s", baseSql, whereClause.toString());
            List<Map<String, Object>> dataSource = jdbcTemplate.queryForList(querySql, params.toArray());

            result.put("dataSource", dataSource);
        }

        return result;

    }

    public Map<String, Object> editView(String entityName, Map<String, Object> formData) {

        MetaEntity metaEntity = metadata(entityName);
        List<MetaEntityField> metaEntityFields = metaEntity.fields;

        List<FormItem> formItems = metaEntityFields.stream()
                .filter(field -> Whether.T.name().equals(field.enabled))
                .map(field -> {
                    FormItem formItem = new FormItem();
                    formItem.label = field.fieldLabel;
                    formItem.name = field.fieldName;
                    formItem.valueType = field.formItemType;
                    if (isNotEmpty(field.optionData) && isEmpty(field.optionDependencies)) {
                        List<Map<String, Object>> options = jdbcTemplate.queryForList(field.optionData);
                        formItem.options = options;
                    }
                    return formItem;
                }).collect(Collectors.toList());

        Map<String, Object> initialValue = new HashMap<>();

        List<MetaEntityField> enabledFields = metaEntityFields.stream()
                .filter(field -> Whether.T.name().equals(field.enabled))
                .collect(Collectors.toList());
        String uniqueKey = isNotEmpty(metaEntity.primaryKey) ? metaEntity.primaryKey : metaEntity.businessKey;
        Object uniqueValue = formData.get(uniqueKey);

        if (isEmpty(formData)) {
            String querySql = String.format("select %s from %s where 1 = 2",
                    enabledFields.stream().map(a -> a.fieldName).collect(Collectors.joining(",")), entityName);
            String[] columnNames = jdbcTemplate.queryForRowSet(querySql).getMetaData().getColumnNames();
            for (String columnName : columnNames) {
                initialValue.put(columnName, null);
            }
        } else if (isNotEmpty(uniqueKey) && isNotEmpty(uniqueValue)) {
            String querySql = String.format("select %s from %s where %s = ?",
                    enabledFields.stream().map(a -> a.fieldName).collect(Collectors.joining(",")), entityName, uniqueKey);
            initialValue = jdbcTemplate.queryForMap(querySql, new Object[]{uniqueValue});

        } else {
            // TODO
        }

        Map<String, Object> result = new HashMap<>();
        result.put("formItems", formItems);
        result.put("initialValue", initialValue);

        return result;
    }

    public Map<String, Object> saveEditData(String entityName, Map<String, Object> formData) {
        MetaEntity metaEntity = metadata(entityName);
        List<MetaEntityField> metaEntityFields = metaEntity.fields;

        String primaryKey = metaEntity.primaryKey;
        Object businessKey = metaEntity.businessKey;

        if (isNotEmpty(formData.get(primaryKey))) {
            List<Object> params = new ArrayList<>();
            String updateSql = String.format("update %s set %s where %s = ?", metaEntity.entityName,
                    metaEntityFields.stream()
                            .filter(field -> Whether.T.name().equals(field.enabled) && !field.fieldName.equals(primaryKey) && !field.fieldName.equals(businessKey))
                            .map(field -> {
                                params.add(formData.get(field.fieldName));
                                return field.fieldName + " = ? ";
                            })
                            .collect(Collectors.joining(",")), primaryKey);
            params.add(formData.get(primaryKey));

            jdbcTemplate.update(updateSql, params.toArray());
        } else if (isNotEmpty(formData.get(businessKey))) {

        } else {
            String insertSql = "";
            metaEntityFields.stream()
                    .filter(field -> Whether.T.name().equals(field.enabled))
                    .map(field -> field.fieldName)
                    .collect(Collectors.toList());

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement preparedStatement =  connection.prepareStatement(insertSql);
                    preparedStatement.setObject(1, null);
                    return preparedStatement;
                }
            }, keyHolder);
            keyHolder.getKey();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("initialValue", null);
        return result;
    }


    public Map<String, Object> configView() {

        List<MetaEntityField> metaEntityFields = metadata4Fields("entity_define");
        List<MetaEntityField> metaEntityFieldFields = metadata4Fields("entity_field_define");

        List<FormItem> formItems = metaEntityFields.stream()
                .map(field -> {
                    FormItem formItem = new FormItem();
                    formItem.label = field.fieldLabel;
                    formItem.name = field.fieldName;
                    formItem.valueType = field.formItemType;
                    if (isNotEmpty(field.optionData) && isEmpty(field.optionDependencies)) {
                        List<Map<String, Object>> options = jdbcTemplate.queryForList(field.optionData);
                        formItem.options = options;
                    }
                    return formItem;
                }).collect(Collectors.toList());

        List<Column> columns = metaEntityFieldFields.stream()
                .filter(field -> Whether.T.name().equals(field.enabled))
                .map(field -> {
                    Column column = new Column();
                    column.dataIndex = field.fieldName;
                    column.title = field.fieldLabel;
                    column.width = field.width;

                    FormItem formItem = new FormItem();
                    formItem.label = field.fieldLabel;
                    formItem.name = field.fieldName;
                    formItem.valueType = field.formItemType;
                    if (isNotEmpty(field.optionData) && isEmpty(field.optionDependencies)) {
                        List<Map<String, Object>> options = jdbcTemplate.queryForList(field.optionData);
                        formItem.options = options;
                    }
                    column.render = formItem;

                    return column;
                }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("formItems", formItems);
        result.put("columns", columns);

        return result;
    }

    public Map<String, Object> getConfigData(String entityName) {

        Map<String, Object> initialValue = jdbcTemplate.queryForMap("select * from entity_define where entity_name = ?", entityName);
        List<Map<String, Object>> dataSource = jdbcTemplate.queryForList("select * from entity_field_define where entity_name = ?", entityName);

        Map<String, Object> result = new HashMap<>();
        result.put("initialValue", initialValue);
        result.put("dataSource", dataSource);

        return result;
    }


    @Deprecated
    private List<MetaEntityField> metadata4Fields(String entityName) {
        List<MetaEntityField> metaEntityFields = jdbcTemplate.query("select * from entity_field_define where entity_name = ? order by ordinal_position",
                new Object[]{entityName}, new BeanPropertyRowMapper<>(MetaEntityField.class));
        return metaEntityFields;
    }

    private final Map<String, MetaEntity> cache = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    private MetaEntity metadata(String entityName) {
        MetaEntity entity = cache.get(entityName);
        if (entity != null) return entity;

        boolean locked = false;
        try {
            locked = lock.tryLock();
            if (locked) {
                MetaEntity entityMeta = jdbcTemplate.queryForObject("select * from entity_define where entity_name = ?",
                        new Object[]{entityName}, new BeanPropertyRowMapper<>(MetaEntity.class));
                entityMeta.fields = jdbcTemplate.query("select * from entity_field_define where entity_name = ? order by ordinal_position",
                        new Object[]{entityName}, new BeanPropertyRowMapper<>(MetaEntityField.class));
                cache.put(entityName, entityMeta);
                return entityMeta;
            } else {
                Thread.sleep(50);
                return metadata(entityName);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (locked) lock.unlock();
        }
    }

    /*@PostConstruct
    public void init() {
        List<MetaEntity> metaEntities = jdbcTemplate.query("select * from entity_define", new BeanPropertyRowMapper<>(MetaEntity.class));
        List<MetaEntityField> metaEntityFields = jdbcTemplate.query("select * from entity_field_define order by entity_name, ordinal_position",
                new BeanPropertyRowMapper<>(MetaEntityField.class));

        Map<String, List<MetaEntityField>> groupBy = metaEntityFields.stream().collect(Collectors.groupingBy(MetaEntityField::getEntityName));
        metaEntities.forEach(entity -> entity.setFields(groupBy.get(entity.entityName)));
    }*/

    private enum Whether {
        T, F; // True or False
    }

    private boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof CharSequence) {
            return ((CharSequence) object).length() == 0;
        } else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        } else if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        } else {
            return object instanceof Map ? ((Map) object).isEmpty() : false;
        }
    }

    private boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    private interface Strategy {
        void criteriaBuilder(StringBuffer whereClause, MetaEntityField field, List<Object> params, Object value);
    }

    private enum FilterStrategy implements Strategy {
        // 过滤策略
        // Equals;Like;NotLike;StartingWith;EndingWith;Not;
        // LessThan;LessThanEqual;GreaterThan;GreaterThanEqual;After;Before;
        // Between;In;NotIn;
        // IsNull;IsNotNull;
        Equals() {
            @Override
            public void criteriaBuilder(StringBuffer whereClause, MetaEntityField field, List<Object> params, Object value) {
                whereClause.append(String.format(" and %s = ?", field.fieldName));
                params.add(value);
            }
        },
        Like() {
            @Override
            public void criteriaBuilder(StringBuffer whereClause, MetaEntityField field, List<Object> params, Object value) {
                whereClause.append(String.format(" and %s like ?", field.fieldName));
                params.add("%" + value + "%");
            }
        },
        StartingWith() {
            @Override
            public void criteriaBuilder(StringBuffer whereClause, MetaEntityField field, List<Object> params, Object value) {
                whereClause.append(String.format(" and %s like ?", field.fieldName));
                params.add(value + "%");
            }
        },
        EndingWith() {
            @Override
            public void criteriaBuilder(StringBuffer whereClause, MetaEntityField field, List<Object> params, Object value) {
                whereClause.append(String.format(" and %s like ?", field.fieldName));
                params.add("%" + value);
            }
        };
    }

    /**
     * 表单字段描述对象
     */
    @Data
    public static class FormItem {
        String label; // label 标签的文本
        String name; // NamePath
        String valueType; // 前端控件类型 Input,InputNumber,DatePick,Select,CheckBox,Radio
        List<Map<String, Object>> options; // Select选项内容{ label, value }[]
        String dependencies;
    }

    /**
     * 表格列描述数据对象
     */
    @Data
    public static class Column {
        String dataIndex;
        String title;
        String width;
        Object render; // 默认显示字段文本，设置render则自定义渲染
        List<Column> children; // 多级表头
    }

    @Data
    private static class MetaEntity {
        Long id;
        String entityName;
        String entityComment;
        String entityLabel;
        String primaryKey;
        String businessKey;

        List<MetaEntityField> fields;
    }

    @Data
    private static class MetaEntityField {
        Long id;
        String entityName;
        String fieldName;
        String fieldComment;
        String fieldLabel;
        String fieldType;
        Integer ordinalPosition;
        String optionData;
        String optionGroup;
        String optionDependencies;
        String dataType;
        Integer dataLength;
        Integer dataPrecision;
        Integer dataScale;
        String formItemType;
        String filterStrategy;
        String enabled;
        String width;
        String orderBy;
    }
}
