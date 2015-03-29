package com.grayfox.server.dao.jdbc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

final class CypherQueries {

    // User queries
    static final String USER = "MATCH (:Credential {accessToken:{1}})<-[:HAS]-(u:User) RETURN u.name, u.lastName, u.photoUrl, u.foursquareId";
    static final String USER_FOURSQUARE_ID = "MATCH (:Credential {accessToken:{1}})<-[:HAS]-(u:User) RETURN u.foursquareId";
    static final String USER_FRIENDS = "MATCH (:User {foursquareId:{1}})-[:FRIENDS]-(friends:User) RETURN friends.name, friends.lastName, friends.photoUrl, friends.foursquareId";
    static final String USER_FRIENDS_FOURSQUARE_IDS = "MATCH (:User {foursquareId:{1}})-[:FRIENDS]-(friends:User) RETURN friends.foursquareId";
    static final String USER_LIKES = "MATCH (:User {foursquareId:{1}})-[:LIKES]->(c:Category) RETURN c.defaultName, c.iconUrl, c.foursquareId";
    static final String USER_LIKES_SPANISH = "MATCH (:User {foursquareId:{1}})-[:LIKES]->(c:Category) RETURN c.spanishName, c.iconUrl, c.foursquareId";
    static final String USER_LIKES_FOURSQUARE_IDS = "MATCH (:User {foursquareId:{1}})-[:LIKES]->(c:Category) RETURN c.foursquareId";
    static final String ARE_FRIENDS = "MATCH (:User {foursquareId:{1}})-[:FRIENDS]-(:User {foursquareId:{2}}) RETURN true";
    static final String EXISTS_USER = "MATCH (:User {foursquareId:{1}}) RETURN true";
    static final String CREATE_USER = "MATCH (c:Credential {accessToken:{1}}) CREATE (:User {name:{2}, lastName:{3}, photoUrl:{4}, foursquareId:{5}})-[:HAS]->(c)";
    static final String CREATE_FRIEND = "MATCH (me:User {foursquareId:{1}}) CREATE (:User {name:{2}, lastName:{3}, photoUrl:{4}, foursquareId:{5}})<-[:FRIENDS]-(me)";
    static final String CREATE_HAS_CREDENTIAL_RELATION = "MATCH (u:User {foursquareId:{1}}), (c:Credential {accessToken:{2}}) CREATE (u)-[:HAS]->(c)";
    static final String CREATE_LIKES_RELATION = "MATCH (u:User {foursquareId:{1}}), (c:Category {foursquareId:{2}}) CREATE (u)-[:LIKES]->(c)";
    static final String DELETE_LIKES_RELATION = "MATCH (:User {foursquareId:{1}})-[r:LIKES]->(:Category {foursquareId:{2}}) DELETE r";
    static final String CREATE_FRIENDS_RELATION = "MATCH (me:User {foursquareId:{1}}), (friend:User {foursquareId:{2}}) CREATE (me)-[:FRIENDS]->(friend)";
    static final String DELETE_FRIENDS_RELATION = "MATCH (:User {foursquareId:{1}})-[r:FRIENDS]-(:User {foursquareId:{2}}) DELETE r";
    static final String UPDATE_USER = "MATCH (u:User {foursquareId:{1}}) SET u.name = {2}, u.lastName = {3}, u.photoUrl = {4}";

    // Credential queries
    static final String CREDENTIAL = "MATCH (c:Credential {foursquareAccessToken:{1}}) RETURN c.accessToken";
    static final String EXISTS_ACCESS_TOKEN = "MATCH (:Credential {accessToken:{1}}) RETURN true";
    static final String CREATE_CREDENTIAL = "CREATE (:Credential {accessToken:{1}, foursquareAccessToken:{2}})";
    static final String DELETE_CREDENTIALS = "MATCH (:User {foursquareId:{1}})-[r:HAS]->(c:Credential) DELETE r, c";

    // Recommendations queries
    static final Map<String, String> NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED_I18N;
    static final Map<String, String> NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED_BY_FRIENDS_I18N;

    // POI queries
    static final String POIS = "MATCH (p:Poi) RETURN p.name, p.latitude, p.longitude, p.foursquareId";
    static final String NEAREAST_POIS_BY_CATEGORY = "MATCH (p:Poi)-[:IS]->(c:Category {foursquareId: {1}}) WHERE (DEGREES(ACOS((SIN(RADIANS(p.latitude)) * SIN(RADIANS({2})) + COS(RADIANS(p.latitude)) * COS(RADIANS({2})) * COS(RADIANS(p.longitude-({3}))))))*60*1.1515*1.609344*1000) <= {4} RETURN DISTINCT p.name, p.latitude, p.longitude, p.foursquareId";

    // Category queries
    static final Map<String, String> CATEGORIES_BY_POI_FOURSQUARE_ID_I18N;
    static final Map<String, String> CATEGORIES_LIKE_NAME_I18N;

    static {
        HashMap<String, String> queries = new HashMap<>();
        queries.put("default", "MATCH (:Credential {accessToken:{1}})<-[:HAS]-(:User)-[:LIKES]->(c:Category)<-[:IS]-(p:Poi) WHERE (DEGREES(ACOS((SIN(RADIANS(p.latitude)) * SIN(RADIANS({2})) + COS(RADIANS(p.latitude)) * COS(RADIANS({2})) * COS(RADIANS(p.longitude-({3}))))))*60*1.1515*1.609344*1000) <= {4} RETURN DISTINCT p.name, p.latitude, p.longitude, p.foursquareId, c.defaultName");
        queries.put("es", "MATCH (:Credential {accessToken:{1}})<-[:HAS]-(:User)-[:LIKES]->(c:Category)<-[:IS]-(p:Poi) WHERE (DEGREES(ACOS((SIN(RADIANS(p.latitude)) * SIN(RADIANS({2})) + COS(RADIANS(p.latitude)) * COS(RADIANS({2})) * COS(RADIANS(p.longitude-({3}))))))*60*1.1515*1.609344*1000) <= {4} RETURN DISTINCT p.name, p.latitude, p.longitude, p.foursquareId, c.spanishName");
        NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED_I18N = Collections.unmodifiableMap(queries);

        queries = new HashMap<>();
        queries.put("default", "MATCH (:Credential {accessToken:{1}})<-[:HAS]-(:User)-[:FRIENDS]-(u:User)-[:LIKES]->(c:Category)<-[:IS]-(p:Poi) WHERE (DEGREES(ACOS((SIN(RADIANS(p.latitude)) * SIN(RADIANS({2})) + COS(RADIANS(p.latitude)) * COS(RADIANS({2})) * COS(RADIANS(p.longitude-({3}))))))*60*1.1515*1.609344*1000) <= {4} RETURN DISTINCT p.name, p.latitude, p.longitude, p.foursquareId, u.name, u.lastName, c.defaultName");
        queries.put("es", "MATCH (:Credential {accessToken:{1}})<-[:HAS]-(:User)-[:FRIENDS]-(u:User)-[:LIKES]->(c:Category)<-[:IS]-(p:Poi) WHERE (DEGREES(ACOS((SIN(RADIANS(p.latitude)) * SIN(RADIANS({2})) + COS(RADIANS(p.latitude)) * COS(RADIANS({2})) * COS(RADIANS(p.longitude-({3}))))))*60*1.1515*1.609344*1000) <= {4} RETURN DISTINCT p.name, p.latitude, p.longitude, p.foursquareId, u.name, u.lastName, c.spanishName");
        NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED_BY_FRIENDS_I18N = Collections.unmodifiableMap(queries);
        
        queries = new HashMap<>();
        queries.put("default", "MATCH (c:Category)<-[:IS]-(:Poi {foursquareId:{1}}) RETURN c.defaultName, c.iconUrl, c.foursquareId");
        queries.put("es", "MATCH (c:Category)<-[:IS]-(:Poi {foursquareId:{1}}) RETURN c.spanishName, c.iconUrl, c.foursquareId");
        CATEGORIES_BY_POI_FOURSQUARE_ID_I18N = Collections.unmodifiableMap(queries);
        
        queries = new HashMap<>();
        queries.put("default", "MATCH (c:Category) WHERE c.defaultName =~ '(?i).*%s.*' RETURN c.defaultName, c.iconUrl, c.foursquareId LIMIT 5");
        queries.put("es", "MATCH (c:Category) WHERE c.spanishName =~ '(?i).*%s.*' RETURN c.spanishName, c.iconUrl, c.foursquareId LIMIT 5");
        CATEGORIES_LIKE_NAME_I18N = Collections.unmodifiableMap(queries);
    }

    private CypherQueries() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }

    public static String getQueryFrom(Map<String, String> i18nMap, Locale locale) {
        return i18nMap.getOrDefault(locale.getLanguage(), i18nMap.get("default"));
    }
}