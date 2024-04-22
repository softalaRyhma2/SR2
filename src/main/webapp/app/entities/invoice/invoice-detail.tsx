import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './invoice.reducer';

export const InvoiceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const invoiceEntity = useAppSelector(state => state.invoice.entity);

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="invoiceDetailsHeading">
          <Translate contentKey="sr2App.invoice.detail.title">Invoice</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{invoiceEntity.id}</dd>
          <dt>
            <span id="totalSum">
              <Translate contentKey="sr2App.invoice.totalSum">Total Sum</Translate>
            </span>
          </dt>
          <dd>{invoiceEntity.totalSum}</dd>
          <dt>
            <span id="invoiceDate">
              <Translate contentKey="sr2App.invoice.invoiceDate">Invoice Date</Translate>
            </span>
          </dt>
          <dd>
            {invoiceEntity.invoiceDate ? <TextFormat value={invoiceEntity.invoiceDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isClosed">
              <Translate contentKey="sr2App.invoice.isClosed">Is Closed</Translate>
            </span>
          </dt>
          <dd>{invoiceEntity.isClosed ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="sr2App.invoice.company">Company</Translate>
          </dt>
          <dd>{invoiceEntity.company ? invoiceEntity.company.companyName : ''}</dd>
        </dl>
        <Button tag={Link} to="/invoice" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/invoice/${invoiceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
        <Col>
          <h3 id="stock-heading" data-cy="StockHeading">
            <Translate contentKey="sr2App.stock.home.title">Stocks</Translate>
          </h3>
          <Table responsive>
            <thead>
              <tr>
                <th>stock id</th>
                <th>stock date</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {Array.isArray(invoiceEntity.stocks) && invoiceEntity.stocks.length > 0 ? (
                invoiceEntity.stocks.map(stock => (
                  <tr key={stock.id}>
                    <td>{stock.id}</td>
                    <td>{stock.stockDate}</td>
                    <td>
                      {Array.isArray(stock.stockItems) && stock.stockItems.length > 0 ? (
                        <Table>
                          <thead>
                            <tr>
                              <th>Item id</th>
                              <th>ID from stockitemtypecompany</th>
                              <th>Quantity</th>
                              <th>Price</th>
                            </tr>
                          </thead>
                          <tbody>
                            {stock.stockItems.map(item => (
                              <tr key={item.id}>
                                <td>{item.id}</td>
                                <td>{item.stockItemTypeCompany.id}</td>
                                <td>{item.quantity}</td>
                                <td>{item.price}€</td>
                              </tr>
                            ))}
                          </tbody>
                        </Table>
                      ) : (
                        <p>No stock items in stock</p>
                      )}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td>No stocks in this invoice</td>
                </tr>
              )}
            </tbody>
          </Table>
        </Col>
      </Col>
    </Row>
  );
};

export default InvoiceDetail;
