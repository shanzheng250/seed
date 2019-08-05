package com.shanz.api.spi;

import io.netty.util.internal.StringUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/***
 *
 * 使用说明:
 *     SPILoader.getLoader(IMQClient.class).loadDefault();
 *     默认会自动加载IMQClient接口上面的@SPI的值对应的文件中的实现类，
 *     若@SPI的值有多个，会默认加载@SPIOrder中小的那个
 *
 *
 *
 *
 **/


/**
 * @ClassName:SpiLoader
 * @Description: spi 加载工具
 * @Author: shanzheng
 * @Date: 2019/6/24 16:44
 * @Version:1.0
 **/
public class SPILoader<T> {

    private final Class<?> type;

    private static Map<Class<?>,SPILoader> cacheSpiloader = new ConcurrentHashMap<>();

    private static Map<String,List<Class>> cacheSpiImpl = new ConcurrentHashMap<>();

    private final static String SPI_DIR = "META-INF/seed/";

    public SPILoader(Class<?> type) {
        this.type = type;
    }

    /**
     * 功能描述 获取classloader
     * @param:
     * @return:
     * @date: 2019/6/25 10:30
     */
    public static <T> SPILoader<T> getLoader(Class<T> c) {
        if (c == null){
            throw new IllegalArgumentException(c.getName()+" == null");
        }

        if (!c.isInterface()){
            throw new IllegalArgumentException(c.getName() + "is not a interface");
        }

        if (!isSpiAnnotationPresent(c)){
            throw new IllegalArgumentException(c.getName() + "is not a spi interface");
        }

        if (cacheSpiloader.get(c) == null){
            cacheSpiloader.putIfAbsent(c,new SPILoader(c));
        }
        return cacheSpiloader.get(c);
    }


    /**
     * 功能描述 加载指定的class对象
     * @param:
     * @return: 
     * @date: 2019/6/25 9:49
     */
    public <T> T load(String name){
        loadClasses();
        try {
            Class<T> c =  loadClass(name);
            return (T)instant(c);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * 功能描述 加载默认的适配类 从@SPI 中获取
     * @param:
     * @return:
     * @date: 2019/6/25 15:13
     */
    public <T>  T loadDefault(){
        SPI defaultSpi = type.getAnnotation(SPI.class);
        String value = defaultSpi.value();

        if (StringUtil.isNullOrEmpty(value)){
            // TODO: 2019/6/25 动态字节码生成一个默认的
            throw new IllegalArgumentException("no name: " + value +"SPI");
        }
        return  (T)load(value);

    }


    /**
     * 功能描述 获取优先加载的class
     * @param:
     * @return:
     * @date: 2019/6/25 15:14
     */
    private <T> Class<T> loadClass(String name){
        List<Class> lists = cacheSpiImpl.get(name);

        if (lists == null || lists.isEmpty()){
            // TODO: 2019/6/25 动态字节码生成一个默认的
            throw new IllegalArgumentException("no name: " + name +"SPI");
        }

        lists.sort((o1,o2)->{
            SPIOrder spi1 = (SPIOrder) o1.getAnnotation(SPIOrder.class);
            SPIOrder spi2 = (SPIOrder) o2.getAnnotation(SPIOrder.class);
            int order1 = spi1 == null ? 0 : spi1.order();
            int order2 = spi2 == null ? 0 : spi2.order();
            return order1 - order2;
        });

        return lists.get(0);
    }


    /**
     * 功能描述 从固定目录下读取所有的实现文件
     * @param:
     * @return:
     * @date: 2019/6/25 11:02
     */
    private void loadClasses() {
        String fileName = SPI_DIR + type.getName();
        ClassLoader classLoader = SPILoader.class.getClassLoader();
        Enumeration<URL> urls;
        try {

            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                urls = ClassLoader.getSystemResources(fileName);
            }
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    java.net.URL url = urls.nextElement();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"))) {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (line.length() > 0 && line.indexOf("=") != -1) {
                                String aliname = line.substring(0, line.indexOf("=")).trim();
                                String classname = line.substring(line.indexOf("=")+1, line.length()).trim();
                                Class<?> c = Class.forName(classname);

                                if (cacheSpiImpl.containsKey(aliname)){
                                    cacheSpiImpl.get(aliname).add(c);
                                }else {
                                    cacheSpiImpl.computeIfAbsent(aliname, s -> new ArrayList<>()).add(c);
                                }
                            }

                        }
                    }
                }
            }

        } catch (Exception e){

        }

    }

    /**
     * 功能描述 实例化对象
     * @param:
     * @return:
     * @date: 2019/6/25 15:14
     */
    private <T> T instant(Class<T> c) throws IllegalAccessException, InstantiationException {
        return c.newInstance();
    }


    private static boolean isSpiAnnotationPresent(Class c){
        return c.isAnnotationPresent(SPI.class);
    }

}
