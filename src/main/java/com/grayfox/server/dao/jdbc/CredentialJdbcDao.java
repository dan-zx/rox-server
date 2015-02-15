package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.List;

import com.grayfox.server.dao.CredentialDao;
import com.grayfox.server.domain.Credential;

import org.springframework.stereotype.Repository;

@Repository
public class CredentialJdbcDao extends JdbcDao implements CredentialDao {

    @Override
    public Credential fetchByFoursquareAccessToken(String foursquareAccessToken) {
        List<Credential> credentials = getJdbcTemplate().query(CypherQueries.CREDENTIAL_BY_FOURSQUARE_ACCESS_TOKEN, 
                (ResultSet rs, int i) -> {
                    Credential credential = new Credential();
                    credential.setAccessToken(rs.getString(1));
                    credential.setFoursquareAccessToken(foursquareAccessToken);
                    return credential;
                }, 
                foursquareAccessToken);
        return credentials.isEmpty() ? null : credentials.get(0);
    }

    @Override
    public boolean existsAccessToken(String accessToken) {
        List<Boolean> exists = getJdbcTemplate().queryForList(CypherQueries.EXISTS_ACCESS_TOKEN, Boolean.class, accessToken);
        return !exists.isEmpty();
    }

    @Override
    public void create(Credential credential) {
        getJdbcTemplate().update(CypherQueries.CREATE_CREDENTIAL, credential.getAccessToken(), credential.getFoursquareAccessToken());
    }
}