import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, PayloadAction } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IStock, defaultValue } from 'app/shared/model/stock.model';
import { combineReducers } from '@reduxjs/toolkit';
import stockItemReducer, { getStockItemEntitiesForStock, StockItemSlice } from '../stock-item/stock-item.reducer';
import stockItemTypeReducer, { StockItemTypeSlice } from '../stock-item-type/stock-item-type.reducer';
import { produce } from 'immer';
import { getCurrentUserCompany } from '../company/company.reducer';
import { getEntities as getInvoices } from '../invoice/invoice.reducer';

// Import getEntities async thunk from invoice.reducer

//import { getInvoices } from './invoices'; // Import the getInvoices function

export interface IStockState extends EntityState<IStock> {
  loading: boolean;
  errorMessage: string | null;
  updating: boolean;
  totalItems: number;
  updateSuccess: boolean;
  companyNames: Record<string, string>;
  stockTotal: number;
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
  stockTotal: 0,
};

const apiUrl = 'api/stocks';

// Actions

// Async Thunk to Fetch Company Stocks
export const fetchUserCompanyStocks = createAsyncThunk('stock/fetch_user_company_stocks', async (_, thunkAPI) => {
  try {
    // Step 1: Fetch the user's company

    const company = await thunkAPI.dispatch(getCurrentUserCompany());

    // Step 2: Fetch invoices linked to the user's company
    const invoicesAction: any = await thunkAPI.dispatch(getInvoices({ page: 1, size: 10, sort: 'asc' }));
    const invoices = invoicesAction.payload.data; // Access the data property

    // Step 3: Extract invoice IDs
    const invoiceIds = invoices.map((invoice: any) => invoice.id);

    // Step 4: Fetch stocks linked to invoices
    const requestUrl = `${apiUrl}?invoiceIds=${invoiceIds.join(',')}`;
    const response = await axios.get<IStock[]>(requestUrl);

    // Step 5: Return the fetched stocks
    return response.data;
  } catch (error) {
    console.error('Error fetching user company stocks:', error);
    throw error; // Rethrow the error for handling in components or other parts of the app
  }
});

export const getCompanyNameByInvoiceId = createAsyncThunk('stock/fetch_company_name', async (invoiceId: string, { dispatch }) => {
  try {
    const requestUrl = `api/invoices/${invoiceId}`;
    const response = await axios.get<{ company: { companyName: string } }>(requestUrl);
    console.log(response);
    const companyName = response.data.company.companyName;
    console.log('Company name:', companyName);
    dispatch(setCompanyName({ invoiceId, companyName }));
    return companyName;
  } catch (error) {
    console.error('Error fetching company name:', error);
    throw error;
  }
});

export const getEntities = createAsyncThunk('stock/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
  return axios.get<IStock[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'stock/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IStock>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'stock/create_entity',
  async (entity: IStock, thunkAPI) => {
    const result = await axios.post<IStock>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'stock/update_entity',
  async (entity: IStock, thunkAPI) => {
    const result = await axios.put<IStock>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'stock/partial_update_entity',
  async (entity: IStock, thunkAPI) => {
    const result = await axios.patch<IStock>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'stock/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IStock>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// Slice

export const StockSlice = createSlice({
  name: 'stock',
  initialState,
  reducers: {
    setCompanyName(state, action: PayloadAction<{ invoiceId: string; companyName: string }>) {
      const { invoiceId, companyName } = action.payload;
      state.companyNames[invoiceId] = companyName;
    },
    reset: state => initialState,
  },
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
      .addCase(getStockItemEntitiesForStock.fulfilled, (state, action) => {
        const stockItems = action.payload.data;
        let stockItemTotal = 0;
        stockItems.forEach(stockItem => {
          stockItemTotal += stockItem.quantity * stockItem.price;
        });
        state.stockTotal = stockItemTotal;
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
      })
      .addMatcher(isFulfilled(getStockItemEntitiesForStock), (state, action) => {
        const { data } = action.payload;
        return {
          ...state,
          loading: false,
          entities: data,
        };
      })
      .addMatcher(isFulfilled(fetchUserCompanyStocks), (state, action) => {
        state.loading = false;
        state.entities = action.payload;
        state.totalItems = action.payload.length;
      })
      .addMatcher(isPending(fetchUserCompanyStocks), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isFulfilled(fetchUserCompanyStocks), state => {
        state.loading = false;
      });
  },
});

export const { setCompanyName, reset } = StockSlice.actions;

export const fetchCompanyNames = () => async (dispatch, getState) => {
  const { entities } = getState().stock;
  for (const entity of entities) {
    await dispatch(getCompanyNameByInvoiceId(entity.invoiceId));
  }
};

// Testing stock & stockItem
export const rootReducer = combineReducers({
  stock: StockSlice.reducer,
  stockItem: StockItemSlice.reducer,
  stockItemType: StockItemTypeSlice.reducer,
});

export const { reset: resetStock } = StockSlice.actions;
export const { reset: resetStockItem } = StockItemSlice.actions;
export const { reset: resetStockItemType } = StockItemTypeSlice.actions;
// Reducer
export default StockSlice.reducer;
