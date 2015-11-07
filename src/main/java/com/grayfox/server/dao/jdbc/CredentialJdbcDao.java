/*
 * Copyright 2014-2015 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
                    int columnIndex = 1;
                    credential.setId(rs.getLong(columnIndex++));
                    credential.setAccessToken(rs.getString(columnIndex++));
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
        credential.setId(getJdbcTemplate().queryForObject(getQuery("Credential.findIdByAccessToken"), Long.class, credential.getAccessToken()));
    }
}