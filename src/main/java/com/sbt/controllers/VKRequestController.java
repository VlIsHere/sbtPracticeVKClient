package com.sbt.controllers;

import com.sbt.exceptions.IdFormatException;
import com.sbt.services.IBaseService;
import com.sbt.utils.Checker;
import com.sbt.services.VKRequestService;

public class VKRequestController implements IBaseController {
    private IBaseService service;

    public VKRequestController(){
        service = new VKRequestService();
    }

    public boolean auth(String code){
        try {
            return service.auth(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getInfoById(String id) throws IdFormatException {
        Checker.checkVKID(id);
        return service.getInfoUserById(id);
    }

    public String getIdByMaxLikesOnWall(String id) throws IdFormatException{
        Checker.checkVKID(id);
        try {
            return service.getIdByMaxLikesOnWall(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
