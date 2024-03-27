package com.softala.sr2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.softala.sr2.IntegrationTest;
import com.softala.sr2.domain.ReservedItem;
import com.softala.sr2.repository.ReservedItemRepository;
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
 * Integration tests for the {@link ReservedItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReservedItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/reserved-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReservedItemRepository reservedItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservedItemMockMvc;

    private ReservedItem reservedItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReservedItem createEntity(EntityManager em) {
        ReservedItem reservedItem = new ReservedItem().quantity(DEFAULT_QUANTITY);
        return reservedItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReservedItem createUpdatedEntity(EntityManager em) {
        ReservedItem reservedItem = new ReservedItem().quantity(UPDATED_QUANTITY);
        return reservedItem;
    }

    @BeforeEach
    public void initTest() {
        reservedItem = createEntity(em);
    }

    @Test
    @Transactional
    void createReservedItem() throws Exception {
        int databaseSizeBeforeCreate = reservedItemRepository.findAll().size();
        // Create the ReservedItem
        restReservedItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservedItem)))
            .andExpect(status().isCreated());

        // Validate the ReservedItem in the database
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeCreate + 1);
        ReservedItem testReservedItem = reservedItemList.get(reservedItemList.size() - 1);
        assertThat(testReservedItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void createReservedItemWithExistingId() throws Exception {
        // Create the ReservedItem with an existing ID
        reservedItem.setId(1L);

        int databaseSizeBeforeCreate = reservedItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservedItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservedItem)))
            .andExpect(status().isBadRequest());

        // Validate the ReservedItem in the database
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservedItemRepository.findAll().size();
        // set the field null
        reservedItem.setQuantity(null);

        // Create the ReservedItem, which fails.

        restReservedItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservedItem)))
            .andExpect(status().isBadRequest());

        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReservedItems() throws Exception {
        // Initialize the database
        reservedItemRepository.saveAndFlush(reservedItem);

        // Get all the reservedItemList
        restReservedItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservedItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getReservedItem() throws Exception {
        // Initialize the database
        reservedItemRepository.saveAndFlush(reservedItem);

        // Get the reservedItem
        restReservedItemMockMvc
            .perform(get(ENTITY_API_URL_ID, reservedItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reservedItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getNonExistingReservedItem() throws Exception {
        // Get the reservedItem
        restReservedItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReservedItem() throws Exception {
        // Initialize the database
        reservedItemRepository.saveAndFlush(reservedItem);

        int databaseSizeBeforeUpdate = reservedItemRepository.findAll().size();

        // Update the reservedItem
        ReservedItem updatedReservedItem = reservedItemRepository.findById(reservedItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReservedItem are not directly saved in db
        em.detach(updatedReservedItem);
        updatedReservedItem.quantity(UPDATED_QUANTITY);

        restReservedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReservedItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReservedItem))
            )
            .andExpect(status().isOk());

        // Validate the ReservedItem in the database
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeUpdate);
        ReservedItem testReservedItem = reservedItemList.get(reservedItemList.size() - 1);
        assertThat(testReservedItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingReservedItem() throws Exception {
        int databaseSizeBeforeUpdate = reservedItemRepository.findAll().size();
        reservedItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservedItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservedItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservedItem in the database
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReservedItem() throws Exception {
        int databaseSizeBeforeUpdate = reservedItemRepository.findAll().size();
        reservedItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservedItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservedItem in the database
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReservedItem() throws Exception {
        int databaseSizeBeforeUpdate = reservedItemRepository.findAll().size();
        reservedItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservedItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservedItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReservedItem in the database
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservedItemWithPatch() throws Exception {
        // Initialize the database
        reservedItemRepository.saveAndFlush(reservedItem);

        int databaseSizeBeforeUpdate = reservedItemRepository.findAll().size();

        // Update the reservedItem using partial update
        ReservedItem partialUpdatedReservedItem = new ReservedItem();
        partialUpdatedReservedItem.setId(reservedItem.getId());

        restReservedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservedItem))
            )
            .andExpect(status().isOk());

        // Validate the ReservedItem in the database
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeUpdate);
        ReservedItem testReservedItem = reservedItemList.get(reservedItemList.size() - 1);
        assertThat(testReservedItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateReservedItemWithPatch() throws Exception {
        // Initialize the database
        reservedItemRepository.saveAndFlush(reservedItem);

        int databaseSizeBeforeUpdate = reservedItemRepository.findAll().size();

        // Update the reservedItem using partial update
        ReservedItem partialUpdatedReservedItem = new ReservedItem();
        partialUpdatedReservedItem.setId(reservedItem.getId());

        partialUpdatedReservedItem.quantity(UPDATED_QUANTITY);

        restReservedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservedItem))
            )
            .andExpect(status().isOk());

        // Validate the ReservedItem in the database
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeUpdate);
        ReservedItem testReservedItem = reservedItemList.get(reservedItemList.size() - 1);
        assertThat(testReservedItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingReservedItem() throws Exception {
        int databaseSizeBeforeUpdate = reservedItemRepository.findAll().size();
        reservedItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reservedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservedItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservedItem in the database
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReservedItem() throws Exception {
        int databaseSizeBeforeUpdate = reservedItemRepository.findAll().size();
        reservedItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservedItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservedItem in the database
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReservedItem() throws Exception {
        int databaseSizeBeforeUpdate = reservedItemRepository.findAll().size();
        reservedItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservedItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reservedItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReservedItem in the database
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReservedItem() throws Exception {
        // Initialize the database
        reservedItemRepository.saveAndFlush(reservedItem);

        int databaseSizeBeforeDelete = reservedItemRepository.findAll().size();

        // Delete the reservedItem
        restReservedItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, reservedItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReservedItem> reservedItemList = reservedItemRepository.findAll();
        assertThat(reservedItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
