import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStockItemType } from 'app/shared/model/stock-item-type.model';
import { getEntities as getStockItemTypes } from 'app/entities/stock-item-type/stock-item-type.reducer';
import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { IStockItemTypeCompany } from 'app/shared/model/stock-item-type-company.model';
import { getEntity, updateEntity, createEntity, reset } from './stock-item-type-company.reducer';

export const StockItemTypeCompanyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const stockItemTypes = useAppSelector(state => state.stockItemType.entities);
  const companies = useAppSelector(state => state.company.entities);
  const stockItemTypeCompanyEntity = useAppSelector(state => state.stockItemTypeCompany.entity);
  const loading = useAppSelector(state => state.stockItemTypeCompany.loading);
  const updating = useAppSelector(state => state.stockItemTypeCompany.updating);
  const updateSuccess = useAppSelector(state => state.stockItemTypeCompany.updateSuccess);

  const handleClose = () => {
    navigate('/stock-item-type-company' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStockItemTypes({}));
    dispatch(getCompanies({}));
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
    if (values.typePrice !== undefined && typeof values.typePrice !== 'number') {
      values.typePrice = Number(values.typePrice);
    }

    const entity = {
      ...stockItemTypeCompanyEntity,
      ...values,
      stockItemType: stockItemTypes.find(it => it.id.toString() === values.stockItemType.toString()),
      company: companies.find(it => it.id.toString() === values.company.toString()),
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
          ...stockItemTypeCompanyEntity,
          stockItemType: stockItemTypeCompanyEntity?.stockItemType?.id,
          company: stockItemTypeCompanyEntity?.company?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sr2App.stockItemTypeCompany.home.createOrEditLabel" data-cy="StockItemTypeCompanyCreateUpdateHeading">
            <Translate contentKey="sr2App.stockItemTypeCompany.home.createOrEditLabel">Create or edit a StockItemTypeCompany</Translate>
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
                  id="stock-item-type-company-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sr2App.stockItemTypeCompany.typePrice')}
                id="stock-item-type-company-typePrice"
                name="typePrice"
                data-cy="typePrice"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="stock-item-type-company-stockItemType"
                name="stockItemType"
                required
                data-cy="stockItemType"
                label={translate('sr2App.stockItemTypeCompany.stockItemType')}
                type="select"
              >
                <option value="">Select a type</option>
                {stockItemTypes
                  ? stockItemTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.typeName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="stock-item-type-company-company"
                name="company"
                required
                data-cy="company"
                label={translate('sr2App.stockItemTypeCompany.company')}
                type="select"
              >
                <option value="">Select a company</option>
                {companies
                  ? companies.map(otherEntity => {
                      if (otherEntity.companyName !== 'Recser') {
                        return (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.companyName}
                          </option>
                        );
                      }
                    })
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/stock-item-type-company" replace color="info">
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

export default StockItemTypeCompanyUpdate;
