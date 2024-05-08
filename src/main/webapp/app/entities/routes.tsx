import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Company from './company';
import Stock from './stock';
import Reservation from './reservation';
import Invoice from './invoice';
import StockItem from './stock-item';
import StockItemType from './stock-item-type';
import StockItemTypeCompany from './stock-item-type-company';
import ReservedItem from './reserved-item';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="company/*" element={<Company />} />
        <Route path="stock/*" element={<Stock />} />
        <Route path="reservation/*" element={<Reservation />} />
        <Route path="invoice/*" element={<Invoice />} />
        <Route path="stock-item/*" element={<StockItem />} />
        <Route path="stock-item-type/*" element={<StockItemType />} />
        <Route path="stock-item-type-company/*" element={<StockItemTypeCompany />} />
        <Route path="reserved-item/*" element={<ReservedItem />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
