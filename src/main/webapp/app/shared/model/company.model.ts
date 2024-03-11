import { IInvoice } from 'app/shared/model/invoice.model';

export interface ICompany {
  id?: number;
  companyName?: string;
  companyEmail?: string;
  companyDetails?: string | null;
  invoices?: IInvoice[] | null;
}

export const defaultValue: Readonly<ICompany> = {};
