package com.softala.sr2.domain;

import static com.softala.sr2.domain.CompanyTestSamples.*;
import static com.softala.sr2.domain.InvoiceTestSamples.*;
import static com.softala.sr2.domain.StockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.softala.sr2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = getInvoiceSample1();
        Invoice invoice2 = new Invoice();
        assertThat(invoice1).isNotEqualTo(invoice2);

        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);

        invoice2 = getInvoiceSample2();
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    void companyTest() throws Exception {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        invoice.setCompany(companyBack);
        assertThat(invoice.getCompany()).isEqualTo(companyBack);

        invoice.company(null);
        assertThat(invoice.getCompany()).isNull();
    }

    @Test
    void stockTest() throws Exception {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Stock stockBack = getStockRandomSampleGenerator();

        invoice.setStock(stockBack);
        assertThat(invoice.getStock()).isEqualTo(stockBack);

        invoice.stock(null);
        assertThat(invoice.getStock()).isNull();
    }
}
