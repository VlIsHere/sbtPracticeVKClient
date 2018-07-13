package com.sbt.controllers;

import com.sbt.exceptions.IdFormatException;

public interface IBaseController {

    boolean auth(String code);

    String getInfoById(String id) throws IdFormatException;

    String getIdByMaxLikesOnWall(String id) throws IdFormatException;
}
