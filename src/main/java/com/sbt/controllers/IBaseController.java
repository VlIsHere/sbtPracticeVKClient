package com.sbt.controllers;

import com.sbt.exceptions.IdFormatException;

public interface IBaseController {

    public boolean auth(String code);

    public String getInfoById(String id) throws IdFormatException;

    public String getIdByMaxLikesOnWall(String id) throws IdFormatException;
}
