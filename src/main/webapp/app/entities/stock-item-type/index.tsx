import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StockItemType from './stock-item-type';
import StockItemTypeDetail from './stock-item-type-detail';
import StockItemTypeUpdate from './stock-item-type-update';
import StockItemTypeDeleteDialog from './stock-item-type-delete-dialog';
import PrivateRoute from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

const StockItemTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StockItemType />} />
    <Route
      path="new"
      element={
        <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.RECSER]}>
          <StockItemTypeUpdate />
        </PrivateRoute>
      }
    />
    <Route path=":id">
      <Route index element={<StockItemTypeDetail />} />
      <Route
        path="edit"
        element={
          <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.RECSER]}>
            <StockItemTypeUpdate />
          </PrivateRoute>
        }
      />
      <Route
        path="delete"
        element={
          <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.RECSER]}>
            <StockItemTypeDeleteDialog />
          </PrivateRoute>
        }
      />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StockItemTypeRoutes;
