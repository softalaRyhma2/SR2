package com.softala.sr2.domain;

import static com.softala.sr2.domain.CompanyTestSamples.*;
import static com.softala.sr2.domain.InvoiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.softala.sr2.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = getCompanySample1();
        Company company2 = new Company();
        assertThat(company1).isNotEqualTo(company2);

        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);

        company2 = getCompanySample2();
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    void invoiceTest() throws Exception {
        Company company = getCompanyRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        company.addInvoice(invoiceBack);
        assertThat(company.getInvoices()).containsOnly(invoiceBack);
        assertThat(invoiceBack.getCompany()).isEqualTo(company);

        company.removeInvoice(invoiceBack);
        assertThat(company.getInvoices()).doesNotContain(invoiceBack);
        assertThat(invoiceBack.getCompany()).isNull();

        company.invoices(new HashSet<>(Set.of(invoiceBack)));
        assertThat(company.getInvoices()).containsOnly(invoiceBack);
        assertThat(invoiceBack.getCompany()).isEqualTo(company);

        company.setInvoices(new HashSet<>());
        assertThat(company.getInvoices()).doesNotContain(invoiceBack);
        assertThat(invoiceBack.getCompany()).isNull();
    }
}
