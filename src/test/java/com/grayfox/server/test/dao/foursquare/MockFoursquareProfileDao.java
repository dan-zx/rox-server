package com.grayfox.server.test.dao.foursquare;

import com.grayfox.server.dao.foursquare.FoursquareProfileDao;

import com.grayfox.server.domain.User;
import org.springframework.stereotype.Repository;

@Repository("foursquareProfileDao")
public class MockFoursquareProfileDao extends FoursquareProfileDao {

    @Override
    public User collectUserData(String accessToken) {
        return null;
    }
}