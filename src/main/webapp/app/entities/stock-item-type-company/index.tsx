import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StockItemTypeCompany from './stock-item-type-company';
import StockItemTypeCompanyDetail from './stock-item-type-company-detail';
import StockItemTypeCompanyUpdate from './stock-item-type-company-update';
import StockItemTypeCompanyDeleteDialog from './stock-item-type-company-delete-dialog';

const StockItemTypeCompanyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StockItemTypeCompany />} />
    <Route path="new" element={<StockItemTypeCompanyUpdate />} />
    <Route path=":id">
      <Route index element={<StockItemTypeCompanyDetail />} />
      <Route path="edit" element={<StockItemTypeCompanyUpdate />} />
      <Route path="delete" element={<StockItemTypeCompanyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StockItemTypeCompanyRoutes;
