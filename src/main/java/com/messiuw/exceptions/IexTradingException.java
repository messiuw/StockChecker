package com.messiuw.exceptions;

import com.messiuw.request.HttpConnectionManager;

/**
 * Created by matthias.polkehn on 08.03.2018.
 */
public class IexTradingException extends Exception{

    public IexTradingException(String message) {
        super(message);
        HttpConnectionManager.getInstance().disconnectAllConnections();
    }

}
