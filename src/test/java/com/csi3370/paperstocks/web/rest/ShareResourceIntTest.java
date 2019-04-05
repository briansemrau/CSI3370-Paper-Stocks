package com.csi3370.paperstocks.web.rest;

import com.csi3370.paperstocks.Csi3370App;

import com.csi3370.paperstocks.domain.Share;
import com.csi3370.paperstocks.domain.Portfolio;
import com.csi3370.paperstocks.repository.ShareRepository;
import com.csi3370.paperstocks.service.ShareService;
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
 * Test class for the ShareResource REST controller.
 *
 * @see ShareResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Csi3370App.class)
public class ShareResourceIntTest {

    private static final String DEFAULT_TICKER = "AAAAAAAAAA";
    private static final String UPDATED_TICKER = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private ShareService shareService;

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

    private MockMvc restShareMockMvc;

    private Share share;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShareResource shareResource = new ShareResource(shareService);
        this.restShareMockMvc = MockMvcBuilders.standaloneSetup(shareResource)
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
    public static Share createEntity(EntityManager em) {
        Share share = new Share()
            .ticker(DEFAULT_TICKER)
            .quantity(DEFAULT_QUANTITY);
        // Add required entity
        Portfolio portfolio = PortfolioResourceIntTest.createEntity(em);
        em.persist(portfolio);
        em.flush();
        share.setPortfolio(portfolio);
        return share;
    }

    @Before
    public void initTest() {
        share = createEntity(em);
    }

    @Test
    @Transactional
    public void createShare() throws Exception {
        int databaseSizeBeforeCreate = shareRepository.findAll().size();

        // Create the Share
        restShareMockMvc.perform(post("/api/shares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(share)))
            .andExpect(status().isCreated());

        // Validate the Share in the database
        List<Share> shareList = shareRepository.findAll();
        assertThat(shareList).hasSize(databaseSizeBeforeCreate + 1);
        Share testShare = shareList.get(shareList.size() - 1);
        assertThat(testShare.getTicker()).isEqualTo(DEFAULT_TICKER);
        assertThat(testShare.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createShareWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shareRepository.findAll().size();

        // Create the Share with an existing ID
        share.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShareMockMvc.perform(post("/api/shares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(share)))
            .andExpect(status().isBadRequest());

        // Validate the Share in the database
        List<Share> shareList = shareRepository.findAll();
        assertThat(shareList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTickerIsRequired() throws Exception {
        int databaseSizeBeforeTest = shareRepository.findAll().size();
        // set the field null
        share.setTicker(null);

        // Create the Share, which fails.

        restShareMockMvc.perform(post("/api/shares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(share)))
            .andExpect(status().isBadRequest());

        List<Share> shareList = shareRepository.findAll();
        assertThat(shareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = shareRepository.findAll().size();
        // set the field null
        share.setQuantity(null);

        // Create the Share, which fails.

        restShareMockMvc.perform(post("/api/shares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(share)))
            .andExpect(status().isBadRequest());

        List<Share> shareList = shareRepository.findAll();
        assertThat(shareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShares() throws Exception {
        // Initialize the database
        shareRepository.saveAndFlush(share);

        // Get all the shareList
        restShareMockMvc.perform(get("/api/shares?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(share.getId().intValue())))
            .andExpect(jsonPath("$.[*].ticker").value(hasItem(DEFAULT_TICKER.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @Test
    @Transactional
    public void getShare() throws Exception {
        // Initialize the database
        shareRepository.saveAndFlush(share);

        // Get the share
        restShareMockMvc.perform(get("/api/shares/{id}", share.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(share.getId().intValue()))
            .andExpect(jsonPath("$.ticker").value(DEFAULT_TICKER.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingShare() throws Exception {
        // Get the share
        restShareMockMvc.perform(get("/api/shares/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShare() throws Exception {
        // Initialize the database
        shareService.save(share);

        int databaseSizeBeforeUpdate = shareRepository.findAll().size();

        // Update the share
        Share updatedShare = shareRepository.findById(share.getId()).get();
        // Disconnect from session so that the updates on updatedShare are not directly saved in db
        em.detach(updatedShare);
        updatedShare
            .ticker(UPDATED_TICKER)
            .quantity(UPDATED_QUANTITY);

        restShareMockMvc.perform(put("/api/shares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShare)))
            .andExpect(status().isOk());

        // Validate the Share in the database
        List<Share> shareList = shareRepository.findAll();
        assertThat(shareList).hasSize(databaseSizeBeforeUpdate);
        Share testShare = shareList.get(shareList.size() - 1);
        assertThat(testShare.getTicker()).isEqualTo(UPDATED_TICKER);
        assertThat(testShare.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingShare() throws Exception {
        int databaseSizeBeforeUpdate = shareRepository.findAll().size();

        // Create the Share

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareMockMvc.perform(put("/api/shares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(share)))
            .andExpect(status().isBadRequest());

        // Validate the Share in the database
        List<Share> shareList = shareRepository.findAll();
        assertThat(shareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShare() throws Exception {
        // Initialize the database
        shareService.save(share);

        int databaseSizeBeforeDelete = shareRepository.findAll().size();

        // Delete the share
        restShareMockMvc.perform(delete("/api/shares/{id}", share.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Share> shareList = shareRepository.findAll();
        assertThat(shareList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Share.class);
        Share share1 = new Share();
        share1.setId(1L);
        Share share2 = new Share();
        share2.setId(share1.getId());
        assertThat(share1).isEqualTo(share2);
        share2.setId(2L);
        assertThat(share1).isNotEqualTo(share2);
        share1.setId(null);
        assertThat(share1).isNotEqualTo(share2);
    }
}
