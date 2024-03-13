import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StockItemType from './stock-item-type';
import StockItemTypeDetail from './stock-item-type-detail';
import StockItemTypeUpdate from './stock-item-type-update';
import StockItemTypeDeleteDialog from './stock-item-type-delete-dialog';

const StockItemTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StockItemType />} />
    <Route path="new" element={<StockItemTypeUpdate />} />
    <Route path=":id">
      <Route index element={<StockItemTypeDetail />} />
      <Route path="edit" element={<StockItemTypeUpdate />} />
      <Route path="delete" element={<StockItemTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StockItemTypeRoutes;
