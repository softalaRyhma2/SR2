import company from 'app/entities/company/company.reducer';
import stock from 'app/entities/stock/stock.reducer';
import reservation from 'app/entities/reservation/reservation.reducer';
import invoice from 'app/entities/invoice/invoice.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  company,
  stock,
  reservation,
  invoice,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
