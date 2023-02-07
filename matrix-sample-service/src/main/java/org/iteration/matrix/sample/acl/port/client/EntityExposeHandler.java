package org.iteration.matrix.sample.acl.port.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RequestPredicates.accept;
import static org.springframework.web.servlet.function.RouterFunctions.route;

/**
 * 通用编辑功能
 */
@Component
public class EntityExposeHandler {

    @Autowired
    ExposeService exposeService;

    /**
     * 生成列表查询视图
     *
     * @param request
     * @return
     */
    public ServerResponse queryView(ServerRequest request) {
        try {
            String entityName = request.pathVariable("entityName");
            Map<String, Object> result = exposeService.queryView(entityName);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ServerResponse.status(500).body(ex.getMessage());
        }
    }

    /**
     * 获取列表或分页数据
     *
     * @param request
     * @return
     */
    public ServerResponse listOrPageEntityData(ServerRequest request) {
        try {
            String entityName = request.pathVariable("entityName");
            // url参数
            Long pageNum = request.param("pageNum").flatMap(this::getLong).orElse(1L);
            Long pageSize = request.param("pageSize").flatMap(this::getLong).orElse(0L);
            // 请求消息体
            Map<String, Object> formData = request.body(Map.class);
            Map<String, Object> result = exposeService.listOrPageEntityData(entityName, formData, pageNum, pageSize);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ServerResponse.status(500).body(ex.getMessage());
        }
    }

    /**
     * 生成新增/编辑视图
     *
     * @param request
     * @return
     */
    public ServerResponse editView(ServerRequest request) {
        try {
            String entityName = request.pathVariable("entityName");
            Map<String, Object> formData = request.body(Map.class);
            Map<String, Object> result = exposeService.editView(entityName, formData);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ServerResponse.status(500).body(ex.getMessage());
        }
    }

    /**
     * 保存编辑数据
     *
     * @param request
     * @return
     */
    public ServerResponse saveEditData(ServerRequest request) {
        try {
            String entityName = request.pathVariable("entityName");
            Map<String, Object> formData = request.body(Map.class);

            Map<String, Object> result = exposeService.saveEditData(entityName, formData);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ServerResponse.status(500).body(ex.getMessage());
        }
    }


    /**
     * 生成配置视图
     *
     * @param request
     * @return
     */
    public ServerResponse configView(ServerRequest request) {
        try {
            Map<String, Object> result = exposeService.configView();
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ServerResponse.status(500).body(ex.getMessage());
        }
    }

    /**
     * 获取配置数据
     *
     * @param request
     * @return
     */
    public ServerResponse getConfigData(ServerRequest request) {
        try {
            String entityName = request.pathVariable("entityName");
            Map<String, Object> result = exposeService.getConfigData(entityName);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ServerResponse.status(500).body(ex.getMessage());
        }
    }

    /**
     * 保存配置数据
     *
     * @param request
     * @return
     */
    public ServerResponse saveConfigData(ServerRequest request) {
        try {
            String entityName = request.pathVariable("entityName");
            Map<String, Object> result = exposeService.getConfigData(entityName);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ServerResponse.status(500).body(ex.getMessage());
        }
    }


    public ServerResponse saveEntity(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(null);
    }


    @Bean
    public RouterFunction<?> entityExposeRouter() {
        RouterFunction<ServerResponse> route = route()
                .POST("/entity/view/{entityName}/query", accept(APPLICATION_JSON), this::queryView)
                .POST("/entity/view/{entityName}/edit", accept(APPLICATION_JSON), this::editView)
                .POST("/entity/view/config", accept(APPLICATION_JSON), this::configView)

                .POST("/entity/{entityName}/data", accept(APPLICATION_JSON), this::listOrPageEntityData)
                .POST("/entity/{entityName}/edit", accept(APPLICATION_JSON), this::saveEditData)
                .POST("/entity/{entityName}/config/data", accept(APPLICATION_JSON), this::getConfigData)
                //.POST("/person", handler::createPerson)
                .build();

        return route;
    }


    Optional<String> getString(Object value) {
        return Optional.ofNullable(value).map(v -> v.toString());
    }

    Optional<Long> getLong(Object value) {
        return getNumber(value).map(v -> v instanceof Long ? (Long) v : new Long(v.longValue()));
    }

    Optional<Number> getNumber(Object value) {
        return Optional.ofNullable(value).map(v -> {
            if (v instanceof Number) {
                return (Number) v;
            } else if (v instanceof String) {
                try {
                    String text = (String) v;
                    return NumberFormat.getInstance().parse(text);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }
}
