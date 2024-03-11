import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StockItem from './stock-item';
import StockItemDetail from './stock-item-detail';
import StockItemUpdate from './stock-item-update';
import StockItemDeleteDialog from './stock-item-delete-dialog';

const StockItemRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StockItem />} />
    <Route path="new" element={<StockItemUpdate />} />
    <Route path=":id">
      <Route index element={<StockItemDetail />} />
      <Route path="edit" element={<StockItemUpdate />} />
      <Route path="delete" element={<StockItemDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StockItemRoutes;
