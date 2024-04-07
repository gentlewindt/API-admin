package com.gentlewind.apicommon.service;


/**
* @author gentlewind

*/
public interface InnerUserInterfaceInfoService  {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);


}
