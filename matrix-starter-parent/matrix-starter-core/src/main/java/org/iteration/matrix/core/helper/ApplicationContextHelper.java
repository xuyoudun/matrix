package org.iteration.matrix.core.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;

        /*if (applicationContext instanceof BeanDefinitionRegistry) {
            BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext;

            for (EntityType<?> entityType : entityManager.getMetamodel().getEntities()) {
                Class<?> entityClazz = entityType.getJavaType();

                BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(MagnetoRepositoryJpaImpl.class);
                //beanDefinitionBuilder.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_NO);
               //AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(DDDRepository.class);
                //beanDefinitionBuilder.getBeanDefinition().addQualifier(qualifier);


                //CustomAutowireConfigurer cac = new CustomAutowireConfigurer();
                SimpleJpaRepository simpleJpaRepository = new SimpleJpaRepository(entityType.getJavaType(), entityManager);
                //beanDefinitionBuilder.addPropertyValue("jpaRepository", simpleJpaRepository);
                beanDefinitionBuilder.addConstructorArgValue(simpleJpaRepository);//
                //beanDefinitionRegistry.registerBeanDefinition(entityType.getJavaType().getName(), beanDefinitionBuilder.getBeanDefinition());
                //beanDefinitionRegistry.registerAlias(entityType.getJavaType().getName(), entityType.getName());
                String generatedName = BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinitionBuilder.getBeanDefinition(), beanDefinitionRegistry);
                beanDefinitionRegistry.registerAlias(generatedName, entityType.getJavaType().getName());
                beanDefinitionRegistry.registerAlias(generatedName, entityType.getName().replace("PO","Repository"));

                Object xxx = applicationContext.getBean(entityType.getJavaType().getName());
                System.out.println(xxx);
            }

            if (applicationContext instanceof ListableBeanFactory) {
                ListableBeanFactory listableBeanFactory = (ListableBeanFactory) applicationContext;

                Map<String, MagnetoRepositoryJpaImpl> users = listableBeanFactory.getBeansOfType(MagnetoRepositoryJpaImpl.class);
                System.out.println("查找到的所有的 User 集合对象：" + users);
            }

        }*/

    }
}
