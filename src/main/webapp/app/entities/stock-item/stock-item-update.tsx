import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams, useLocation } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStock } from 'app/shared/model/stock.model';
import { getEntities as getStocks } from 'app/entities/stock/stock.reducer';
import { IStockItemTypeCompany } from 'app/shared/model/stock-item-type-company.model';
import { getEntities as getStockItemTypes } from 'app/entities/stock-item-type-company/stock-item-type-company.reducer';
import { IStockItem } from 'app/shared/model/stock-item.model';
import { getEntity, updateEntity, createEntity, reset } from './stock-item.reducer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export const StockItemUpdate = () => {
  const dispatch = useAppDispatch();
  const isAdminOrRecser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, ['ROLE_ADMIN', 'ROLE_RECSER']));

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const stocks = useAppSelector(state => state.stock.entities);
  const stockItemTypes = useAppSelector(state => state.stockItemTypeCompany.entities);
  const stockItemEntity = useAppSelector(state => state.stockItem.entity);
  const loading = useAppSelector(state => state.stockItem.loading);
  const updating = useAppSelector(state => state.stockItem.updating);
  const updateSuccess = useAppSelector(state => state.stockItem.updateSuccess);

  const [selectedStockItemType, setSelectedStockItemType] = useState(null);

  const [companyNameForStock, setCompanyNameForStock] = useState(null);

  const handleClose = () => {
    if (stockId) {
      navigate(`/stock/${stockId}`);
    } else {
      navigate('/stock-item' + location.search);
    }
  };

  const handleGoBack = () => {
    navigate(-1);
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
      handleGoBack;
    }
  }, [updateSuccess, navigate]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }
    if (values.available !== undefined && typeof values.available !== 'number') {
      values.available = Number(values.available);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }

    const entity = {
      ...stockItemEntity,
      ...values,
      stock: stocks.find(it => it.id.toString() === values.stock.toString()),
      companyName: companyNameForStock,
      stockItemTypeCompany: selectedStockItemType,
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
          stockItemTypeCompany: stockItemEntity?.stockItemTypeCompany?.id,
        };
  //code below for fetching stockid when coming from stock-detail.tsx
  const location = useLocation();
  const [stockId, setStockId] = useState(null);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const stockIdParam = params.get('stockId');
    const companyNameParam = params.get('companyNameForStock');
    if (stockIdParam) {
      setStockId(stockIdParam);
    }
    if (companyNameParam) {
      setCompanyNameForStock(companyNameParam);
    }
  }, [location.search]);

  const [filteredStockItemTypes, setFilteredStockItemTypes] = useState([]);
  useEffect(() => {
    let filteredTypes = [];
    if (companyNameForStock) {
      filteredTypes = stockItemTypes.filter(type => type.company?.companyName === companyNameForStock);
    }
    setFilteredStockItemTypes(filteredTypes);
  }, [companyNameForStock, stockItemTypes]);

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
                label={translate('sr2App.stockItem.available')}
                id="stock-item-available"
                name="available"
                data-cy="available"
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
                validate={{ required: true }}
                value={selectedStockItemType ? selectedStockItemType.typePrice : ''}
              />
              {/* Jos stockId on saatavilla, näytä se */}
              {stockId && (
                <ValidatedField id="stock-item-stock" name="stock" data-cy="stock" label="Stock ID" type="text" value={stockId} />
              )}
              {/* Jos stockId ei ole saatavilla, näytä varaston valintakenttä */}
              {!stockId && (
                <ValidatedField id="stock-item-stock" name="stock" data-cy="stock" label="Select Stock" type="select">
                  <option value="">Select a stock</option>
                  {stocks.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.id}
                    </option>
                  ))}
                </ValidatedField>
              )}
              <ValidatedField
                id="stock-item-type-company"
                name="stockItemTypeCompany"
                data-cy="stockItemTypeCompany"
                label={translate('sr2App.stockItem.stockItemType')}
                type="select"
                onChange={e => {
                  const selectedItem = filteredStockItemTypes.find(type => type.id.toString() === e.target.value);
                  setSelectedStockItemType(selectedItem);
                }}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              >
                <option value="" key="0" />
                {filteredStockItemTypes && filteredStockItemTypes.length > 0 ? (
                  filteredStockItemTypes.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.stockItemType.typeName}, {otherEntity.typePrice}, {otherEntity.company.companyName}
                    </option>
                  ))
                ) : (
                  <option value="">No type prices for this company</option>
                )}
              </ValidatedField>
              <Button onClick={handleGoBack} id="cancel-save" data-cy="entityCreateCancelButton" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button
                onClick={handleGoBack}
                updateSuccessColor="primary"
                id="save-entity"
                data-cy="entityCreateSaveButton"
                type="submit"
                disabled={updating}
              >
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
