import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStock } from 'app/shared/model/stock.model';
import { getEntities as getStocks } from 'app/entities/stock/stock.reducer';
import { IStockItemType } from 'app/shared/model/stock-item-type.model';
import { getEntities as getStockItemTypes } from 'app/entities/stock-item-type/stock-item-type.reducer';
import { IStockItem } from 'app/shared/model/stock-item.model';
import { getEntity, updateEntity, createEntity, reset } from './stock-item.reducer';

export const StockItemUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const stocks = useAppSelector(state => state.stock.entities);
  const stockItemTypes = useAppSelector(state => state.stockItemType.entities);
  const stockItemEntity = useAppSelector(state => state.stockItem.entity);
  const loading = useAppSelector(state => state.stockItem.loading);
  const updating = useAppSelector(state => state.stockItem.updating);
  const updateSuccess = useAppSelector(state => state.stockItem.updateSuccess);

  const handleClose = () => {
    navigate('/stock-item' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStocks({}));
    dispatch(getStockItemTypes({}));
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
    if (values.availability !== undefined && typeof values.availability !== 'number') {
      values.availability = Number(values.availability);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.stockItemId !== undefined && typeof values.stockItemId !== 'number') {
      values.stockItemId = Number(values.stockItemId);
    }

    const entity = {
      ...stockItemEntity,
      ...values,
      stock: stocks.find(it => it.id.toString() === values.stock.toString()),
      stockItemType: stockItemTypes.find(it => it.id.toString() === values.stockItemType.toString()),
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
          ...stockItemEntity,
          stock: stockItemEntity?.stock?.id,
          stockItemType: stockItemEntity?.stockItemType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sr2App.stockItem.home.createOrEditLabel" data-cy="StockItemCreateUpdateHeading">
            <Translate contentKey="sr2App.stockItem.home.createOrEditLabel">Create or edit a StockItem</Translate>
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
                  id="stock-item-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sr2App.stockItem.quantity')}
                id="stock-item-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('sr2App.stockItem.availability')}
                id="stock-item-availability"
                name="availability"
                data-cy="availability"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('sr2App.stockItem.price')}
                id="stock-item-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('sr2App.stockItem.stockItemId')}
                id="stock-item-stockItemId"
                name="stockItemId"
                data-cy="stockItemId"
                type="text"
              />
              <ValidatedField
                id="stock-item-stock"
                name="stock"
                data-cy="stock"
                label={translate('sr2App.stockItem.stock')}
                type="select"
                required
              >
                <option value="" key="0" />
                {stocks
                  ? stocks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="stock-item-stockItemType"
                name="stockItemType"
                data-cy="stockItemType"
                label={translate('sr2App.stockItem.stockItemType')}
                type="select"
                required
              >
                <option value="" key="0" />
                {stockItemTypes
                  ? stockItemTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/stock-item" replace color="info">
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

export default StockItemUpdate;
