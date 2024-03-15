import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reserved-item.reducer';

export const ReservedItemDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservedItemEntity = useAppSelector(state => state.reservedItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservedItemDetailsHeading">
          <Translate contentKey="sr2App.reservedItem.detail.title">ReservedItem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reservedItemEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="sr2App.reservedItem.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{reservedItemEntity.quantity}</dd>
          <dt>
            <Translate contentKey="sr2App.reservedItem.reservation">Reservation</Translate>
          </dt>
          <dd>{reservedItemEntity.reservation ? reservedItemEntity.reservation.id : ''}</dd>
          <dt>
            <Translate contentKey="sr2App.reservedItem.stockItem">Stock Item</Translate>
          </dt>
          <dd>{reservedItemEntity.stockItem ? reservedItemEntity.stockItem.id : ''}</dd>
        </dl>
        <dt>
          <Translate contentKey="sr2App.reservedItem.user">User ID</Translate>
        </dt>
        <dd>{reservedItemEntity.user ? reservedItemEntity.user.id : ''}</dd>
        <Button tag={Link} to="/reserved-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reserved-item/${reservedItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservedItemDetail;
