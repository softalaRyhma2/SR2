import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { IInvoice } from 'app/shared/model/invoice.model';
import { getEntity, updateEntity, createEntity, reset } from './invoice.reducer';

export const InvoiceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const companies = useAppSelector(state => state.company.entities);
  const invoiceEntity = useAppSelector(state => state.invoice.entity);
  const loading = useAppSelector(state => state.invoice.loading);
  const updating = useAppSelector(state => state.invoice.updating);
  const updateSuccess = useAppSelector(state => state.invoice.updateSuccess);

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
    dispatch(getCompanies({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      navigate(`/invoice`);
    }
  }, [updateSuccess, id, navigate]);

  const handleGoBack = () => {
    if (isNew) {
      navigate('/invoice');
    } else {
      navigate(`/invoice/${id}`);
    }
  };

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.totalSum !== undefined && typeof values.totalSum !== 'number') {
      values.totalSum = Number(values.totalSum);
    }

    const entity = {
      ...invoiceEntity,
      ...values,
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
      ? { invoiceDate: new Date().toISOString().split('T')[0] }
      : {
          ...invoiceEntity,
          company: invoiceEntity?.company?.id,
        };

  const isClosed = invoiceEntity?.isClosed;

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sr2App.invoice.home.createOrEditLabel" data-cy="InvoiceCreateUpdateHeading">
            <Translate contentKey="sr2App.invoice.home.createOrEditLabel">Create or edit a Invoice</Translate>
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
                  id="invoice-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                  disabled={isClosed}
                />
              ) : null}
              {/*!isNew ? (
                <ValidatedField
                  label={translate('sr2App.invoice.totalSum')}
                  id="invoice-totalSum"
                  name="totalSum"
                  data-cy="totalSum"
                  type="text"
                  disabled={isClosed}
                />
              ) : null*/}
              <ValidatedField
                label={translate('sr2App.invoice.invoiceDate')}
                id="invoice-invoiceDate"
                name="invoiceDate"
                data-cy="invoiceDate"
                type="date"
                disabled={isClosed}
              />
              {!isNew ? (
                <ValidatedField
                  label={translate('sr2App.invoice.isClosed')}
                  id="invoice-isClosed"
                  name="isClosed"
                  data-cy="isClosed"
                  check
                  type="checkbox"
                  disabled={isClosed} // added disabled attribute here
                />
              ) : null}
              {isNew ? (
                <ValidatedField
                  id="invoice-company"
                  name="company"
                  data-cy="company"
                  label={translate('sr2App.invoice.company')}
                  type="select"
                  required
                >
                  <option value="" key="0" />
                  {companies
                    ? companies
                        .filter(company => company.companyName !== 'Recser')
                        .map(company => (
                          <option value={company.id} key={company.id}>
                            {company.companyName}
                          </option>
                        ))
                    : null}
                </ValidatedField>
              ) : (
                <div>
                  <span>Company: </span>
                  <span>{invoiceEntity.company?.companyName}</span>
                  <br />
                  <br />
                </div>
              )}
              <Button onClick={handleGoBack} id="cancel-save" data-cy="entityCreateCancelButton" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating || isClosed}>
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

export default InvoiceUpdate;
