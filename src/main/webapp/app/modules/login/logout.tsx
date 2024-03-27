import React, { useLayoutEffect } from 'react';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';

export const Logout = () => {
  const logoutUrl = useAppSelector(state => state.authentication.logoutUrl);
  const dispatch = useAppDispatch();

  useLayoutEffect(() => {
    dispatch(logout());
    if (logoutUrl) {
      window.location.href = logoutUrl;
    }
  });

  const centerStyle = {
    display: 'flex', // Enables Flexbox
    justifyContent: 'center', // Centers horizontally
    alignItems: 'center', // Centers vertically
  };
  return (
    <div style={centerStyle}>
      <h4>Logged out successfully!</h4>
    </div>
  );
};

export default Logout;
