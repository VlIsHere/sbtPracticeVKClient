package com.sbt.services;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

public interface BaseService {

    void auth(String codeForAuth) throws ClientException, ApiException;

    String getInfoUserById(String id);

    String getIdByMaxLikesOnWall(String id) throws ClientException, ApiException;
}
