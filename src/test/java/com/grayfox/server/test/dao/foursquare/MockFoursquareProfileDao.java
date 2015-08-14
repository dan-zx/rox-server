package com.grayfox.server.test.dao.foursquare;

import javax.inject.Inject;

import com.grayfox.server.test.util.HttpStatus;
import com.grayfox.server.test.util.Utils;
import com.squareup.okhttp.mockwebserver.MockResponse;

import com.foursquare4j.FoursquareApi;
import com.grayfox.server.dao.foursquare.FoursquareProfileDao;
import com.grayfox.server.domain.User;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.springframework.stereotype.Repository;

@Repository("foursquareProfileDao")
public class MockFoursquareProfileDao extends FoursquareProfileDao {

    @Inject private MockWebServer mockWebServer;
    @Inject private FoursquareApi foursquareApi;

    @Override
    public User collectUserData(String accessToken) {
        if (!"fakeToken".equals(accessToken)) {
            mockWebServer.enqueue(new MockResponse()
                .setStatus(HttpStatus.OK.toString())
                .setBody(Utils.getContentFromFileInClasspath("responses/generic_error.json")));
        } else {
            mockWebServer.enqueue(new MockResponse()
                .setStatus(HttpStatus.OK.toString())
                .setBody(Utils.getContentFromFileInClasspath("responses/user.json")));
            mockWebServer.enqueue(new MockResponse()
                .setStatus(HttpStatus.OK.toString())
                .setBody(Utils.getContentFromFileInClasspath("responses/venuelikes_self.json")));
            mockWebServer.enqueue(new MockResponse()
                .setStatus(HttpStatus.OK.toString())
                .setBody(Utils.getContentFromFileInClasspath("responses/friends.json")));
            mockWebServer.enqueue(new MockResponse()
                .setStatus(HttpStatus.OK.toString())
                .setBody(Utils.getContentFromFileInClasspath("responses/venuelikes_friends.json")));
        }
        return collectUserData(foursquareApi);
    }
}