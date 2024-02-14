import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reservation.reducer';

export const ReservationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservationEntity = useAppSelector(state => state.reservation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservationDetailsHeading">
          <Translate contentKey="sr2App.reservation.detail.title">Reservation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.id}</dd>
          <dt>
            <span id="reservedQuantity">
              <Translate contentKey="sr2App.reservation.reservedQuantity">Reserved Quantity</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.reservedQuantity}</dd>
          <dt>
            <span id="reservationDate">
              <Translate contentKey="sr2App.reservation.reservationDate">Reservation Date</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.reservationDate ? (
              <TextFormat value={reservationEntity.reservationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isPickedUp">
              <Translate contentKey="sr2App.reservation.isPickedUp">Is Picked Up</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.isPickedUp ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="sr2App.reservation.stock">Stock</Translate>
          </dt>
          <dd>{reservationEntity.stock ? reservationEntity.stock.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reservation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reservation/${reservationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservationDetail;
