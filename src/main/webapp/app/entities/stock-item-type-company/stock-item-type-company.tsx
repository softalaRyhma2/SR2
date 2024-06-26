import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { faSpinner } from '@fortawesome/free-solid-svg-icons';

import { getEntities } from './stock-item-type-company.reducer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import { getCurrentUserCompany } from '../company/company.reducer';

export const StockItemTypeCompany = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const entities = useAppSelector(state => state.stockItemTypeCompany.entities);
  const loading = useAppSelector(state => state.stockItemTypeCompany.loading);
  const totalItems = useAppSelector(state => state.stockItemTypeCompany.totalItems);
  const authorities = useAppSelector(state => state.authentication.account.authorities);
  const isAdminOrRecser = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN, AUTHORITIES.RECSER]);
  const [currentUserCompany, setCurrentUserCompany] = useState(null);
  const [filteredEntities, setFilteredEntities] = useState([]);

  useEffect(() => {
    // Fetch the current user's company when the component mounts
    dispatch(getCurrentUserCompany())
      .then(resultAction => {
        // Extract the company from the result action
        const company = resultAction.payload;
        setCurrentUserCompany(company);
      })
      .catch(error => console.error('Error fetching current user company:', error));
  }, [dispatch]);

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
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
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
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  // Filter entities based on currentUserCompany only if user is not admin or recser
  useEffect(() => {
    // Filter entities based on currentUserCompany only if user is not admin or recser
    const filtered =
      !isAdminOrRecser && currentUserCompany
        ? entities.filter(entity => entity.company.companyName.toLowerCase() === currentUserCompany.companyName.toLowerCase())
        : entities;

    // Set the filtered entities
    setFilteredEntities(filtered);
  }, [entities, isAdminOrRecser, currentUserCompany]);

  // Render loader while data is loading
  if (loading) {
    return (
      <div className="text-center">
        <FontAwesomeIcon icon={faSpinner} spin size="3x" />
        <p>Loading...</p>
      </div>
    );
  }

  return (
    <div>
      <h2 id="stock-item-type-company-heading" data-cy="StockItemTypeCompanyHeading">
        <Translate contentKey="sr2App.stockItemTypeCompany.home.title">Stock Item Type Companies</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sr2App.stockItemTypeCompany.home.refreshListLabel">Refresh List</Translate>
          </Button>
          {isAdminOrRecser && (
            <Link
              to="/stock-item-type-company/new"
              className="btn btn-primary jh-create-entity"
              id="jh-create-entity"
              data-cy="entityCreateButton"
            >
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="sr2App.stockItemTypeCompany.home.createLabel">Create new Stock Item Type Company</Translate>
            </Link>
          )}
        </div>
      </h2>
      {filteredEntities !== null && ( // Conditionally render the table
        <div className="table-responsive">
          {filteredEntities.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  {isAdminOrRecser && (
                    <th className="hand" onClick={sort('id')}>
                      <Translate contentKey="sr2App.stockItemTypeCompany.id">ID</Translate>{' '}
                      <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                    </th>
                  )}
                  <th className="hand" onClick={sort('typePrice')}>
                    <Translate contentKey="sr2App.stockItemTypeCompany.typePrice">Type Price</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('typePrice')} />
                  </th>
                  <th className="hand" onClick={sort('stockItemType')}>
                    <Translate contentKey="sr2App.stockItemTypeCompany.stockItemType">Stock Item Type</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('stockItemType')} />
                  </th>
                  <th className="hand" onClick={sort('company')}>
                    <Translate contentKey="sr2App.stockItemTypeCompany.company">Company</Translate>
                    <FontAwesomeIcon icon={getSortIconByFieldName('company')} />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {filteredEntities.map((stockItemTypeCompany, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    {isAdminOrRecser && <td>{stockItemTypeCompany.id}</td>}
                    <td>{stockItemTypeCompany.typePrice.toFixed(2)}</td>
                    <td>{stockItemTypeCompany.stockItemType ? stockItemTypeCompany.stockItemType.typeName : ''}</td>
                    <td>{stockItemTypeCompany.company ? stockItemTypeCompany.company.companyName : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        {isAdminOrRecser && (
                          <Button
                            tag={Link}
                            to={`/stock-item-type-company/${stockItemTypeCompany.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                            color="primary"
                            size="sm"
                            data-cy="entityEditButton"
                          >
                            <FontAwesomeIcon icon="pencil-alt" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.edit">Edit</Translate>
                            </span>
                          </Button>
                        )}
                        {isAdminOrRecser && (
                          <Button
                            onClick={() =>
                              (window.location.href = `/stock-item-type-company/${stockItemTypeCompany.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
                <Translate contentKey="sr2App.stockItemTypeCompany.home.notFound">No Stock Item Type Companies found</Translate>
              </div>
            )
          )}
        </div>
      )}
      {totalItems ? (
        <div className={filteredEntities && filteredEntities.length > 0 ? '' : 'd-none'}>
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

export default StockItemTypeCompany;
