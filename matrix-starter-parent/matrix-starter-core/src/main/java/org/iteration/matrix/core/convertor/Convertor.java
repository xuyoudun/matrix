package org.iteration.matrix.core.convertor;


import org.dozer.DozerBeanMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.function.Function;


public interface Convertor {

    static DozerBeanMapper mapper = new DozerBeanMapper();

    default <T> T convertTo(Class<T> destin) {
        return mapper.map(this, destin);
    }

    static <S, T> List<T> convertList(final List<S> listSource, Function<S, T> fn) {
        /* null和empty含义不同，null通常不做处理，empty表示修改为空*/
        if (listSource == null) {
            return null;
        }
        if (listSource.size() == 0) {
            return Collections.emptyList();
        }
        // return listSource.stream().map(s -> fn.apply(s)).collect(Collectors.toList());
        List<T> listResult = new ArrayList<>(listSource.size());
        for (S object : listSource) {
            T instance = fn.apply(object);
            listResult.add(instance);
        }
        return listResult;
    }

    static <S, T> List<T> convertList(final List<S> listSource, final Class<T> destin) {
        return convertList(listSource, (s) -> mapper.map(s, destin));
    }

    static <S, T> Page<T> convertPage(final Page<S> pageSource, Function<S, T> f) {
        List<T> list = new ArrayList<>(pageSource.getContent().size());
        for (S object : pageSource.getContent()) {
            T instance = f.apply(object);
            list.add(instance);
        }
        return new PageImpl<>(list, pageSource.getPageable(), pageSource.getTotalPages());
    }

    static <S, T> Page<T> convertPage(final Page<S> pageSource, final Class<T> destin) {
        return convertPage(pageSource, (s) -> mapper.map(s, destin));
    }

    /**
     * 将JavaBean对象封装到Map集合当中
     *
     * @param objSource
     * @return
     * @throws Exception
     */
    static Map<String, Object> convertMap(Object objSource) {
        //创建Map集合对象
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            //获取对象字节码信息,不要Object的属性
            BeanInfo beanInfo = null;
            beanInfo = Introspector.getBeanInfo(objSource.getClass(), Object.class);
            //获取bean对象中的所有属性
            PropertyDescriptor[] list = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : list) {
                String key = pd.getName();//获取属性名
                Object value = pd.getReadMethod().invoke(objSource);//调用getter()方法,获取内容
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
     * @param map    集合
     * @param destin 封装javabean对象
     * @throws Exception
     */
    static <T> T convertBean(Map<String, Object> map, Class<T> destin) {
        //采用反射动态创建对象
        T obj = null;
        try {
            obj = destin.newInstance();
            //获取对象字节码信息,不要Object的属性
            BeanInfo beanInfo = Introspector.getBeanInfo(destin, Object.class);
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

    static void copyProperties(Object source, Object destination) {
        mapper.map(source, destination);
    }

}
