package com.grayfox.server.service;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.grayfox.server.dao.CategoryDao;
import com.grayfox.server.domain.Category;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    @Inject private CategoryDao categoryDao;

    @Transactional(readOnly = true)
    public List<Category> getCategoriesLikeName(String partialName, Locale locale) {
        return categoryDao.fetchLikeName(partialName, locale);
    }
}