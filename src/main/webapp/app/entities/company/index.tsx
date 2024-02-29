import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Company from './company';
import CompanyDetail from './company-detail';
import CompanyUpdate from './company-update';
import CompanyDeleteDialog from './company-delete-dialog';
import { AUTHORITIES } from 'app/config/constants';
import PrivateRoute from 'app/shared/auth/private-route';

const CompanyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Company />} />
    <Route
      path="new"
      element={
        <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.RECSER]}>
          <CompanyUpdate />
        </PrivateRoute>
      }
    />
    <Route path=":id">
      <Route index element={<CompanyDetail />} />
      <Route path="edit" element={<CompanyUpdate />} />
      <Route path="delete" element={<CompanyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompanyRoutes;
