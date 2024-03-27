package com.softala.sr2.web.rest;

import static com.softala.sr2.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.softala.sr2.IntegrationTest;
import com.softala.sr2.domain.StockItem;
import com.softala.sr2.repository.StockItemRepository;
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
 * Integration tests for the {@link StockItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_AVAILABLE = 1;
    private static final Integer UPDATED_AVAILABLE = 2;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/stock-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockItemMockMvc;

    private StockItem stockItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockItem createEntity(EntityManager em) {
        StockItem stockItem = new StockItem().quantity(DEFAULT_QUANTITY).available(DEFAULT_AVAILABLE).price(DEFAULT_PRICE);
        return stockItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockItem createUpdatedEntity(EntityManager em) {
        StockItem stockItem = new StockItem().quantity(UPDATED_QUANTITY).available(UPDATED_AVAILABLE).price(UPDATED_PRICE);
        return stockItem;
    }

    @BeforeEach
    public void initTest() {
        stockItem = createEntity(em);
    }

    @Test
    @Transactional
    void createStockItem() throws Exception {
        int databaseSizeBeforeCreate = stockItemRepository.findAll().size();
        // Create the StockItem
        restStockItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockItem)))
            .andExpect(status().isCreated());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeCreate + 1);
        StockItem testStockItem = stockItemList.get(stockItemList.size() - 1);
        assertThat(testStockItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStockItem.getAvailable()).isEqualTo(DEFAULT_AVAILABLE);
        assertThat(testStockItem.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createStockItemWithExistingId() throws Exception {
        // Create the StockItem with an existing ID
        stockItem.setId(1L);

        int databaseSizeBeforeCreate = stockItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockItem)))
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockItemRepository.findAll().size();
        // set the field null
        stockItem.setQuantity(null);

        // Create the StockItem, which fails.

        restStockItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockItem)))
            .andExpect(status().isBadRequest());

        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockItemRepository.findAll().size();
        // set the field null
        stockItem.setAvailable(null);

        // Create the StockItem, which fails.

        restStockItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockItem)))
            .andExpect(status().isBadRequest());

        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockItemRepository.findAll().size();
        // set the field null
        stockItem.setPrice(null);

        // Create the StockItem, which fails.

        restStockItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockItem)))
            .andExpect(status().isBadRequest());

        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStockItems() throws Exception {
        // Initialize the database
        stockItemRepository.saveAndFlush(stockItem);

        // Get all the stockItemList
        restStockItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @Test
    @Transactional
    void getStockItem() throws Exception {
        // Initialize the database
        stockItemRepository.saveAndFlush(stockItem);

        // Get the stockItem
        restStockItemMockMvc
            .perform(get(ENTITY_API_URL_ID, stockItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingStockItem() throws Exception {
        // Get the stockItem
        restStockItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStockItem() throws Exception {
        // Initialize the database
        stockItemRepository.saveAndFlush(stockItem);

        int databaseSizeBeforeUpdate = stockItemRepository.findAll().size();

        // Update the stockItem
        StockItem updatedStockItem = stockItemRepository.findById(stockItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStockItem are not directly saved in db
        em.detach(updatedStockItem);
        updatedStockItem.quantity(UPDATED_QUANTITY).available(UPDATED_AVAILABLE).price(UPDATED_PRICE);

        restStockItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStockItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStockItem))
            )
            .andExpect(status().isOk());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeUpdate);
        StockItem testStockItem = stockItemList.get(stockItemList.size() - 1);
        assertThat(testStockItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockItem.getAvailable()).isEqualTo(UPDATED_AVAILABLE);
        assertThat(testStockItem.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingStockItem() throws Exception {
        int databaseSizeBeforeUpdate = stockItemRepository.findAll().size();
        stockItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockItem() throws Exception {
        int databaseSizeBeforeUpdate = stockItemRepository.findAll().size();
        stockItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockItem() throws Exception {
        int databaseSizeBeforeUpdate = stockItemRepository.findAll().size();
        stockItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockItemWithPatch() throws Exception {
        // Initialize the database
        stockItemRepository.saveAndFlush(stockItem);

        int databaseSizeBeforeUpdate = stockItemRepository.findAll().size();

        // Update the stockItem using partial update
        StockItem partialUpdatedStockItem = new StockItem();
        partialUpdatedStockItem.setId(stockItem.getId());

        partialUpdatedStockItem.quantity(UPDATED_QUANTITY);

        restStockItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockItem))
            )
            .andExpect(status().isOk());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeUpdate);
        StockItem testStockItem = stockItemList.get(stockItemList.size() - 1);
        assertThat(testStockItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockItem.getAvailable()).isEqualTo(DEFAULT_AVAILABLE);
        assertThat(testStockItem.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateStockItemWithPatch() throws Exception {
        // Initialize the database
        stockItemRepository.saveAndFlush(stockItem);

        int databaseSizeBeforeUpdate = stockItemRepository.findAll().size();

        // Update the stockItem using partial update
        StockItem partialUpdatedStockItem = new StockItem();
        partialUpdatedStockItem.setId(stockItem.getId());

        partialUpdatedStockItem.quantity(UPDATED_QUANTITY).available(UPDATED_AVAILABLE).price(UPDATED_PRICE);

        restStockItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockItem))
            )
            .andExpect(status().isOk());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeUpdate);
        StockItem testStockItem = stockItemList.get(stockItemList.size() - 1);
        assertThat(testStockItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockItem.getAvailable()).isEqualTo(UPDATED_AVAILABLE);
        assertThat(testStockItem.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingStockItem() throws Exception {
        int databaseSizeBeforeUpdate = stockItemRepository.findAll().size();
        stockItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockItem() throws Exception {
        int databaseSizeBeforeUpdate = stockItemRepository.findAll().size();
        stockItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockItem() throws Exception {
        int databaseSizeBeforeUpdate = stockItemRepository.findAll().size();
        stockItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stockItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockItem() throws Exception {
        // Initialize the database
        stockItemRepository.saveAndFlush(stockItem);

        int databaseSizeBeforeDelete = stockItemRepository.findAll().size();

        // Delete the stockItem
        restStockItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
