import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
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
            <span id="quantity">
              <Translate contentKey="sr2App.stock.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{stockEntity.quantity}</dd>
          <dt>
            <span id="available">
              <Translate contentKey="sr2App.stock.available">Available</Translate>
            </span>
          </dt>
          <dd>{stockEntity.available}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="sr2App.stock.price">Price</Translate>
            </span>
          </dt>
          <dd>{stockEntity.price}</dd>
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
    </Row>
  );
};

export default StockDetail;
