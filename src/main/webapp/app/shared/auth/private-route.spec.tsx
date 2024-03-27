import React from 'react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { render } from '@testing-library/react';
import { TranslatorContext } from 'react-jhipster';
import configureStore from 'redux-mock-store';
import thunk from 'redux-thunk';
import { Provider } from 'react-redux';

import { AUTHORITIES, AuthoritiesConstants } from 'app/config/constants';
import PrivateRoute, { hasAnyAuthority } from './private-route';

const TestComp = () => <div>Test</div>;

describe('private-route component', () => {
  beforeAll(() => {
    TranslatorContext.registerTranslations('en', {
      'error.http.403': 'You are not authorized to access this page.',
    });
  });

  const mockStore = configureStore([thunk]);
  const wrapper = (Elem: JSX.Element, authentication) => {
    const store = mockStore({ authentication });
    return render(
      <Provider store={store}>
        <MemoryRouter>{Elem}</MemoryRouter>
      </Provider>,
    );
  };

  // All tests will go here
  it('Should throw error when falsy children are provided', () => {
    const originalError = console.error;
    console.error = jest.fn();
    expect(() =>
      wrapper(<PrivateRoute>{null}</PrivateRoute>, {
        isAuthenticated: true,
        sessionHasBeenFetched: true,
        account: {
          authorities: [],
        },
      }),
    ).toThrow(Error);
    console.error = originalError;
  });

  it('Should render an error message when the user has no authorities', () => {
    const { container } = wrapper(
      <PrivateRoute>
        <TestComp />
      </PrivateRoute>,
      {
        isAuthenticated: true,
        sessionHasBeenFetched: true,
        account: {
          authorities: [],
        },
      },
    );
    expect(container.innerHTML).toMatch(/<div class="insufficient-authority"><div class="alert alert-danger">.*<\/div><\/div>/);
  });

  it('Should render a route for the component provided when authenticated', () => {
    const { container } = wrapper(
      <PrivateRoute>
        <TestComp />
      </PrivateRoute>,
      {
        isAuthenticated: true,
        sessionHasBeenFetched: true,
        account: {
          authorities: [AuthoritiesConstants.ADMIN],
        },
      },
    );
    expect(container.innerHTML).toEqual('<div>Test</div>');
  });

  it('Should render a route for the component provided when authenticated with TRANSPORT role', () => {
    const { container } = wrapper(
      <PrivateRoute>
        <TestComp />
      </PrivateRoute>,
      {
        isAuthenticated: true,
        sessionHasBeenFetched: true,
        account: {
          authorities: [AuthoritiesConstants.TRANSPORT], // Updated to use TRANSPORT
        },
      },
    );
    expect(container.innerHTML).toEqual('<div>Test</div>');
  });

  it('Should render a route for the component provided when authenticated with PCENTER role', () => {
    const { container } = wrapper(
      <PrivateRoute>
        <TestComp />
      </PrivateRoute>,
      {
        isAuthenticated: true,
        sessionHasBeenFetched: true,
        account: {
          authorities: [AuthoritiesConstants.PCENTER], // Added test case for PCENTER
        },
      },
    );
    expect(container.innerHTML).toEqual('<div>Test</div>');
  });

  it('Should redirect when not authenticated', () => {
    const { container } = wrapper(
      <Routes>
        <Route
          path="/"
          element={
            <PrivateRoute>
              <TestComp />
            </PrivateRoute>
          }
        />
        <Route path="/login" element={<div>Login</div>} />
      </Routes>,
      {
        isAuthenticated: false,
        sessionHasBeenFetched: true,
        account: {
          authorities: [AuthoritiesConstants.ADMIN],
        },
      },
    );
    expect(container.innerHTML).not.toEqual('<div>Test</div>');
    expect(container.innerHTML).toEqual('<div>Login</div>');
  });
});

describe('hasAnyAuthority', () => {
  it('Should return false when authorities is invalid', () => {
    expect(hasAnyAuthority(undefined, undefined)).toEqual(false);
    expect(hasAnyAuthority(null, [])).toEqual(false);
    expect(hasAnyAuthority([], [])).toEqual(false);
  });

  it('Should return true when authorities are valid and hasAnyAuthorities is empty', () => {
    expect(hasAnyAuthority([AuthoritiesConstants.TRANSPORT], [])).toEqual(true);
    expect(hasAnyAuthority([AuthoritiesConstants.PCENTER], [])).toEqual(true);
    expect(hasAnyAuthority([AuthoritiesConstants.ADMIN], [])).toEqual(true); // Added for ADMIN
  });

  it('Should return true when authorities contain TRANSPORT and checking for TRANSPORT', () => {
    expect(hasAnyAuthority([AuthoritiesConstants.TRANSPORT], [AuthoritiesConstants.TRANSPORT])).toEqual(true);
  });

  it('Should return true when authorities contain PCENTER and checking for PCENTER', () => {
    expect(hasAnyAuthority([AuthoritiesConstants.PCENTER], [AuthoritiesConstants.PCENTER])).toEqual(true);
  });

  it('Should return true when authorities contain ADMIN and checking for any', () => {
    // Demonstrates that ADMIN should typically have access to anything
    expect(hasAnyAuthority([AuthoritiesConstants.ADMIN], [AuthoritiesConstants.TRANSPORT])).toEqual(true);
    expect(hasAnyAuthority([AuthoritiesConstants.ADMIN], [AuthoritiesConstants.PCENTER])).toEqual(true);
    expect(hasAnyAuthority([AuthoritiesConstants.ADMIN], [AuthoritiesConstants.ADMIN])).toEqual(true);
  });

  it('Should return false when authorities do not contain the required authority', () => {
    expect(hasAnyAuthority([AuthoritiesConstants.TRANSPORT], [AuthoritiesConstants.PCENTER])).toEqual(false);
    expect(hasAnyAuthority([AuthoritiesConstants.PCENTER], [AuthoritiesConstants.TRANSPORT])).toEqual(false);
    // Example where neither TRANSPORT nor PCENTER should access ADMIN-specific resources
    expect(hasAnyAuthority([AuthoritiesConstants.TRANSPORT, AuthoritiesConstants.PCENTER], [AuthoritiesConstants.ADMIN])).toEqual(false);
  });
});
