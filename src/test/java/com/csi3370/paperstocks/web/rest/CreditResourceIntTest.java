package com.csi3370.paperstocks.web.rest;

import com.csi3370.paperstocks.Csi3370App;

import com.csi3370.paperstocks.domain.Credit;
import com.csi3370.paperstocks.repository.CreditRepository;
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
 * Test class for the CreditResource REST controller.
 *
 * @see CreditResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Csi3370App.class)
public class CreditResourceIntTest {

    private static final Double DEFAULT_CREDIT = 1D;
    private static final Double UPDATED_CREDIT = 2D;

    @Autowired
    private CreditRepository creditRepository;

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

    private MockMvc restCreditMockMvc;

    private Credit credit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CreditResource creditResource = new CreditResource(creditRepository);
        this.restCreditMockMvc = MockMvcBuilders.standaloneSetup(creditResource)
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
    public static Credit createEntity(EntityManager em) {
        Credit credit = new Credit()
            .credit(DEFAULT_CREDIT);
        return credit;
    }

    @Before
    public void initTest() {
        credit = createEntity(em);
    }

    @Test
    @Transactional
    public void createCredit() throws Exception {
        int databaseSizeBeforeCreate = creditRepository.findAll().size();

        // Create the Credit
        restCreditMockMvc.perform(post("/api/credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(credit)))
            .andExpect(status().isCreated());

        // Validate the Credit in the database
        List<Credit> creditList = creditRepository.findAll();
        assertThat(creditList).hasSize(databaseSizeBeforeCreate + 1);
        Credit testCredit = creditList.get(creditList.size() - 1);
        assertThat(testCredit.getCredit()).isEqualTo(DEFAULT_CREDIT);
    }

    @Test
    @Transactional
    public void createCreditWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = creditRepository.findAll().size();

        // Create the Credit with an existing ID
        credit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditMockMvc.perform(post("/api/credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(credit)))
            .andExpect(status().isBadRequest());

        // Validate the Credit in the database
        List<Credit> creditList = creditRepository.findAll();
        assertThat(creditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCredits() throws Exception {
        // Initialize the database
        creditRepository.saveAndFlush(credit);

        // Get all the creditList
        restCreditMockMvc.perform(get("/api/credits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(credit.getId().intValue())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCredit() throws Exception {
        // Initialize the database
        creditRepository.saveAndFlush(credit);

        // Get the credit
        restCreditMockMvc.perform(get("/api/credits/{id}", credit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(credit.getId().intValue()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCredit() throws Exception {
        // Get the credit
        restCreditMockMvc.perform(get("/api/credits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCredit() throws Exception {
        // Initialize the database
        creditRepository.saveAndFlush(credit);

        int databaseSizeBeforeUpdate = creditRepository.findAll().size();

        // Update the credit
        Credit updatedCredit = creditRepository.findById(credit.getId()).get();
        // Disconnect from session so that the updates on updatedCredit are not directly saved in db
        em.detach(updatedCredit);
        updatedCredit
            .credit(UPDATED_CREDIT);

        restCreditMockMvc.perform(put("/api/credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCredit)))
            .andExpect(status().isOk());

        // Validate the Credit in the database
        List<Credit> creditList = creditRepository.findAll();
        assertThat(creditList).hasSize(databaseSizeBeforeUpdate);
        Credit testCredit = creditList.get(creditList.size() - 1);
        assertThat(testCredit.getCredit()).isEqualTo(UPDATED_CREDIT);
    }

    @Test
    @Transactional
    public void updateNonExistingCredit() throws Exception {
        int databaseSizeBeforeUpdate = creditRepository.findAll().size();

        // Create the Credit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditMockMvc.perform(put("/api/credits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(credit)))
            .andExpect(status().isBadRequest());

        // Validate the Credit in the database
        List<Credit> creditList = creditRepository.findAll();
        assertThat(creditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCredit() throws Exception {
        // Initialize the database
        creditRepository.saveAndFlush(credit);

        int databaseSizeBeforeDelete = creditRepository.findAll().size();

        // Delete the credit
        restCreditMockMvc.perform(delete("/api/credits/{id}", credit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Credit> creditList = creditRepository.findAll();
        assertThat(creditList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Credit.class);
        Credit credit1 = new Credit();
        credit1.setId(1L);
        Credit credit2 = new Credit();
        credit2.setId(credit1.getId());
        assertThat(credit1).isEqualTo(credit2);
        credit2.setId(2L);
        assertThat(credit1).isNotEqualTo(credit2);
        credit1.setId(null);
        assertThat(credit1).isNotEqualTo(credit2);
    }
}
