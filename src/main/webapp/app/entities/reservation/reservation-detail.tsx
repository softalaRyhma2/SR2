import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reservation.reducer';
import { getReservedItemEntitiesForReservation } from '../reserved-item/reserved-item.reducer';

export const ReservationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
    dispatch(getReservedItemEntitiesForReservation(id));
  }, [id]);

  const reservationEntity = useAppSelector(state => state.reservation.entity);
  const reservedItemList = useAppSelector(state => state.reservedItem.entities);
  console.log('RESERVEDITEMLIST: ' + JSON.stringify(reservedItemList));

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
        </dl>
        <Button tag={Link} to="/reservation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reservation/${reservationEntity.id}/edit`} replace color="primary" disabled={reservationEntity.isPickedUp}>
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
      <Col md="8" className="jh-entity-details">
        <h2>Reserved Items</h2>
        <Button
          tag={Link}
          to={{ pathname: '/reserved-item/new', search: `?reservationId=${reservationEntity.id}` }}
          color="primary"
          className="jh-create-entity"
          id="jh-create-entity"
          data-cy="entityCreateButton"
          disabled={reservationEntity.isPickedUp}
        >
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="sr2App.reservedItem.home.createLabel">Create new Reserved Item</Translate>
        </Button>
        <Table>
          <thead>
            <tr>
              <th>
                <Translate contentKey="global.field.id">ID</Translate>
              </th>
              <th>
                <Translate contentKey="sr2App.reservedItem.quantity">Quantity</Translate>
              </th>
              <th>
                <Translate contentKey="sr2App.reservedItem.stockItemTypeName">Stock Item Type Name</Translate>
              </th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {reservedItemList.length === 0 ? (
              <tr>
                <td colSpan={8}>Reserved items not found</td>
              </tr>
            ) : (
              reservedItemList.map(reservedItemEntity => (
                <tr key={reservedItemEntity.id}>
                  <td>{reservedItemEntity.id}</td>
                  <td>{reservedItemEntity.quantity}</td>
                  <td>{reservedItemEntity.stockItem.stockItemTypeCompany.stockItemType.typeName}</td>
                  <td>
                    <Button tag={Link} to={`/reserved-item/${reservedItemEntity.id}`} color="primary">
                      <FontAwesomeIcon icon="eye" /> View
                    </Button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </Table>
      </Col>
    </Row>
  );
};

export default ReservationDetail;
