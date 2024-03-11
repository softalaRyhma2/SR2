import dayjs from 'dayjs';
import { IReservedItem } from 'app/shared/model/reserved-item.model';

export interface IReservation {
  id?: number;
  reservedQuantity?: number;
  reservationDate?: dayjs.Dayjs;
  isPickedUp?: boolean;
  reservationId?: number | null;
  reservedItems?: IReservedItem[] | null;
}

export const defaultValue: Readonly<IReservation> = {
  isPickedUp: false,
};
