package com.csi3370.paperstocks.web.rest;

import com.csi3370.paperstocks.Csi3370App;

import com.csi3370.paperstocks.domain.Portfolio;
import com.csi3370.paperstocks.domain.User;
import com.csi3370.paperstocks.repository.PortfolioRepository;
import com.csi3370.paperstocks.service.PortfolioService;
import com.csi3370.paperstocks.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.csi3370.paperstocks.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PortfolioResource REST controller.
 *
 * @see PortfolioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Csi3370App.class)
public class PortfolioResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPortfolioMockMvc;

    private Portfolio portfolio;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PortfolioResource portfolioResource = new PortfolioResource(portfolioService);
        this.restPortfolioMockMvc = MockMvcBuilders.standaloneSetup(portfolioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Portfolio createEntity(EntityManager em) {
        Portfolio portfolio = new Portfolio()
            .name(DEFAULT_NAME);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        portfolio.setUser(user);
        return portfolio;
    }

    @Before
    public void initTest() {
        portfolio = createEntity(em);
    }

    @Test
    @Transactional
    public void createPortfolio() throws Exception {
        int databaseSizeBeforeCreate = portfolioRepository.findAll().size();

        // Create the Portfolio
        restPortfolioMockMvc.perform(post("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolio)))
            .andExpect(status().isCreated());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeCreate + 1);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPortfolioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = portfolioRepository.findAll().size();

        // Create the Portfolio with an existing ID
        portfolio.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortfolioMockMvc.perform(post("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolio)))
            .andExpect(status().isBadRequest());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPortfolios() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get all the portfolioList
        restPortfolioMockMvc.perform(get("/api/portfolios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portfolio.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getPortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get the portfolio
        restPortfolioMockMvc.perform(get("/api/portfolios/{id}", portfolio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(portfolio.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPortfolio() throws Exception {
        // Get the portfolio
        restPortfolioMockMvc.perform(get("/api/portfolios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePortfolio() throws Exception {
        // Initialize the database
        portfolioService.save(portfolio);

        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();

        // Update the portfolio
        Portfolio updatedPortfolio = portfolioRepository.findById(portfolio.getId()).get();
        // Disconnect from session so that the updates on updatedPortfolio are not directly saved in db
        em.detach(updatedPortfolio);
        updatedPortfolio
            .name(UPDATED_NAME);

        restPortfolioMockMvc.perform(put("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPortfolio)))
            .andExpect(status().isOk());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();

        // Create the Portfolio

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortfolioMockMvc.perform(put("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolio)))
            .andExpect(status().isBadRequest());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePortfolio() throws Exception {
        // Initialize the database
        portfolioService.save(portfolio);

        int databaseSizeBeforeDelete = portfolioRepository.findAll().size();

        // Delete the portfolio
        restPortfolioMockMvc.perform(delete("/api/portfolios/{id}", portfolio.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Portfolio.class);
        Portfolio portfolio1 = new Portfolio();
        portfolio1.setId(1L);
        Portfolio portfolio2 = new Portfolio();
        portfolio2.setId(portfolio1.getId());
        assertThat(portfolio1).isEqualTo(portfolio2);
        portfolio2.setId(2L);
        assertThat(portfolio1).isNotEqualTo(portfolio2);
        portfolio1.setId(null);
        assertThat(portfolio1).isNotEqualTo(portfolio2);
    }
}
