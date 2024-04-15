import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity /*, getEntityWithStock*/ } from './invoice.reducer';

export const InvoiceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  /*  useEffect(() => {
    dispatch(getEntityWithStock(id));
  }, []);

  const invoiceEntity = useAppSelector(state => state.invoice.getEntityWithStock);
  */
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
          <h3>Stocks</h3>
          <Table responsive>
            <thead>
              <tr>
                <th>stock id</th>
                <th>stock date</th>
                <th>items</th>
              </tr>
            </thead>
            <tbody>
              {Array.isArray(invoiceEntity.stocks) && invoiceEntity.stocks.length > 0 ? (
                invoiceEntity.stocks.map(stock => (
                  <tr key={stock.id}>
                    <td>{stock.id}</td>
                    <td>{stock.stockDate}</td>
                    <td>
                      {Array.isArray(invoiceEntity.stocksit) && invoiceEntity.stocksit.length > 0 ? (
                        <ul>
                          {invoiceEntity.stocksit.map(item => (
                            <li key={item.id}>{item.id}</li>
                          ))}
                        </ul>
                      ) : (
                        <td>No stock items in stock</td>
                      )}
                    </td>
                  </tr>
                ))
              ) : (
                <td>No stocks in this invoice</td>
              )}
            </tbody>
          </Table>
        </Col>
      </Col>
    </Row>
  );
};

export default InvoiceDetail;
