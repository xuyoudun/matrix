package org.iteration.matrix.core.config;

import com.github.pagehelper.autoconfigure.PageHelperProperties;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


@Configuration
@AutoConfigureBefore({MybatisAutoConfiguration.class})
@EnableConfigurationProperties({MybatisProperties.class, PageHelperProperties.class})
public class MybatisAndPageHelperConfiguration {

    private final MybatisProperties mybatisProperties;
    private final PageHelperProperties pageHelperProperties;

    private final String DEFAULT_MAPPER_LOCATIONS = "classpath*:mybatis/mapper/*.xml";

    public MybatisAndPageHelperConfiguration(MybatisProperties mybatisProperties, PageHelperProperties pageHelperProperties) {
        //
        if (ObjectUtils.isEmpty(mybatisProperties.getMapperLocations())) {
            mybatisProperties.setMapperLocations(new String[]{DEFAULT_MAPPER_LOCATIONS});
        }

        // https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md
        if (StringUtils.isEmpty(pageHelperProperties.getOffsetAsPageNum())) {
            pageHelperProperties.setOffsetAsPageNum(true);
        }
        if (StringUtils.isEmpty(pageHelperProperties.getRowBoundsWithCount())) {
            pageHelperProperties.setRowBoundsWithCount(true);
        }
        if (StringUtils.isEmpty(pageHelperProperties.getReasonable())) {
            pageHelperProperties.setReasonable(true);
        }
        if (StringUtils.isEmpty(pageHelperProperties.getParams())) {
            pageHelperProperties.setParams("count=countSql");
        }

        this.mybatisProperties = mybatisProperties;
        this.pageHelperProperties = pageHelperProperties;
    }

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        // https://mybatis.org/mybatis-3/configuration.html#properties
        return new ConfigurationCustomizer() {
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                // Enables automatic mapping from classic database column names A_COLUMN to camel case classic Java property names aColumn.
                configuration.setMapUnderscoreToCamelCase(true);
            }
        };
    }
}
