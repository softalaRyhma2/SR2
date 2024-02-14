import dayjs from 'dayjs';
import { IStock } from 'app/shared/model/stock.model';

export interface IReservation {
  id?: number;
  reservedQuantity?: number;
  reservationDate?: dayjs.Dayjs;
  isPickedUp?: boolean;
  stock?: IStock | null;
}

export const defaultValue: Readonly<IReservation> = {
  isPickedUp: false,
};
