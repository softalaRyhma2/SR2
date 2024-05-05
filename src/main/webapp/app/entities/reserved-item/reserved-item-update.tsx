import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IReservation } from 'app/shared/model/reservation.model';
import { getEntities as getReservations } from 'app/entities/reservation/reservation.reducer';
import { IStockItem } from 'app/shared/model/stock-item.model';
import { getEntities as getStockItems } from 'app/entities/stock-item/stock-item.reducer';
import { IReservedItem } from 'app/shared/model/reserved-item.model';
import { getEntity, updateEntity, createEntity, reset } from './reserved-item.reducer';

export const ReservedItemUpdate = () => {
  const dispatch = useAppDispatch();

  //const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reservations = useAppSelector(state => state.reservation.entities);
  const stockItems = useAppSelector(state => state.stockItem.entities);
  const reservedItemEntity = useAppSelector(state => state.reservedItem.entity);
  const loading = useAppSelector(state => state.reservedItem.loading);
  const updating = useAppSelector(state => state.reservedItem.updating);
  const updateSuccess = useAppSelector(state => state.reservedItem.updateSuccess);

  const handleClose = () => {
    history.back();
    //navigate('/reserved-item' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getReservations({}));
    dispatch(getStockItems({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }

    const entity = {
      ...reservedItemEntity,
      ...values,
      reservation: reservations.find(it => it.id.toString() === values.reservation.toString()),
      stockItem: stockItems.find(it => it.id.toString() === values.stockItem.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...reservedItemEntity,
          reservation: reservedItemEntity?.reservation?.id,
          stockItem: reservedItemEntity?.stockItem?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sr2App.reservedItem.home.createOrEditLabel" data-cy="ReservedItemCreateUpdateHeading">
            <Translate contentKey="sr2App.reservedItem.home.createOrEditLabel">Create or edit a ReservedItem</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="reserved-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sr2App.reservedItem.quantity')}
                id="reserved-item-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="reserved-item-reservation"
                name="reservation"
                data-cy="reservation"
                label={translate('sr2App.reservedItem.reservation')}
                type="select"
              >
                <option value="" key="0" />
                {reservations
                  ? reservations.map(rItem => (
                      <option value={rItem.id} key={rItem.id}>
                        {rItem.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="reserved-item-stockItem" name="stockItem" data-cy="stockItem" label="Stock Item" type="select">
                <option value="" key="0" />
                {stockItems
                  ? stockItems
                      .filter(sItem => sItem.stockItemTypeCompany.company.companyName === 'Käsittelylaitos')
                      .map(sItem => (
                        <option value={sItem.id} key={sItem.id}>
                          {sItem.stockItemTypeCompany.stockItemType.typeName}, available:{sItem.available} (
                          {sItem.stockItemTypeCompany.company.companyName}, stockId: {sItem.stock.id}, stockDdate: {sItem.stock.stockDate})
                        </option>
                      ))
                  : null}
              </ValidatedField>
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                onClick={handleClose}
                to="/reserved-item"
                replace
                color="info"
              >
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ReservedItemUpdate;
