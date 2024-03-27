import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './stock-item-type.reducer';

export const StockItemTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stockItemTypeEntity = useAppSelector(state => state.stockItemType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stockItemTypeDetailsHeading">
          <Translate contentKey="sr2App.stockItemType.detail.title">StockItemType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{stockItemTypeEntity.id}</dd>
          <dt>
            <span id="typeName">
              <Translate contentKey="sr2App.stockItemType.typeName">Type Name</Translate>
            </span>
          </dt>
          <dd>{stockItemTypeEntity.typeName}</dd>
        </dl>
        <Button tag={Link} to="/stock-item-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stock-item-type/${stockItemTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StockItemTypeDetail;
