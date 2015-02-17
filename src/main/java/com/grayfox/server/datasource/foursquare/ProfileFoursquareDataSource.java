package com.grayfox.server.datasource.foursquare;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import com.foursquare4j.FoursquareApi;
import com.foursquare4j.response.Group;
import com.foursquare4j.response.Result;
import com.foursquare4j.response.Venue;

import com.grayfox.server.datasource.DataSourceException;
import com.grayfox.server.datasource.ProfileDataSource;
import com.grayfox.server.domain.Category;
import com.grayfox.server.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

@Repository
public class ProfileFoursquareDataSource implements ProfileDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileFoursquareDataSource.class);

    @Inject private FoursquareApi foursquareApi;

    @Override
    public User collectUserData() {
        LOGGER.trace("Collecting user data...");
        Result<com.foursquare4j.response.User> foursquareUser = foursquareApi.getUser("self");
        if (foursquareUser.getMeta().getCode() == 200) {
            User user = new User();
            user.setName(foursquareUser.getResponse().getFirstName());
            user.setLastName(foursquareUser.getResponse().getLastName());
            String photoUrl = new StringBuilder()
                .append(foursquareUser.getResponse().getPhoto().getPrefix())
                .append("300x300")
                .append(foursquareUser.getResponse().getPhoto().getSuffix()).toString();
            user.setPhotoUrl(photoUrl);
            user.setFoursquareId(foursquareUser.getResponse().getId());
            return user;
        } else {
            LOGGER.error("Foursquare error while requesting [user/self] [code={}, errorType={}, errorDetail={}]", foursquareUser.getMeta().getCode(), foursquareUser.getMeta().getErrorType(), foursquareUser.getMeta().getErrorDetail());
            throw new DataSourceException.Builder("foursquare.request.error").build();
        }
    }

    @Override
    public Set<Category> collectLikes() {
        LOGGER.trace("Collecting user venue likes...");
        return collectLikesFrom("self");
    }

    @Override
    public Set<User> collectFriendsAndLikes() {
        LOGGER.trace("Collecting user friends...");
        Result<Group<com.foursquare4j.response.User>> foursquareFriends = foursquareApi.getUserFriends("self", 6, null);
        if (foursquareFriends.getMeta().getCode() == 200) {
            Set<User> friends = new HashSet<>();
            for (com.foursquare4j.response.User foursquareFriend : foursquareFriends.getResponse().getItems()) {
                User friend = new User();
                friend.setName(foursquareFriend.getFirstName());
                friend.setLastName(foursquareFriend.getLastName());
                String photoUrl = new StringBuilder()
                    .append(foursquareFriend.getPhoto().getPrefix())
                    .append("300x300")
                    .append(foursquareFriend.getPhoto().getSuffix()).toString();
                friend.setPhotoUrl(photoUrl);
                friend.setFoursquareId(foursquareFriend.getId());
                friend.setLikes(collectLikesFrom(foursquareFriend.getId()));
                friends.add(friend);
            }
            return friends;
        } else {
            LOGGER.error("Foursquare error while requesting [user/friends] [code={}, errorType={}, errorDetail={}]", foursquareFriends.getMeta().getCode(), foursquareFriends.getMeta().getErrorType(), foursquareFriends.getMeta().getErrorDetail());
            throw new DataSourceException.Builder("foursquare.request.error").build();
        }
    }

    @Override
    public void setAccessToken(String accessToken) {
        foursquareApi.setAccessToken(accessToken);
    }

    private Set<Category> collectLikesFrom(String userId) {
        Result<Group<Venue>> venueLikes = foursquareApi.getUserVenueLikes(userId, null, null, null, null, null);
        if (venueLikes.getMeta().getCode() == 200) {
            Set<Category> myCategories = new HashSet<>();
            for (Venue compactVenue : venueLikes.getResponse().getItems()) {
                Result<Venue> fullVenue = foursquareApi.getVenue(compactVenue.getId());
                if (fullVenue.getMeta().getCode() == 200) {
                    for (com.foursquare4j.response.Category category : fullVenue.getResponse().getCategories()) {
                        Category myCategory = new Category();
                        myCategory.setFoursquareId(category.getId());
                        myCategory.setName(category.getName());
                        myCategories.add(myCategory);
                    }
                } else {
                    LOGGER.error("Foursquare error while requesting [venue/{}] [code={}, errorType={}, errorDetail={}]", compactVenue.getId(), fullVenue.getMeta().getCode(), fullVenue.getMeta().getErrorType(), fullVenue.getMeta().getErrorDetail());
                    throw new DataSourceException.Builder("foursquare.request.error").build();
                }
            }
            return myCategories;
        } else {
            LOGGER.error("Foursquare error while requesting [user/venuelikes] [code={}, errorType={}, errorDetail={}]", venueLikes.getMeta().getCode(), venueLikes.getMeta().getErrorType(), venueLikes.getMeta().getErrorDetail());
            throw new DataSourceException.Builder("foursquare.request.error").build();
        }
    }
}