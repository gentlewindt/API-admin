package com.gentlewind.apicommon.service;
import com.gentlewind.apicommon.model.entity.InterfaceInfo;

/**
* @author gentlewind

*/
public interface InnerInterfaceInfoService  {

    /**
     *  从数据库中查询接口是否存在
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path , String method);

}
