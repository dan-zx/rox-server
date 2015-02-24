package com.grayfox.server.dao.jdbc;

final class CypherQueries {

    // User queries
    static final String COMPACT_USER_BY_ACCESS_TOKEN = "MATCH (:Credential {accessToken:{1}})<-[:HAS]-(u:User) RETURN u.name, u.lastName, u.photoUrl, u.foursquareId";
    static final String CREATE_USER = "MATCH (c:Credential {accessToken:{1}}) CREATE (:User {name:{2}, lastName:{3}, photoUrl:{4}, foursquareId:{5}})-[:HAS]->(c)";
    static final String CREATE_FRIEND = "MATCH (me:User {foursquareId:{1}}) CREATE (:User {name:{2}, lastName:{3}, photoUrl:{4}, foursquareId:{5}})<-[:FRIENDS]-(me)";
    static final String CREATE_LIKES_RELATION = "MATCH (u:User {foursquareId:{1}}), (c:Category {foursquareId:{2}}) CREATE (u)-[:LIKES]->(c)";
    static final String CREATE_FRIENDS_RELATION = "MATCH (me:User {foursquareId:{1}}), (friend:User {foursquareId:{2}}) CREATE (me)-[:FRIENDS]->(friend)";
    static final String UPDATE_USER = "MATCH (u:User {foursquareId:{1}}) SET u.name = {2}, u.lastName = {3}, u.photoUrl = {4}";
    static final String EXISTS_USER = "MATCH (:User {foursquareId:{1}}) RETURN true";

    // Credential queries
    static final String CREATE_CREDENTIAL = "CREATE (:Credential {accessToken:{1}, foursquareAccessToken:{2}})";
    static final String EXISTS_ACCESS_TOKEN = "MATCH (:Credential {accessToken:{1}}) RETURN true";
    static final String CREDENTIAL_BY_FOURSQUARE_ACCESS_TOKEN = "MATCH (c:Credential {foursquareAccessToken:{1}}) RETURN c.accessToken";

    // Recommendations queries
    static final String NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED = "MATCH (:Credential {accessToken:{1}})<-[:HAS]-(:User)-[:LIKES]->(c:Category)<-[:IS]-(p:Poi) WHERE (DEGREES(ACOS((SIN(RADIANS(p.latitude)) * SIN(RADIANS({2})) + COS(RADIANS(p.latitude)) * COS(RADIANS({2})) * COS(RADIANS(p.longitude-({3}))))))*60*1.1515*1.609344*1000) <= {4} RETURN DISTINCT p.name, p.latitude, p.longitude, p.foursquareId, c.defaultName";
    static final String NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED_SPANISH = "MATCH (:Credential {accessToken:{1}})<-[:HAS]-(:User)-[:LIKES]->(c:Category)<-[:IS]-(p:Poi) WHERE (DEGREES(ACOS((SIN(RADIANS(p.latitude)) * SIN(RADIANS({2})) + COS(RADIANS(p.latitude)) * COS(RADIANS({2})) * COS(RADIANS(p.longitude-({3}))))))*60*1.1515*1.609344*1000) <= {4} RETURN DISTINCT p.name, p.latitude, p.longitude, p.foursquareId, c.spanishName";
    static final String NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED_BY_FRIENDS = "MATCH (:Credential {accessToken:{1}})<-[:HAS]-(:User)-[:FRIENDS]-(u:User)-[:LIKES]->(c:Category)<-[:IS]-(p:Poi) WHERE (DEGREES(ACOS((SIN(RADIANS(p.latitude)) * SIN(RADIANS({2})) + COS(RADIANS(p.latitude)) * COS(RADIANS({2})) * COS(RADIANS(p.longitude-({3}))))))*60*1.1515*1.609344*1000) <= {4} RETURN DISTINCT p.name, p.latitude, p.longitude, p.foursquareId, u.name, u.lastName, c.defaultName";
    static final String NEAREAST_RECOMMENDATIONS_BY_CATEGORIES_LIKED_BY_FRIENDS_SPANISH = "MATCH (:Credential {accessToken:{1}})<-[:HAS]-(:User)-[:FRIENDS]-(u:User)-[:LIKES]->(c:Category)<-[:IS]-(p:Poi) WHERE (DEGREES(ACOS((SIN(RADIANS(p.latitude)) * SIN(RADIANS({2})) + COS(RADIANS(p.latitude)) * COS(RADIANS({2})) * COS(RADIANS(p.longitude-({3}))))))*60*1.1515*1.609344*1000) <= {4} RETURN DISTINCT p.name, p.latitude, p.longitude, p.foursquareId, u.name, u.lastName, c.spanishName";

    // Category queries
    static final String CATEGORIES_BY_POI_FOURSQUARE_ID = "MATCH (c:Category)<-[:IS]-(:Poi {foursquareId:{1}}) return c.defaultName, c.iconUrl, c.foursquareId";
    static final String CATEGORIES_BY_POI_FOURSQUARE_ID_SPANISH = "MATCH (c:Category)<-[:IS]-(:Poi {foursquareId:{1}}) return c.spanishName, c.iconUrl, c.foursquareId";

    private CypherQueries() {
        throw new IllegalAccessError("This class cannot be instantiated nor extended");
    }
}