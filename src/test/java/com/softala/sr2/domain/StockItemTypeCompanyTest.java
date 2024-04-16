package com.softala.sr2.domain;

import static com.softala.sr2.domain.CompanyTestSamples.*;
import static com.softala.sr2.domain.StockItemTypeCompanyTestSamples.*;
import static com.softala.sr2.domain.StockItemTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.softala.sr2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockItemTypeCompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockItemTypeCompany.class);
        StockItemTypeCompany stockItemTypeCompany1 = getStockItemTypeCompanySample1();
        StockItemTypeCompany stockItemTypeCompany2 = new StockItemTypeCompany();
        assertThat(stockItemTypeCompany1).isNotEqualTo(stockItemTypeCompany2);

        stockItemTypeCompany2.setId(stockItemTypeCompany1.getId());
        assertThat(stockItemTypeCompany1).isEqualTo(stockItemTypeCompany2);

        stockItemTypeCompany2 = getStockItemTypeCompanySample2();
        assertThat(stockItemTypeCompany1).isNotEqualTo(stockItemTypeCompany2);
    }

    @Test
    void stockItemTypeTest() throws Exception {
        StockItemTypeCompany stockItemTypeCompany = getStockItemTypeCompanyRandomSampleGenerator();
        StockItemType stockItemTypeBack = getStockItemTypeRandomSampleGenerator();

        stockItemTypeCompany.setStockItemType(stockItemTypeBack);
        assertThat(stockItemTypeCompany.getStockItemType()).isEqualTo(stockItemTypeBack);

        stockItemTypeCompany.stockItemType(null);
        assertThat(stockItemTypeCompany.getStockItemType()).isNull();
    }

    @Test
    void companyTest() throws Exception {
        StockItemTypeCompany stockItemTypeCompany = getStockItemTypeCompanyRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        stockItemTypeCompany.setCompany(companyBack);
        assertThat(stockItemTypeCompany.getCompany()).isEqualTo(companyBack);

        stockItemTypeCompany.company(null);
        assertThat(stockItemTypeCompany.getCompany()).isNull();
    }
}
