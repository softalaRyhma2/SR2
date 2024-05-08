import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Alert } from 'reactstrap'; // Import Alert component
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IInvoice } from 'app/shared/model/invoice.model';
import { getEntities as getInvoices } from 'app/entities/invoice/invoice.reducer';
import { IStock } from 'app/shared/model/stock.model';
import { getEntity, updateEntity, createEntity, reset } from './stock.reducer';

export const StockUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const invoices = useAppSelector(state => state.invoice.entities);
  const stockEntity = useAppSelector(state => state.stock.entity);
  const loading = useAppSelector(state => state.stock.loading);
  const updating = useAppSelector(state => state.stock.updating);
  const updateSuccess = useAppSelector(state => state.stock.updateSuccess);

  const [error, setError] = useState<string | null>(null); // State to manage error message

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getInvoices({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      navigate(`/stock`);
    }
  }, [updateSuccess, id, navigate]);

  const handleGoBack = () => {
    navigate(`/stock`);
  };

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...stockEntity,
      ...values,
      invoice: invoices.find(it => it.id.toString() === values.invoice.toString()),
    };

    // Check if invoice is not selected
    if (!entity.invoice) {
      setError('Please select an invoice before saving.'); // Set error message
      return; // Prevent further execution
    }

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
          ...stockEntity,
          invoice: stockEntity?.invoice?.id,
        };

  const today = new Date().toISOString().substring(0, 10);

  // Filter invoices where isClosed is false
  const filteredInvoices = invoices.filter(invoice => !invoice.isClosed);

  return (
    <div>
      {/* Render error message if exists */}
      {error && (
        <Row className="justify-content-center">
          <Col md="8">
            <Alert color="warning">{error}</Alert>
          </Col>
        </Row>
      )}

      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sr2App.stock.home.createOrEditLabel" data-cy="StockCreateUpdateHeading">
            <Translate contentKey="sr2App.stock.home.createOrEditLabel">Create or edit a Stock</Translate>
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
                  id="stock-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sr2App.stock.stockDate')}
                id="stock-stockDate"
                name="stockDate"
                data-cy="stockDate"
                type="date"
                defaultValue={today}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              {isNew ? (
                <ValidatedField id="stock-invoice" name="invoice" data-cy="invoice" label={translate('sr2App.stock.invoice')} type="select">
                  {filteredInvoices && filteredInvoices.length > 0 ? (
                    filteredInvoices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id} - {otherEntity.company.companyName}
                      </option>
                    ))
                  ) : (
                    <option disabled>
                      <Translate contentKey="sr2App.invoice.home.notFound">No Reserved Items found</Translate>
                    </option>
                  )}
                </ValidatedField>
              ) : (
                <div>
                  <span>Invoice number: </span>
                  <span>{stockEntity.invoice?.id}</span>
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

export default StockUpdate;
