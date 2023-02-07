package org.iteration.matrix.core.utils;

import org.dozer.DozerBeanMapper;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class BeanMapperUtil {

    private static DozerBeanMapper mapper = new DozerBeanMapper();

    private BeanMapperUtil() {
    }

    public static DozerBeanMapper getMapper() {
        return mapper;
    }

    /**
     * 构造新的destinationClass实例对象，通过source对象中的字段内容
     * 映射到destinationClass实例对象中，并返回新的destinationClass实例对象。
     *
     * @param source           源数据对象
     * @param destinationClass 要构造新的实例对象Class
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        return getMapper().map(source, destinationClass);
    }

    /**
     * @param sourceList
     * @param destinationClass
     * @param <T>
     * @return
     */
    public static <T> List<T> mapList(Collection<?> sourceList, Class<T> destinationClass) {
        return sourceList.stream()
                .map(source -> getMapper().map(source, destinationClass))
                .collect(Collectors.toList());
    }


    /**
     * 将对象source的所有属性值拷贝到对象destination中.
     *
     * @param source            对象source
     * @param destinationObject destinationObject
     */
    public static void copy(Object source, Object destinationObject) {
        getMapper().map(source, destinationObject);
    }


    /**
     * 将JavaBean对象封装到Map集合当中
     *
     * @param bean
     * @return
     * @throws Exception
     */
    public static Map<String, Object> bean2Map(Object bean) {
        //创建Map集合对象
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //获取对象字节码信息,不要Object的属性
            BeanInfo beanInfo = null;
            beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
            //获取bean对象中的所有属性
            PropertyDescriptor[] list = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : list) {
                String key = pd.getName();//获取属性名
                Object value = pd.getReadMethod().invoke(bean);//调用getter()方法,获取内容
                map.put(key, value);//增加到map集合当中
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 将Map集合中的数据封装到JavaBean对象中
     *
     * @param map       集合
     * @param classType 封装javabean对象
     * @throws Exception
     */
    public static <T> T map2Bean(Map<String, Object> map, Class<T> classType) {
        //采用反射动态创建对象
        T obj = null;
        try {
            obj = classType.newInstance();
            //获取对象字节码信息,不要Object的属性
            BeanInfo beanInfo = Introspector.getBeanInfo(classType, Object.class);
            //获取bean对象中的所有属性
            PropertyDescriptor[] list = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : list) {
                String key = pd.getName();    //获取属性名
                Object value = map.get(key);  //获取属性值
                pd.getWriteMethod().invoke(obj, value);//调用属性setter()方法,设置到javabean对象当中
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
