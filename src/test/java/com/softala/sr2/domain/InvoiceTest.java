package com.softala.sr2.domain;

import static com.softala.sr2.domain.CompanyTestSamples.*;
import static com.softala.sr2.domain.InvoiceTestSamples.*;
import static com.softala.sr2.domain.StockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.softala.sr2.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void stockTest() throws Exception {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Stock stockBack = getStockRandomSampleGenerator();

        invoice.addStock(stockBack);
        assertThat(invoice.getStocks()).containsOnly(stockBack);
        assertThat(stockBack.getInvoice()).isEqualTo(invoice);

        invoice.removeStock(stockBack);
        assertThat(invoice.getStocks()).doesNotContain(stockBack);
        assertThat(stockBack.getInvoice()).isNull();

        invoice.stocks(new HashSet<>(Set.of(stockBack)));
        assertThat(invoice.getStocks()).containsOnly(stockBack);
        assertThat(stockBack.getInvoice()).isEqualTo(invoice);

        invoice.setStocks(new HashSet<>());
        assertThat(invoice.getStocks()).doesNotContain(stockBack);
        assertThat(stockBack.getInvoice()).isNull();
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
}
