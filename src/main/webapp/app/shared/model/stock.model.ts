import dayjs from 'dayjs';
import { IInvoice } from 'app/shared/model/invoice.model';

export interface IStock {
  id?: number;
  quantity?: number;
  available?: number;
  price?: number;
  stockDate?: dayjs.Dayjs;
  invoice?: IInvoice | null;
}

export const defaultValue: Readonly<IStock> = {};
