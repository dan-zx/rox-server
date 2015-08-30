package com.grayfox.server.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.grayfox.server.domain.Category;
import com.grayfox.server.test.config.TestConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TransactionConfiguration(defaultRollback = true)
public class CategoryDaoTest {

    @Inject private CategoryDao categoryDao;

    @Before
    public void setUp() {
        assertThat(categoryDao).isNotNull();
    }

    @Test
    @Transactional
    public void testFetchLikeName() {
        Category c1 = new Category();
        c1.setFoursquareId("4bf58dd8d48988d188941735");
        c1.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/default_88.png");
        c1.setName("Estadio de fútbol");
        
        Category c2 = new Category();
        c2.setFoursquareId("4bf58dd8d48988d18c941735");
        c2.setIconUrl("https://ss3.4sqi.net/img/categories_v2/arts_entertainment/default_88.png");
        c2.setName("Estadio de béisbol");

        List<Category> expectedCategories = Arrays.asList(c1, c2);
        
        assertThat(categoryDao.fetchLikeName("estadio", Locale.ROOT)).isNotNull().isNotEmpty().containsOnlyElementsOf(expectedCategories);
    }
}