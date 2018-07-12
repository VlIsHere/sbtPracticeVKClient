package com.sbt.controllers;

import com.sbt.exceptions.IdFormatException;
import com.sbt.services.BaseService;
import com.sbt.services.Checker;
import com.sbt.services.VKRequestService;

public class VKRequestController  {
    private BaseService service;

    public VKRequestController(){
        service = new VKRequestService();
    }

    public boolean auth(String code){
        try {
            service.auth(code);
            return true;
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
