import dayjs from 'dayjs';
import { IStock } from 'app/shared/model/stock.model';
import { ICompany } from 'app/shared/model/company.model';

export interface IInvoice {
  id?: number;
  totalSum?: number | null;
  invoiceDate?: dayjs.Dayjs | null;
  invoiceId?: number | null;
  stocks?: IStock[] | null;
  company?: ICompany;
}

export const defaultValue: Readonly<IInvoice> = {};
