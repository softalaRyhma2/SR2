import dayjs from 'dayjs';
import { IStock } from 'app/shared/model/stock.model';

export interface IReservation {
  id?: number;
  reservedQuantity?: number | null;
  reservationDate?: dayjs.Dayjs | null;
  isPickedUp?: boolean | null;
  stock?: IStock | null;
}

export const defaultValue: Readonly<IReservation> = {
  isPickedUp: false,
};
