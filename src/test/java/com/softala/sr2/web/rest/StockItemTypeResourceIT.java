package com.softala.sr2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.softala.sr2.IntegrationTest;
import com.softala.sr2.domain.StockItemType;
import com.softala.sr2.repository.StockItemTypeRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link StockItemTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockItemTypeResourceIT {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stock-item-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StockItemTypeRepository stockItemTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockItemTypeMockMvc;

    private StockItemType stockItemType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockItemType createEntity(EntityManager em) {
        StockItemType stockItemType = new StockItemType().typeName(DEFAULT_TYPE_NAME);
        return stockItemType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockItemType createUpdatedEntity(EntityManager em) {
        StockItemType stockItemType = new StockItemType().typeName(UPDATED_TYPE_NAME);
        return stockItemType;
    }

    @BeforeEach
    public void initTest() {
        stockItemType = createEntity(em);
    }

    @Test
    @Transactional
    void createStockItemType() throws Exception {
        int databaseSizeBeforeCreate = stockItemTypeRepository.findAll().size();
        // Create the StockItemType
        restStockItemTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockItemType)))
            .andExpect(status().isCreated());

        // Validate the StockItemType in the database
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeCreate + 1);
        StockItemType testStockItemType = stockItemTypeList.get(stockItemTypeList.size() - 1);
        assertThat(testStockItemType.getTypeName()).isEqualTo(DEFAULT_TYPE_NAME);
    }

    @Test
    @Transactional
    void createStockItemTypeWithExistingId() throws Exception {
        // Create the StockItemType with an existing ID
        stockItemType.setId(1L);

        int databaseSizeBeforeCreate = stockItemTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockItemTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockItemType)))
            .andExpect(status().isBadRequest());

        // Validate the StockItemType in the database
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockItemTypeRepository.findAll().size();
        // set the field null
        stockItemType.setTypeName(null);

        // Create the StockItemType, which fails.

        restStockItemTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockItemType)))
            .andExpect(status().isBadRequest());

        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStockItemTypes() throws Exception {
        // Initialize the database
        stockItemTypeRepository.saveAndFlush(stockItemType);

        // Get all the stockItemTypeList
        restStockItemTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockItemType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getStockItemType() throws Exception {
        // Initialize the database
        stockItemTypeRepository.saveAndFlush(stockItemType);

        // Get the stockItemType
        restStockItemTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, stockItemType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockItemType.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingStockItemType() throws Exception {
        // Get the stockItemType
        restStockItemTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockItemType() throws Exception {
        // Initialize the database
        stockItemTypeRepository.saveAndFlush(stockItemType);

        int databaseSizeBeforeUpdate = stockItemTypeRepository.findAll().size();

        // Update the stockItemType
        StockItemType updatedStockItemType = stockItemTypeRepository.findById(stockItemType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStockItemType are not directly saved in db
        em.detach(updatedStockItemType);
        updatedStockItemType.typeName(UPDATED_TYPE_NAME);

        restStockItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStockItemType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStockItemType))
            )
            .andExpect(status().isOk());

        // Validate the StockItemType in the database
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeUpdate);
        StockItemType testStockItemType = stockItemTypeList.get(stockItemTypeList.size() - 1);
        assertThat(testStockItemType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingStockItemType() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeRepository.findAll().size();
        stockItemType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockItemType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItemType in the database
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockItemType() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeRepository.findAll().size();
        stockItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItemType in the database
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockItemType() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeRepository.findAll().size();
        stockItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockItemType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockItemType in the database
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockItemTypeWithPatch() throws Exception {
        // Initialize the database
        stockItemTypeRepository.saveAndFlush(stockItemType);

        int databaseSizeBeforeUpdate = stockItemTypeRepository.findAll().size();

        // Update the stockItemType using partial update
        StockItemType partialUpdatedStockItemType = new StockItemType();
        partialUpdatedStockItemType.setId(stockItemType.getId());

        restStockItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockItemType))
            )
            .andExpect(status().isOk());

        // Validate the StockItemType in the database
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeUpdate);
        StockItemType testStockItemType = stockItemTypeList.get(stockItemTypeList.size() - 1);
        assertThat(testStockItemType.getTypeName()).isEqualTo(DEFAULT_TYPE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateStockItemTypeWithPatch() throws Exception {
        // Initialize the database
        stockItemTypeRepository.saveAndFlush(stockItemType);

        int databaseSizeBeforeUpdate = stockItemTypeRepository.findAll().size();

        // Update the stockItemType using partial update
        StockItemType partialUpdatedStockItemType = new StockItemType();
        partialUpdatedStockItemType.setId(stockItemType.getId());

        partialUpdatedStockItemType.typeName(UPDATED_TYPE_NAME);

        restStockItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockItemType))
            )
            .andExpect(status().isOk());

        // Validate the StockItemType in the database
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeUpdate);
        StockItemType testStockItemType = stockItemTypeList.get(stockItemTypeList.size() - 1);
        assertThat(testStockItemType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingStockItemType() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeRepository.findAll().size();
        stockItemType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockItemType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItemType in the database
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockItemType() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeRepository.findAll().size();
        stockItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockItemType))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItemType in the database
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockItemType() throws Exception {
        int databaseSizeBeforeUpdate = stockItemTypeRepository.findAll().size();
        stockItemType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stockItemType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockItemType in the database
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockItemType() throws Exception {
        // Initialize the database
        stockItemTypeRepository.saveAndFlush(stockItemType);

        int databaseSizeBeforeDelete = stockItemTypeRepository.findAll().size();

        // Delete the stockItemType
        restStockItemTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockItemType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StockItemType> stockItemTypeList = stockItemTypeRepository.findAll();
        assertThat(stockItemTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
