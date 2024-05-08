import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './stock-item-type-company.reducer';

export const StockItemTypeCompanyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stockItemTypeCompanyEntity = useAppSelector(state => state.stockItemTypeCompany.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stockItemTypeCompanyDetailsHeading">
          <Translate contentKey="sr2App.stockItemTypeCompany.detail.title">StockItemTypeCompany</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{stockItemTypeCompanyEntity.id}</dd>
          <dt>
            <span id="typePrice">
              <Translate contentKey="sr2App.stockItemTypeCompany.typePrice">Type Price</Translate>
            </span>
          </dt>
          <dd>{stockItemTypeCompanyEntity.typePrice}</dd>
          <dt>
            <Translate contentKey="sr2App.stockItemTypeCompany.stockItemType">Stock Item Type</Translate>
          </dt>
          <dd>{stockItemTypeCompanyEntity.stockItemType ? stockItemTypeCompanyEntity.stockItemType.typeName : ''}</dd>
          <dt>
            <Translate contentKey="sr2App.stockItemTypeCompany.company">Company</Translate>
          </dt>
          <dd>{stockItemTypeCompanyEntity.company ? stockItemTypeCompanyEntity.company.companyName : ''}</dd>
        </dl>
        <Button tag={Link} to="/stock-item-type-company" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stock-item-type-company/${stockItemTypeCompanyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StockItemTypeCompanyDetail;
