import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './stock-item.reducer';

export const StockItemDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stockItemEntity = useAppSelector(state => state.stockItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stockItemDetailsHeading">
          <Translate contentKey="sr2App.stockItem.detail.title">StockItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{stockItemEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="sr2App.stockItem.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{stockItemEntity.quantity}</dd>
          <dt>
            <span id="availability">
              <Translate contentKey="sr2App.stockItem.availability">Availability</Translate>
            </span>
          </dt>
          <dd>{stockItemEntity.availability}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="sr2App.stockItem.price">Price</Translate>
            </span>
          </dt>
          <dd>{stockItemEntity.price}</dd>
          <dt>
            <span id="stockItemId">
              <Translate contentKey="sr2App.stockItem.stockItemId">Stock Item Id</Translate>
            </span>
          </dt>
          <dd>{stockItemEntity.stockItemId}</dd>
          <dt>
            <Translate contentKey="sr2App.stockItem.stock">Stock</Translate>
          </dt>
          <dd>{stockItemEntity.stock ? stockItemEntity.stock.id : ''}</dd>
          <dt>
            <Translate contentKey="sr2App.stockItem.stockItemType">Stock Item Type</Translate>
          </dt>
          <dd>{stockItemEntity.stockItemType ? stockItemEntity.stockItemType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/stock-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stock-item/${stockItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StockItemDetail;
