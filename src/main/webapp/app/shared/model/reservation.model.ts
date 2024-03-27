import dayjs from 'dayjs';
import { IReservedItem } from 'app/shared/model/reserved-item.model';

export interface IReservation {
  id?: number;
  reservationDate?: dayjs.Dayjs;
  isPickedUp?: boolean;
  reservedItems?: IReservedItem[] | null;
}

export const defaultValue: Readonly<IReservation> = {
  isPickedUp: false,
};
