import dayjs from 'dayjs';
import { IStockItem } from 'app/shared/model/stock-item.model';
import { IInvoice } from 'app/shared/model/invoice.model';

export interface IStock {
  id?: number;
  stockDate?: dayjs.Dayjs;
  stockId?: number | null;
  stockItems?: IStockItem[] | null;
  invoice?: IInvoice;
}

export const defaultValue: Readonly<IStock> = {};
