package com.softala.sr2.domain;

import static com.softala.sr2.domain.InvoiceTestSamples.*;
import static com.softala.sr2.domain.StockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.softala.sr2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stock.class);
        Stock stock1 = getStockSample1();
        Stock stock2 = new Stock();
        assertThat(stock1).isNotEqualTo(stock2);

        stock2.setId(stock1.getId());
        assertThat(stock1).isEqualTo(stock2);

        stock2 = getStockSample2();
        assertThat(stock1).isNotEqualTo(stock2);
    }

    @Test
    void invoiceTest() throws Exception {
        Stock stock = getStockRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        stock.setInvoice(invoiceBack);
        assertThat(stock.getInvoice()).isEqualTo(invoiceBack);

        stock.invoice(null);
        assertThat(stock.getInvoice()).isNull();
    }
}
