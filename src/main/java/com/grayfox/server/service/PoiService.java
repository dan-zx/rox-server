package com.grayfox.server.service;

import java.util.List;

import javax.inject.Inject;

import com.grayfox.server.dao.PoiDao;
import com.grayfox.server.domain.Poi;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PoiService {

    @Inject private PoiDao poiDao;

    @Transactional(readOnly = true)
    public List<Poi> getPois() {
        return poiDao.fetchAll();
    }
}