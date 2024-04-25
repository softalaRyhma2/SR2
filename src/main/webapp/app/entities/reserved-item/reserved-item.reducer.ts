import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IReservedItem, defaultValue as defaultReservedItem } from 'app/shared/model/reserved-item.model';

const initialState: EntityState<IReservedItem> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultReservedItem,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/reserved-items';

// Actions

export const getEntities = createAsyncThunk('reservedItem/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
  return axios.get<IReservedItem[]>(requestUrl);
});

// OK:
export const getReservedItemEntitiesForReservation = createAsyncThunk(
  'reservedItem/fetch_entity_list_for_reservation',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/reservation/${id}`;
    return axios.get<IReservedItem[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getEntity = createAsyncThunk(
  'reservedItem/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IReservedItem>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

// Add error handling if same type already exists here:
export const createEntity = createAsyncThunk(
  'reservedItem/create_entity',
  async (entity: IReservedItem, thunkAPI) => {
    const result = await axios.post<IReservedItem>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'reservedItem/update_entity',
  async (entity: IReservedItem, thunkAPI) => {
    const result = await axios.put<IReservedItem>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'reservedItem/partial_update_entity',
  async (entity: IReservedItem, thunkAPI) => {
    const result = await axios.patch<IReservedItem>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'reservedItem/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IReservedItem>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const ReservedItemSlice = createEntitySlice({
  name: 'reservedItem',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(getReservedItemEntitiesForReservation), (state, action) => {
        const { data } = action.payload;
        state.loading = false;
        state.entities = data;
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = ReservedItemSlice.actions;

// Reducer
export default ReservedItemSlice.reducer;
