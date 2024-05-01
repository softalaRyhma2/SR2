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

  // CSV configuration
  const csvConfig = mkConfig({ useKeysAsHeaders: true, fieldSeparator: ';' });
  const handleCompanyChange = (e, company) => {
    const isChecked = e.target.checked;
    if (isChecked) {
      setSelectedCompanies([...selectedCompanies, company]); // Add company to selected companies
    } else {
      setSelectedCompanies(selectedCompanies.filter(selectedCompany => selectedCompany.id !== company.id)); // Remove company from selected companies
    }
  };

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
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  // Sort entities
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

  const handleSelectAll = e => {
    if (e.target.checked) {
      setSelectedInvoices(filteredInvoices);
    } else {
      setSelectedInvoices([]);
    }
  };

  const handleSelectInvoice = invoice => {
    const isSelected = selectedInvoices.some(selectedInvoice => selectedInvoice.id === invoice.id);
    if (isSelected) {
      setSelectedInvoices(selectedInvoices.filter(inv => inv.id !== invoice.id));
    } else {
      setSelectedInvoices([...selectedInvoices, invoice]);
    }
  };

  // Function to handle CSV export
  const handleExportToCSV = () => {
    const selectedInvoiceCsv = generateCsv(csvConfig)(
      selectedInvoices.flatMap(invoice => {
        const stocksForInvoice = stockList.filter(stock => stock.invoice?.id === invoice.id);
        return stocksForInvoice.flatMap(stock => {
          const stockItemsWithTypes = stock.stockItems.map(stockItem => ({
            id: invoice.id,
            totalSum: invoice.totalSum,
            invoiceDate: invoice.invoiceDate,
            companyName: invoice.company ? invoice.company.companyName : '',
            companyEmail: invoice.company ? invoice.company.companyEmail : '',
            stockDate: stock.stockDate || '',
            stockItemType: stockItem.stockItemType ? stockItem.stockItemType.typeName : '',
            stockItemTypePrice: stockItem.stockItemType ? stockItem.price : '',
            stockItemQuantity: stockItem.quantity ? stockItem.quantity : '',
          }));
          // If there are no stock items for the current invoice, include the invoice details
          return stockItemsWithTypes.length > 0
            ? stockItemsWithTypes
            : [
                {
                  id: invoice.id,
                  totalSum: invoice.totalSum,
                  invoiceDate: invoice.invoiceDate,
                  companyName: invoice.company ? invoice.company.companyName : '',
                  companyEmail: invoice.company ? invoice.company.companyEmail : '',
                  stockDate: stock.stockDate || '', // Use stockDate for verification
                  stockItemType: '',
                  stockItemTypePrice: '',
                  stockItemQuantity: '',
                },
              ];
        });
      }),
    );
    download(csvConfig)(selectedInvoiceCsv);
  };

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
    console.log(companyList);
    console.log(stockList);
    setShowFilters(!showFilters);
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
          {/* Add CSV Export Button */}
          <Button onClick={handleExportToCSV}>Export to CSV</Button>
          {/* Filter Button */}
          <Button onClick={toggleFilters} className="mx-2">
            {showFilters ? 'Hide Filters' : 'Show Filters'}
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
        {filteredInvoices && filteredInvoices.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Checkbox onChange={handleSelectAll} />
                </th>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="sr2App.invoice.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('totalSum')}>
                  <Translate contentKey="sr2App.invoice.totalSum">Total Sum</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalSum')} />
                </th>
                <th className="hand" onClick={sort('invoiceDate')}>
                  <Translate contentKey="sr2App.invoice.invoiceDate">Invoice Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('invoiceDate')} />
                </th>
                <th>
                  <Translate contentKey="sr2App.invoice.company">Company</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {filteredInvoices.map((invoice, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Checkbox
                      onChange={() => handleSelectInvoice(invoice)}
                      checked={selectedInvoices.some(selectedInvoice => selectedInvoice.id === invoice.id)}
                    />
                  </td>
                  <td>
                    <Button tag={Link} to={`/invoice/${invoice.id}`} color="link" size="sm">
                      {invoice.id}
                    </Button>
                  </td>
                  <td>{invoice.totalSum}</td>
                  <td>
                    {invoice.invoiceDate ? <TextFormat type="date" value={invoice.invoiceDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{invoice.company ? <Link to={`/company/${invoice.company.id}`}>{invoice.company.companyName}</Link> : ''}</td>
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
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="sr2App.invoice.home.notFound">No Invoices found</Translate>
            </div>
          )
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
