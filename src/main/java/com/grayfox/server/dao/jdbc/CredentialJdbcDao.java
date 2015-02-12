package com.grayfox.server.dao.jdbc;

import java.sql.ResultSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;

import com.grayfox.server.dao.CredentialDao;
import com.grayfox.server.domain.Credential;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CredentialJdbcDao implements CredentialDao {

    @Inject private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void onPostConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Credential fetchByFoursquareAccessToken(String foursquareAccessToken) {
        List<Credential> credentials = jdbcTemplate.query(CypherQueries.CREDENTIAL_BY_FOURSQUARE_ACCESS_TOKEN, 
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
        List<Boolean> exists = jdbcTemplate.queryForList(CypherQueries.EXISTS_ACCESS_TOKEN, Boolean.class, accessToken);
        return !exists.isEmpty();
    }

    @Override
    public void create(Credential credential) {
        jdbcTemplate.update(CypherQueries.CREATE_CREDENTIAL, credential.getAccessToken(), credential.getFoursquareAccessToken());
    }
}