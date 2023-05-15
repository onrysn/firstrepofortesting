package com.mobit;

/**
 * Created by Genel on 1.10.2018.
 */

public class ServerException extends MobitException {

    public ServerException(int errcode, String errmsg){
        super(errcode, errmsg);
    }

}
