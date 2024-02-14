import dayjs from 'dayjs';
import { ICompany } from 'app/shared/model/company.model';

export interface IInvoice {
  id?: number;
  totalSum?: number;
  invoiceDate?: dayjs.Dayjs;
  company?: ICompany | null;
}

export const defaultValue: Readonly<IInvoice> = {};
