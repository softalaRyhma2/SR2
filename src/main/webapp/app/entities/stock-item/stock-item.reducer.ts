import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IStockItem, defaultValue as defaultStockItem } from 'app/shared/model/stock-item.model';
import { IStock, defaultValue as defaultStock } from 'app/shared/model/stock.model';
import { translate } from 'react-jhipster';

const initialState: EntityState<IStockItem> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultStockItem,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/stock-items';

// Actions

export const getEntities = createAsyncThunk('stockItem/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
  return axios.get<IStockItem[]>(requestUrl);
});

//OK:
export const getStockItemEntitiesForStock = createAsyncThunk(
  'stockItem/fetch_entity_list_for_stock',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/stock/${id}`;
    return axios.get<IStockItem[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getEntity = createAsyncThunk(
  'stockItem/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IStockItem>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'stockItem/create_entity',
  async (entity: IStockItem, thunkAPI) => {
    try {
      const result = await axios.post<IStockItem>(apiUrl, cleanEntity(entity));

      // If the request is successful, dispatch appropriate actions based on stock existence
      const stockId = entity.stock?.id;
      if (stockId) {
        thunkAPI.dispatch(getStockItemEntitiesForStock(stockId)); // Fetch stock items for the stock
      } else {
        thunkAPI.dispatch(getEntities({})); // Fetch all entities
      }

      return result;
    } catch (error) {
      // Handle the error
      if (axios.isAxiosError(error)) {
        // Handle Axios errors
        console.log(error.response?.data);
        console.log(error.response?.status);
        console.log(error.response?.headers);
        console.log(error.request);

        // If the error is a 400 Bad Request, display a user-friendly message
        if (error.response?.status === 400) {
          throw new Error(translate('sr2App.stockItem.errors.stockItemTypeExists'));
        }
      } else {
        // Handle non-Axios errors
        console.error(error);
      }

      // Rethrow the error to propagate it
      throw error;
    }
  },
  { serializeError: serializeAxiosError },
);

/*
export const createEntity = createAsyncThunk(
  'stockItem/create_entity',
  async (entity: IStockItem, thunkAPI) => {
    try {
      // Check if stock items with the same type exist
      const { data: existingStockItems } = await axios.get<IStockItem[]>(`${apiUrl}/stock/${entity.stock.id}`);

      const existingItem = existingStockItems.find(item => item.stockItemType.id === entity.stockItemType.id);
      if (existingItem) {
        // If a stock item with the same type exists, throw an error
        throw new Error(
          'A stock item with the same type already exists in this stock. Either edit the existing item or create a new stock item with a different type.',
        );
      }

      // If no stock item with the same type exists, proceed with creating the entity
      const result = await axios.post<IStockItem>(apiUrl, cleanEntity(entity));
      thunkAPI.dispatch(getEntities({}));
      return result;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        // Handle Axios errors
        console.log(error.response?.data);
        console.log(error.response?.status);
        console.log(error.response?.headers);
        console.log(error.request);
      } else {
        // Handle non-Axios errors
        console.error(error);
      }
      throw error; // rethrow the error to propagate it
    }
  },
  { serializeError: serializeAxiosError },
);*/

export const updateEntity = createAsyncThunk(
  'stockItem/update_entity',
  async (entity: IStockItem, thunkAPI) => {
    const result = await axios.put<IStockItem>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'stockItem/partial_update_entity',
  async (entity: IStockItem, thunkAPI) => {
    const result = await axios.patch<IStockItem>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'stockItem/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IStockItem>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const StockItemSlice = createEntitySlice({
  name: 'stockItem',
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
      .addMatcher(isFulfilled(getStockItemEntitiesForStock), (state, action) => {
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

export const { reset } = StockItemSlice.actions;

// Reducer
export default StockItemSlice.reducer;
