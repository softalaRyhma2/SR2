import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

import { getEntities } from './stock-item.reducer';

export const StockItem = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const stockItemList = useAppSelector(state => state.stockItem.entities);
  const loading = useAppSelector(state => state.stockItem.loading);
  const totalItems = useAppSelector(state => state.stockItem.totalItems);
  const authorities = useAppSelector(state => state.authentication.account.authorities);
  const isAdminOrRecser = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN, AUTHORITIES.RECSER]);
  const isTransport = hasAnyAuthority(authorities, [AUTHORITIES.TRANSPORT]);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: 'id', // Only sort by 'id'
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    if (fieldName !== 'id') {
      return null; // No sorting arrows for columns other than 'id'
    }
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="stock-item-heading" data-cy="StockItemHeading">
        <Translate contentKey="sr2App.stockItem.home.title">Stock Items</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sr2App.stockItem.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/stock-item/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sr2App.stockItem.home.createLabel">Create new Stock Item</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {stockItemList && stockItemList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sr2App.stockItem.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand">
                  <Translate contentKey="sr2App.stockItem.quantity">Quantity</Translate>
                </th>
                {!isTransport && (
                  <th className="hand">
                    <Translate contentKey="sr2App.stockItem.available">Available</Translate>
                  </th>
                )}
                <th className="hand">
                  <Translate contentKey="sr2App.stockItem.price">Price</Translate>
                </th>
                <th>Type</th>
                <th>
                  <Translate contentKey="sr2App.stockItem.stock">Stock</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>StockItemTypeCompany id, company</th>
                <th>
                  <Translate contentKey="sr2App.stockItem.stockDate">Stock Date</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {stockItemList.map((stockItem, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{stockItem.id}</td>
                  <td>{stockItem.quantity}</td>
                  {!isTransport && <td>{stockItem.available}</td>}
                  <td>{stockItem.price}</td>
                  <td>{stockItem.stockItemTypeCompany ? stockItem.stockItemTypeCompany.stockItemType.typeName : ''}</td>
                  <td>{stockItem.stock ? <span>{stockItem.stock.id}</span> : ''}</td>
                  <td>
                    {stockItem.stockItemTypeCompany ? stockItem.stockItemTypeCompany.company.id : ''},{' '}
                    {stockItem.stockItemTypeCompany ? stockItem.stockItemTypeCompany.company.companyName : ''}
                  </td>
                  <td>{stockItem.stock ? stockItem.stock.stockDate : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/stock-item/${stockItem.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/stock-item/${stockItem.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      {isAdminOrRecser && (
                        <Button
                          onClick={() =>
                            (window.location.href = `/stock-item/${stockItem.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="sr2App.stockItem.home.notFound">No Stock Items found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={stockItemList && stockItemList.length > 0 ? '' : 'd-none'}>
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

export default StockItem;
