package org.coral.net.akka.api;

import java.io.Serializable;

public interface IRoute extends Serializable, IInnerMessage {

    IInnerMessage getRouter();
}
