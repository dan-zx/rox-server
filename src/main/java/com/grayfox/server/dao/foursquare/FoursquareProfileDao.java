package com.grayfox.server.dao.foursquare;

import java.util.HashSet;
import java.util.Set;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.Group;
import com.foursquare4j.response.Result;
import com.foursquare4j.response.Venue;

import com.grayfox.server.dao.DaoException;
import com.grayfox.server.dao.SocialNetworkProfileDao;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class FoursquareProfileDao implements SocialNetworkProfileDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(FoursquareProfileDao.class);

    @Value("${foursquare.app.client.id}")     private String clientId; 
    @Value("${foursquare.app.client.secret}") private String clientSecret;

    @Override
    public User collectUserData(String accessToken) {
        LOGGER.trace("Collecting user data...");
        FoursquareApi foursquareApi = new FoursquareApi(clientId, clientSecret);
        foursquareApi.setAccessToken(accessToken);
        Result<com.foursquare4j.response.User> foursquareUser = foursquareApi.getUser("self");
        if (foursquareUser.getMeta().getCode() == 200) {
            User user = toUser(foursquareUser.getResponse());
            user.setLikes(collectLikesFrom("self", foursquareApi));
            Result<Group<com.foursquare4j.response.User>> foursquareFriends = foursquareApi.getUserFriends("self", 500, null);
            if (foursquareFriends.getMeta().getCode() == 200) {
                Set<User> friends = new HashSet<>();
                for (com.foursquare4j.response.User foursquareFriend : foursquareFriends.getResponse().getItems()) {
                    User friend = toUser(foursquareFriend);
                    friend.setLikes(collectLikesFrom(foursquareFriend.getId(), foursquareApi));
                    friends.add(friend);
                }
                user.setFriends(friends);
            } else {
                LOGGER.error("Foursquare error while requesting [user/friends] [code={}, errorType={}, errorDetail={}]", foursquareFriends.getMeta().getCode(), foursquareFriends.getMeta().getErrorType(), foursquareFriends.getMeta().getErrorDetail());
                throw new DaoException.Builder("foursquare.request.error").build();
            }
            LOGGER.trace("Done");
            return user;
        } else {
            LOGGER.error("Foursquare error while requesting [user/self] [code={}, errorType={}, errorDetail={}]", foursquareUser.getMeta().getCode(), foursquareUser.getMeta().getErrorType(), foursquareUser.getMeta().getErrorDetail());
            throw new DaoException.Builder("foursquare.request.error").build();
        }
    }

    private User toUser(com.foursquare4j.response.User foursquareUser) {
        User user = new User();
        user.setName(foursquareUser.getFirstName());
        user.setLastName(foursquareUser.getLastName());
        String photoUrl = new StringBuilder()
            .append(foursquareUser.getPhoto().getPrefix())
            .append("300x300")
            .append(foursquareUser.getPhoto().getSuffix()).toString();
        user.setPhotoUrl(photoUrl);
        user.setFoursquareId(foursquareUser.getId());
        return user;
    }

    private Set<Category> collectLikesFrom(String userId, FoursquareApi foursquareApi) {
        Result<Group<Venue>> venueLikes = foursquareApi.getUserVenueLikes(userId, null, null, null, null, null);
        if (venueLikes.getMeta().getCode() == 200) {
            Set<Category> myCategories = new HashSet<>();
            for (Venue venue : venueLikes.getResponse().getItems()) {
                for (com.foursquare4j.response.Category category : venue.getCategories()) {
                    Category myCategory = new Category();
                    myCategory.setFoursquareId(category.getId());
                    myCategories.add(myCategory);
                }
            }
            return myCategories;
        } else {
            LOGGER.error("Foursquare error while requesting [user/venuelikes] [code={}, errorType={}, errorDetail={}]", venueLikes.getMeta().getCode(), venueLikes.getMeta().getErrorType(), venueLikes.getMeta().getErrorDetail());
            throw new DaoException.Builder("foursquare.request.error").build();
        }
    }
}