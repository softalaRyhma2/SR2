import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

import { getCompanyNameByInvoiceId, getEntities } from './stock.reducer';

export const Stock = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const stockList = useAppSelector(state => state.stock.entities);
  const loading = useAppSelector(state => state.stock.loading);
  const totalItems = useAppSelector(state => state.stock.totalItems);

  const companyNames = useAppSelector(state => state.stock.companyNames);
  const authorities = useAppSelector(state => state.authentication.account.authorities);
  const isAdminOrRecser = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN, AUTHORITIES.RECSER]);
  const [filterValue, setFilterValue] = useState('');

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    if (page) {
      setPaginationState({
        ...paginationState,
        activePage: +page,
      });
    }
  }, [pageLocation.search]);

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  useEffect(() => {
    stockList.forEach(stock => {
      if (stock.invoice && stock.invoice.id) {
        dispatch(getCompanyNameByInvoiceId(String(stock.invoice.id)));
      }
    });
  }, [stockList]);

  const getCompanyNameForInvoiceId = (invoiceId: string) => {
    return companyNames[invoiceId] || '';
  };

  const filteredStockList = stockList.filter(stock =>
    getCompanyNameForInvoiceId(stock.invoice?.id)
      .toLowerCase()
      .includes(filterValue.toLowerCase()),
  );

  return (
    <div>
      <h2 id="stock-heading" data-cy="StockHeading">
        <Translate contentKey="sr2App.stock.home.title">Stocks</Translate>
        <div className="d-flex justify-content-end">
          {isAdminOrRecser && (
            <Input
              type="text"
              placeholder="Filter by company name"
              value={filterValue}
              onChange={e => setFilterValue(e.target.value)}
              style={{ width: '300px', marginRight: '250px' }}
            />
          )}
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sr2App.stock.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/stock/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sr2App.stock.home.createLabel">Create new Stock</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {!loading && filteredStockList && filteredStockList.length > 0 && (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="sr2App.stock.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="sr2App.stock.stockDate">Stock Date</Translate>{' '}
                </th>
                <th>
                  <Translate contentKey="sr2App.stock.invoice">Invoice</Translate>
                </th>
                <th>
                  <Translate contentKey="sr2App.stock.companyName">Company Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {filteredStockList.map((stock, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    {
                      stock.id /*
                    <Button tag={Link} to={`/stock/${stock.id}`} color="link" size="sm">
                      {stock.id}
              </Button>*/
                    }
                  </td>
                  <td>{stock.stockDate ? <TextFormat type="date" value={stock.stockDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>
                    {
                      stock.invoice
                        ? stock.invoice.id
                        : '' /*stock.invoice ? <Link to={`/invoice/${stock.invoice.id}`}>{stock.invoice.id}</Link> : ''*/
                    }
                  </td>
                  <td>{stock.invoice ? getCompanyNameForInvoiceId(stock.invoice.id) : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/stock/${stock.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      {/* 
                      <Button
                        tag={Link}
                        to={`/stock/${stock.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      */}
                      {isAdminOrRecser && (
                        <Button
                          onClick={() =>
                            (window.location.href = `/stock/${stock.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                          }
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        )}
        {!loading && !(filteredStockList && filteredStockList.length > 0) && (
          <div className="alert alert-warning">
            <Translate contentKey="sr2App.stock.home.notFound">No Stocks found</Translate>
          </div>
        )}
      </div>
      {totalItems ? (
        <div className={stockList && stockList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Stock;
