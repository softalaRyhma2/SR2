import company from 'app/entities/company/company.reducer';
import stock from 'app/entities/stock/stock.reducer';
import reservation from 'app/entities/reservation/reservation.reducer';
import invoice from 'app/entities/invoice/invoice.reducer';
import stockItem from 'app/entities/stock-item/stock-item.reducer';
import stockItemType from 'app/entities/stock-item-type/stock-item-type.reducer';
import reservedItem from 'app/entities/reserved-item/reserved-item.reducer';
import stockItemTypeCompany from 'app/entities/stock-item-type-company/stock-item-type-company.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  company,
  stock,
  reservation,
  invoice,
  stockItem,
  stockItemType,
  reservedItem,
  stockItemTypeCompany,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
