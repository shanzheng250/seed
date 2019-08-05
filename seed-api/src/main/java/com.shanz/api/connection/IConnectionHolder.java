package com.shanz.api.connection;

/**
 * @ClassName:IConnectionHolder
 * @Description: 链接保存器  辅助类
 * @Author: shanzheng
 * @Date: 2019/7/10 9:19
 * @Version:1.0
 **/
public interface IConnectionHolder {

    /**
     * 功能描述 获取链接
     * @param:
     * @return:
     * @date: 2019/7/10 9:21
     */
      IConnection get();

      /**
       * 功能描述 关闭链接
       * @param:
       * @return:
       * @date: 2019/7/10 9:21
       */
      void close();
}
