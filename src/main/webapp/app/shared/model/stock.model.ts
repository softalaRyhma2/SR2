import dayjs from 'dayjs';

export interface IStock {
  id?: number;
  quantity?: number | null;
  available?: number | null;
  price?: number | null;
  date?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IStock> = {};
