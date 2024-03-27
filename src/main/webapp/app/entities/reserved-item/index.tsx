import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ReservedItem from './reserved-item';
import ReservedItemDetail from './reserved-item-detail';
import ReservedItemUpdate from './reserved-item-update';
import ReservedItemDeleteDialog from './reserved-item-delete-dialog';

const ReservedItemRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ReservedItem />} />
    <Route path="new" element={<ReservedItemUpdate />} />
    <Route path=":id">
      <Route index element={<ReservedItemDetail />} />
      <Route path="edit" element={<ReservedItemUpdate />} />
      <Route path="delete" element={<ReservedItemDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReservedItemRoutes;
