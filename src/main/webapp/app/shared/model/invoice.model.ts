import dayjs from 'dayjs';
import { ICompany } from 'app/shared/model/company.model';
import { IStock } from 'app/shared/model/stock.model';

export interface IInvoice {
  id?: number;
  totalSum?: number | null;
  invoiceDate?: dayjs.Dayjs | null;
  company?: ICompany | null;
  stock?: IStock | null;
}

export const defaultValue: Readonly<IInvoice> = {};
