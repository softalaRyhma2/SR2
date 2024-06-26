import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './invoice.reducer';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { mkConfig, generateCsv, download } from 'export-to-csv';
import { getEntities as getStocks } from '../stock/stock.reducer';
import { DatePicker, Space, Checkbox } from 'antd';
import { getEntities as getCompanies } from '../company/company.reducer';
import { AUTHORITIES } from 'app/config/constants';

import { hasAnyAuthority } from 'app/shared/auth/private-route';

// Import statements...

export const Invoice = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const [selectedTimeframe, setSelectedTimeframe] = useState(null);
  const [showFilters, setShowFilters] = useState(false); // State to toggle visibility of filters
  const [selectedCompanies, setSelectedCompanies] = useState([]);
  const [selectedInvoices, setSelectedInvoices] = useState([]);

  const invoiceList = useAppSelector(state => state.invoice.entities);
  const loading = useAppSelector(state => state.invoice.loading);
  const totalItems = useAppSelector(state => state.invoice.totalItems);

  const companyList = useAppSelector(state => state.company.entities);

  const stockList = useAppSelector(state => state.stock.entities);

  const canSelectCompanies = useAppSelector(state =>
    hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.RECSER]),
  );

  useEffect(() => {
    // Dispatch an action to fetch stocks with the same arguments as getInvoices
    dispatch(getStocks({ page: 1, size: 10, sort: 'asc' }));
    dispatch(getCompanies({ page: 1, size: 10, sort: 'asc' }));
  }, [dispatch]);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
      }),
    );
  };

  const handleCompanyChange = (e, company) => {
    const isChecked = e.target.checked;
    if (isChecked) {
      setSelectedCompanies([...selectedCompanies, company]); // Add company to selected companies
    } else {
      setSelectedCompanies(selectedCompanies.filter(selectedCompany => selectedCompany.id !== company.id)); // Remove company from selected companies
    }
  };

  // Sort entities
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

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {};

  // Function to filter invoices based on selected timeframe
  const filteredInvoices = invoiceList.filter(invoice => {
    if (!selectedTimeframe && !selectedCompanies.length) {
      return true; // No timeframe or companies selected, so include all invoices
    } else {
      // Check timeframe
      const invoiceDate = new Date(invoice.invoiceDate);
      const startDate = selectedTimeframe ? new Date(selectedTimeframe[0]) : null;
      const endDate = selectedTimeframe ? new Date(selectedTimeframe[1]) : null;
      const withinTimeframe = !selectedTimeframe || (invoiceDate >= startDate && invoiceDate <= endDate);

      // Check companies
      const selectedCompanyIds = selectedCompanies.map(company => company.id);
      const belongsToSelectedCompanies = selectedCompanies.length === 0 || selectedCompanyIds.includes(invoice.company?.id);

      return withinTimeframe && belongsToSelectedCompanies;
    }
  });

  // Function to toggle visibility of filters
  const toggleFilters = () => {
    setShowFilters(!showFilters);

    const newText = showFilters ? 'Show Filters' : 'Hide Filters';
    document.getElementById('filterButton').innerText = newText;
  };

  return (
    <div>
      <h2 id="invoice-heading" data-cy="InvoiceHeading">
        <Translate contentKey="sr2App.invoice.home.title">Invoices</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="sr2App.invoice.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/invoice/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="sr2App.invoice.home.createLabel">Create new Invoice</Translate>
          </Link>
          {/* Filter Button */}
          <Button id="filterButton" onClick={toggleFilters} className="mx-2">
            <Translate contentKey={showFilters ? 'sr2App.invoice.home.hideFilters' : 'sr2App.invoice.home.showFilters'} />
          </Button>
        </div>
        {/* RangePicker for timeframe filtering */}
        {showFilters && (
          <div className="mt-2">
            <Space direction="vertical" size={12}>
              <DatePicker.RangePicker onChange={(dates, dateStrings) => setSelectedTimeframe(dates)} />
            </Space>
          </div>
        )}
        {/* Company Dropdown Menu */}
        {showFilters && canSelectCompanies && (
          <div className="mt-2">
            {companyList
              .filter(company => company.companyName !== 'Recser') // Filter out the "Recser" company
              .map(company => (
                <Checkbox key={company.id} onChange={e => handleCompanyChange(e, company)}>
                  {company.companyName}
                </Checkbox>
              ))}
          </div>
        )}
      </h2>
      <div className="table-responsive">
        {!loading && invoiceList && invoiceList.length > 0 && (
          <Table responsive>
            <thead>
              <tr>
                {/* Table header content */}
                {/* Removed checkboxes */}
                <th>
                  <Translate contentKey="sr2App.invoice.id">ID</Translate>
                </th>
                {/*<th className="hand" onClick={sort('totalSum')}>
                  <Translate contentKey="sr2App.invoice.totalSum">Total Sum</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalSum')} />
        </th>*/}
                <th>
                  <Translate contentKey="sr2App.invoice.invoiceDate">Invoice Date</Translate>{' '}
                </th>
                <th>
                  <Translate contentKey="sr2App.invoice.company">Company</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {filteredInvoices.map((invoice, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/* Removed checkboxes */}
                  <td>{invoice.id}</td>
                  {/* <td>{invoice.totalSum}</td>*/}
                  <td>
                    {invoice.invoiceDate ? <TextFormat type="date" value={invoice.invoiceDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{invoice.company ? <span>{invoice.company.companyName}</span> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/invoice/${invoice.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/invoice/${invoice.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/invoice/${invoice.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        )}
        {!loading && !(invoiceList && invoiceList.length > 0) && (
          <div className="alert alert-warning">
            <Translate contentKey="sr2App.invoice.home.notFound">No Invoices found</Translate>
          </div>
        )}
      </div>
      {totalItems ? (
        <div className={filteredInvoices && filteredInvoices.length > 0 ? '' : 'd-none'}>
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

export default Invoice;
