import { IStockItem } from 'app/shared/model/stock-item.model';

export interface IStockItemType {
  id?: number;
  name?: string;
  stockItemTypeId?: number | null;
  stockItems?: IStockItem[] | null;
}

export const defaultValue: Readonly<IStockItemType> = {};
