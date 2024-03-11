import { IReservedItem } from 'app/shared/model/reserved-item.model';
import { IStock } from 'app/shared/model/stock.model';
import { IStockItemType } from 'app/shared/model/stock-item-type.model';

export interface IStockItem {
  id?: number;
  quantity?: number;
  available?: number;
  price?: number;
  reservedItems?: IReservedItem[] | null;
  stock?: IStock;
  stockItemType?: IStockItemType;
}

export const defaultValue: Readonly<IStockItem> = {};
