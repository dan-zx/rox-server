package com.grayfox.server.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.grayfox.server.data.dao.AppUserDao;
import com.grayfox.server.service.ExploreVenuesService;
import com.grayfox.server.util.Strings;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.Recommendation;
import fi.foyt.foursquare.api.entities.RecommendationGroup;
import fi.foyt.foursquare.api.entities.Recommended;

import org.springframework.transaction.annotation.Transactional;

@Named
public class ExploreVenuesServiceImpl implements ExploreVenuesService, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(ExploreVenuesServiceImpl.class);

    private AppUserDao appUserDao;
    private FoursquareApi foursquareApi;

    @Override
    @Transactional(noRollbackFor = ServiceException.class)
    public List<CompactVenue> recommendVenues(Long id, String latLng) {
        String accessToken = appUserDao.fetchAccessTokenById(id);
        if (Strings.isNullOrBlank(accessToken)) throw new ServiceException("Unknown user");
        foursquareApi.setoAuthToken(accessToken);
        try {
            Result<Recommended> response = foursquareApi.venuesExplore(latLng, null, null, null, null, null, null, null, null);
            LOG.debug("Foursquare response: [code={}, errorType={}, errorDetail={}]", response.getMeta().getCode(), response.getMeta().getErrorType(), response.getMeta().getErrorDetail());
            if (response.getMeta().getCode() == 200) {
                for (RecommendationGroup g : response.getResult().getGroups()) {
                    if (g.getType().equals("Recommended Places")) {
                        ArrayList<CompactVenue> result = new ArrayList<>(g.getItems().length);
                        for (Recommendation r : g.getItems()) result.add(r.getVenue());
                        return result;
                    }

                }
            }
        } catch (FoursquareApiException ex) {
            throw new ServiceException("Foursquare error", ex);
        }
        return null;
    }

    @Inject
    public void setAppUserDao(AppUserDao appUserDao) {
        this.appUserDao = appUserDao;
    }

    @Inject
    public void setFoursquareApi(FoursquareApi foursquareApi) {
        this.foursquareApi = foursquareApi;
    }
}