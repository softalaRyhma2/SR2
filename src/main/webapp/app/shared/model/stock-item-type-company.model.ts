import { IStockItemType } from 'app/shared/model/stock-item-type.model';
import { ICompany } from 'app/shared/model/company.model';

export interface IStockItemTypeCompany {
  id?: number;
  typePrice?: number;
  stockItemType?: IStockItemType | null;
  company?: ICompany | null;
}

export const defaultValue: Readonly<IStockItemTypeCompany> = {};
