package com.softala.sr2.web.rest;

import static com.softala.sr2.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.softala.sr2.IntegrationTest;
import com.softala.sr2.domain.StockItemTypeCompany;
import com.softala.sr2.repository.StockItemTypeCompanyRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StockItemTypeCompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockItemTypeCompanyResourceIT {

    private static final BigDecimal DEFAULT_TYPE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TYPE_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/stock-item-type-companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StockItemTypeCompanyRepository stockItemTypeCompanyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockItemTypeCompanyMockMvc;

    private StockItemTypeCompany stockItemTypeCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockItemTypeCompany createEntity(EntityManager em) {
        StockItemTypeCompany stockItemTypeCompany = new StockItemTypeCompany().typePrice(DEFAULT_TYPE_PRICE);
        return stockItemTypeCompany;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockItemTypeCompany createUpdatedEntity(EntityManager em) {
        StockItemTypeCompany stockItemTypeCompany = new StockItemTypeCompany().typePrice(UPDATED_TYPE_PRICE);
        return stockItemTypeCompany;
    }

    @BeforeEach
    public void initTest() {
        stockItemTypeCompany = createEntity(em);
    }

    @Test
    @Transactional
    void createStockItemTypeCompany() throws Exception {
        int databaseSizeBeforeCreate = stockItemTypeCompanyRepository.findAll().size();
        // Create the StockItemTypeCompany
        restStockItemTypeCompanyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockItemTypeCompany))
            )
            .andExpect(status().isCreated());

        // Validate the StockItemTypeCompany in the database
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeCreate + 1);
        StockItemTypeCompany testStockItemTypeCompany = stockItemTypeCompanyList.get(stockItemTypeCompanyList.size() - 1);
        assertThat(testStockItemTypeCompany.getTypePrice()).isEqualByComparingTo(DEFAULT_TYPE_PRICE);
    }

    @Test
    @Transactional
    void createStockItemTypeCompanyWithExistingId() throws Exception {
        // Create the StockItemTypeCompany with an existing ID
        stockItemTypeCompany.setId(1L);

        int databaseSizeBeforeCreate = stockItemTypeCompanyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockItemTypeCompanyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockItemTypeCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItemTypeCompany in the database
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypePriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockItemTypeCompanyRepository.findAll().size();
        // set the field null
        stockItemTypeCompany.setTypePrice(null);

        // Create the StockItemTypeCompany, which fails.

        restStockItemTypeCompanyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockItemTypeCompany))
            )
            .andExpect(status().isBadRequest());

        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStockItemTypeCompanies() throws Exception {
        // Initialize the database
        stockItemTypeCompanyRepository.saveAndFlush(stockItemTypeCompany);

        // Get all the stockItemTypeCompanyList
        restStockItemTypeCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockItemTypeCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].typePrice").value(hasItem(sameNumber(DEFAULT_TYPE_PRICE))));
    }

    @Test
    @Transactional
    void getStockItemTypeCompany() throws Exception {
        // Initialize the database
        stockItemTypeCompanyRepository.saveAndFlush(stockItemTypeCompany);

        // Get the stockItemTypeCompany
        restStockItemTypeCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, stockItemTypeCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockItemTypeCompany.getId().intValue()))
            .andExpect(jsonPath("$.typePrice").value(sameNumber(DEFAULT_TYPE_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingStockItemTypeCompany() throws Exception {
        // Get the stockItemTypeCompany
        restStockItemTypeCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockItemTypeCompany() throws Exception {
        // Initialize the database
        stockItemTypeCompanyRepository.saveAndFlush(stockItemTypeCompany);

        int databaseSizeBeforeUpdate = stockItemTypeCompanyRepository.findAll().size();

        // Update the stockItemTypeCompany
        StockItemTypeCompany updatedStockItemTypeCompany = stockItemTypeCompanyRepository
            .findById(stockItemTypeCompany.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedStockItemTypeCompany are not directly saved in db
        em.detach(updatedStockItemTypeCompany);
        updatedStockItemTypeCompany.typePrice(UPDATED_TYPE_PRICE);

        restStockItemTypeCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStockItemTypeCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStockItemTypeCompany))
            )
            .andExpect(status().isOk());

        // Validate the StockItemTypeCompany in the database
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeUpdate);
        StockItemTypeCompany testStockItemTypeCompany = stockItemTypeCompanyList.get(stockItemTypeCompanyList.size() - 1);
        assertThat(testStockItemTypeCompany.getTypePrice()).isEqualByComparingTo(UPDATED_TYPE_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingStockItemTypeCompany() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeCompanyRepository.findAll().size();
        stockItemTypeCompany.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockItemTypeCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockItemTypeCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockItemTypeCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItemTypeCompany in the database
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockItemTypeCompany() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeCompanyRepository.findAll().size();
        stockItemTypeCompany.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemTypeCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockItemTypeCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItemTypeCompany in the database
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockItemTypeCompany() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeCompanyRepository.findAll().size();
        stockItemTypeCompany.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemTypeCompanyMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockItemTypeCompany))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockItemTypeCompany in the database
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockItemTypeCompanyWithPatch() throws Exception {
        // Initialize the database
        stockItemTypeCompanyRepository.saveAndFlush(stockItemTypeCompany);

        int databaseSizeBeforeUpdate = stockItemTypeCompanyRepository.findAll().size();

        // Update the stockItemTypeCompany using partial update
        StockItemTypeCompany partialUpdatedStockItemTypeCompany = new StockItemTypeCompany();
        partialUpdatedStockItemTypeCompany.setId(stockItemTypeCompany.getId());

        restStockItemTypeCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockItemTypeCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockItemTypeCompany))
            )
            .andExpect(status().isOk());

        // Validate the StockItemTypeCompany in the database
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeUpdate);
        StockItemTypeCompany testStockItemTypeCompany = stockItemTypeCompanyList.get(stockItemTypeCompanyList.size() - 1);
        assertThat(testStockItemTypeCompany.getTypePrice()).isEqualByComparingTo(DEFAULT_TYPE_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateStockItemTypeCompanyWithPatch() throws Exception {
        // Initialize the database
        stockItemTypeCompanyRepository.saveAndFlush(stockItemTypeCompany);

        int databaseSizeBeforeUpdate = stockItemTypeCompanyRepository.findAll().size();

        // Update the stockItemTypeCompany using partial update
        StockItemTypeCompany partialUpdatedStockItemTypeCompany = new StockItemTypeCompany();
        partialUpdatedStockItemTypeCompany.setId(stockItemTypeCompany.getId());

        partialUpdatedStockItemTypeCompany.typePrice(UPDATED_TYPE_PRICE);

        restStockItemTypeCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockItemTypeCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockItemTypeCompany))
            )
            .andExpect(status().isOk());

        // Validate the StockItemTypeCompany in the database
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeUpdate);
        StockItemTypeCompany testStockItemTypeCompany = stockItemTypeCompanyList.get(stockItemTypeCompanyList.size() - 1);
        assertThat(testStockItemTypeCompany.getTypePrice()).isEqualByComparingTo(UPDATED_TYPE_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingStockItemTypeCompany() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeCompanyRepository.findAll().size();
        stockItemTypeCompany.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockItemTypeCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockItemTypeCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockItemTypeCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItemTypeCompany in the database
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockItemTypeCompany() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeCompanyRepository.findAll().size();
        stockItemTypeCompany.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemTypeCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockItemTypeCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItemTypeCompany in the database
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockItemTypeCompany() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeCompanyRepository.findAll().size();
        stockItemTypeCompany.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemTypeCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockItemTypeCompany))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockItemTypeCompany in the database
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockItemTypeCompany() throws Exception {
        // Initialize the database
        stockItemTypeCompanyRepository.saveAndFlush(stockItemTypeCompany);

        int databaseSizeBeforeDelete = stockItemTypeCompanyRepository.findAll().size();

        // Delete the stockItemTypeCompany
        restStockItemTypeCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockItemTypeCompany.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StockItemTypeCompany> stockItemTypeCompanyList = stockItemTypeCompanyRepository.findAll();
        assertThat(stockItemTypeCompanyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
