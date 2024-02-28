import dayjs from 'dayjs';
import { ICompany } from 'app/shared/model/company.model';

export interface IInvoice {
  id?: number;
  totalSum?: number | null;
  invoiceDate?: dayjs.Dayjs | null;
  company?: ICompany;
}

export const defaultValue: Readonly<IInvoice> = {};
