package org.coral.net.akka.api;

import java.io.Serializable;
import java.util.Map;

public interface IInnerMessage extends Serializable {


     Map<String,String> getHeaders();

     void setHeaders(Map<String,String> header);

     void setHeaders(String key, String value);

    Long getAppId();

    String getMethod();

    String getTargetResourceId();

    Serializable getAppMessage();

    void setAppMessage(Serializable appMessage);

    void setMethodWithCluster(String method);

}
