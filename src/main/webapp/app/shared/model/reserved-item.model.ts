import { IReservation } from 'app/shared/model/reservation.model';
import { IStockItem } from 'app/shared/model/stock-item.model';

export interface IReservedItem {
  id?: number;
  quantity?: number;
  reservation?: IReservation | null;
  stockItem?: IStockItem | null;
}

export const defaultValue: Readonly<IReservedItem> = {};
