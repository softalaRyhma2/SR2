import dayjs from 'dayjs';
import { IStockItem } from 'app/shared/model/stock-item.model';
import { IInvoice } from 'app/shared/model/invoice.model';

export interface IStock {
  id?: number;
  stockDate?: dayjs.Dayjs;
  stockItems?: IStockItem[] | null;
  invoice?: IInvoice | null;
}

export const defaultValue: Readonly<IStock> = {};

export interface IStockState {
  loading: boolean;
  errorMessage: string | null;
  entities: IStock[];
  entity: IStock;
  updating: boolean;
  totalItems: number;
  updateSuccess: boolean;
  companyNames: Record<string, string>;
}

const initialState: IStockState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  companyNames: {},
};
