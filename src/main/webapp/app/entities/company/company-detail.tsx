import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './company.reducer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export const CompanyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const companyEntity = useAppSelector(state => state.company.entity);
  const authorities = useAppSelector(state => state.authentication.account.authorities);
  const isAdminOrRecser = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN, AUTHORITIES.RECSER]);

  const handleGoBack = () => {
    history.back(); // Palaa edelliselle sivulle
  };

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="companyDetailsHeading">
          <Translate contentKey="sr2App.company.detail.title">Company</Translate>
        </h2>
        <dl className="jh-entity-details">
          {isAdminOrRecser && (
            <>
              <dt>
                <span id="id">
                  <Translate contentKey="global.field.id">ID</Translate>
                </span>
              </dt>
              <dd>{companyEntity.id}</dd>
            </>
          )}
          <dt>
            <span id="companyName">
              <Translate contentKey="sr2App.company.companyName">Company Name</Translate>
            </span>
          </dt>
          <dd>{companyEntity.companyName}</dd>
          <dt>
            <span id="companyEmail">
              <Translate contentKey="sr2App.company.companyEmail">Company Email</Translate>
            </span>
          </dt>
          <dd>{companyEntity.companyEmail}</dd>
          <dt>
            <span id="companyDetails">
              <Translate contentKey="sr2App.company.companyDetails">Company Details</Translate>
            </span>
          </dt>
          <dd>{companyEntity.companyDetails}</dd>
        </dl>
        {/*<Button tag={Link} to="/company" replace color="info" data-cy="entityDetailsBackButton">*/}
        <Button onClick={handleGoBack} replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/company/${companyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompanyDetail;
