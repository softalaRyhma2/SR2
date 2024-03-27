import { IStockItem } from 'app/shared/model/stock-item.model';

export interface IStockItemType {
  id?: number;
  typeName?: string;
  stockItems?: IStockItem[] | null;
}

export const defaultValue: Readonly<IStockItemType> = {};
