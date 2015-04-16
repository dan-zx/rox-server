package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.List;

import com.grayfox.server.dao.CredentialDao;
import com.grayfox.server.dao.DaoException;
import com.grayfox.server.domain.Credential;

import org.springframework.stereotype.Repository;

@Repository("credentialLocalDao")
public class CredentialJdbcDao extends JdbcDao implements CredentialDao {

    @Override
    public Credential fetchByFoursquareAccessToken(String foursquareAccessToken) {
        List<Credential> credentials = getJdbcTemplate().query(getQuery("credentialByFoursquareAccessToken"), 
                (ResultSet rs, int i) -> {
                    Credential credential = new Credential();
                    credential.setAccessToken(rs.getString(1));
                    credential.setFoursquareAccessToken(foursquareAccessToken);
                    return credential;
                }, 
                foursquareAccessToken);
        if (credentials.size() > 1) {
            throw new DaoException.Builder()
                .messageKey("data.integrity.error")
                .build();
        }
        return credentials.isEmpty() ? null : credentials.get(0);
    }

    @Override
    public boolean existsAccessToken(String accessToken) {
        List<Boolean> exists = getJdbcTemplate().queryForList(getQuery("existsAccessToken"), Boolean.class, accessToken);
        return !exists.isEmpty();
    }

    @Override
    public void save(Credential credential) {
        getJdbcTemplate().update(getQuery("createCredential"), credential.getAccessToken(), credential.getFoursquareAccessToken());
    }
}