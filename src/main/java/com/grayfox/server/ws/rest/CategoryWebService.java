package com.grayfox.server.ws.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.grayfox.server.domain.Category;
import com.grayfox.server.service.CategoryService;

import org.hibernate.validator.constraints.NotBlank;

import org.springframework.stereotype.Controller;

@Controller
@Path("categories")
public class CategoryWebService extends BaseRestComponent {

    @Inject private CategoryService categoryService;

    @GET
    @Path("like/{partialName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Category>> getCategoriesLikeName(@NotBlank(message = "category_name.required.error") @PathParam("partialName") String partialName) {
        return new Result<>(categoryService.getCategoriesLikeName(partialName, getClientLocale()));
    }
}