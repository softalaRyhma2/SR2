import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './stock.reducer';

export const StockDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stockEntity = useAppSelector(state => state.stock.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stockDetailsHeading">
          <Translate contentKey="sr2App.stock.detail.title">Stock</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{stockEntity.id}</dd>
          <dt>
            <span id="stockDate">
              <Translate contentKey="sr2App.stock.stockDate">Stock Date</Translate>
            </span>
          </dt>
          <dd>{stockEntity.stockDate ? <TextFormat value={stockEntity.stockDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="sr2App.stock.invoice">Invoice</Translate>
          </dt>
          <dd>{stockEntity.invoice ? stockEntity.invoice.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/stock" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stock/${stockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
      <Col md="4">
        <h2>Stock Items</h2>
        <Table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Quantity</th>
              <th>View</th>
            </tr>
          </thead>
          <tbody>
            {stockEntity.stockItemList &&
              stockEntity.stockItemList.map(stockItem => (
                <tr key={stockItem.id}>
                  <td>{stockItem.id}</td>
                  <td>{stockItem.name}</td>
                  <td>{stockItem.quantity}</td>
                  <td>
                    <Button tag={Link} to={`/stock-item/${stockItem.id}`} color="primary">
                      <FontAwesomeIcon icon="eye" /> View
                    </Button>
                  </td>
                </tr>
              ))}
          </tbody>
        </Table>
      </Col>
    </Row>
  );
};

export default StockDetail;
