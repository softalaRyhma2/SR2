import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getCompanyNameByInvoiceId, getEntity } from './stock.reducer';
import { getStockItemEntitiesForStock } from '../stock-item/stock-item.reducer';

export const StockDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
    dispatch(getStockItemEntitiesForStock(id));
    //  dispatch(getEntitiesForStockItem(id));
  }, [id]);

  const stockEntity = useAppSelector(state => state.stock.entity);
  const stockItemList = useAppSelector(state => state.stockItem.entities);
  console.log('STOCKITEMLIST: ' + JSON.stringify(stockItemList));
  const stockItemTypeList = useAppSelector(state => state.stockItemType.entities);
  console.log('STOCKITEMTYPELIST: ' + JSON.stringify(stockItemTypeList));
  const [companyName, setCompanyName] = useState('');

  const getStockItemTypeName = (stockItemTypeId: number) => {
    const stockItemType = stockItemTypeList.find(item => item.id === stockItemTypeId);
    return stockItemType ? stockItemType.name : ''; // Olettaen, että stock item typellä on "name" -kenttä
  };

  useEffect(() => {
    if (stockEntity.invoice && stockEntity.invoice.id) {
      dispatch(getCompanyNameByInvoiceId(String(stockEntity.invoice.id))).then((response: any) => {
        setCompanyName(response.payload);
      });
    }
  }, [stockEntity.invoice]);

  return (
    <Row>
      <Col md="4">
        <h2 data-cy="stockDetailsHeading">
          <Translate contentKey="sr2App.stock.detail.title">Stock</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{stockEntity.id}</dd>
          <dt>
            <span id="stockDate">
              <Translate contentKey="sr2App.stock.stockDate">Stock Date</Translate>
            </span>
          </dt>
          <dd>{stockEntity.stockDate ? <TextFormat value={stockEntity.stockDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="sr2App.stock.invoice">Invoice</Translate>
          </dt>
          <dd>{stockEntity.invoice ? stockEntity.invoice.id : ''}</dd>
          <dt>
            <Translate contentKey="sr2App.stock.companyName">Company Name</Translate>
          </dt>
          <dd>{companyName}</dd>
        </dl>
        <Button tag={Link} to="/stock" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stock/${stockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
      <Col md="8">
        <h2>Stock Items</h2>
        <Table>
          <thead>
            <tr>
              <th>StockItemID</th>
              <th>Quantity</th>
              <th>Available</th>
              <th>Price</th>
              <th>Type</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {stockItemList.length === 0 ? (
              <tr>
                <td colSpan={8}>Stock items not found</td>
              </tr>
            ) : (
              stockItemList.map(stockItemEntity => (
                <tr key={stockItemEntity.id}>
                  <td>{stockItemEntity.id}</td>
                  <td>{stockItemEntity.quantity}</td>
                  <td>{stockItemEntity.available}</td>
                  <td>{stockItemEntity.price} €</td>
                  <td>{stockItemEntity.stockItemType ? stockItemEntity.stockItemType.typeName : ''}</td>
                  <td>
                    <Button tag={Link} to={`/stock-item/${stockItemEntity.id}`} color="primary">
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

export default StockDetail;
