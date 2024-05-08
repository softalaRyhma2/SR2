import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IInvoice, defaultValue } from 'app/shared/model/invoice.model';
import { IStock } from 'app/shared/model/stock.model';
import { translate } from 'react-jhipster';

const initialState: EntityState<IInvoice> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/invoices';

// Actions

export const getEntities = createAsyncThunk('invoice/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
  return axios.get<IInvoice[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'invoice/fetch_entity',
  async (id: string | number) => {
    const invoiceRequestUrl = `${apiUrl}/${id}`;
    const invoiceResponse = await axios.get<IInvoice>(invoiceRequestUrl);
    //console.log('I-REDUCER: Invoice response:', invoiceResponse.data);

    await Promise.all(
      invoiceResponse.data.stocks.map(async stock => {
        const stockRequestUrl = `api/stocks/${stock.id}`;
        //console.log('I-REDUCER: Stock request URL:', stockRequestUrl);

        const stockResponse = await axios.get<IStock>(stockRequestUrl);
        //console.log('I-REDUCER: Stock response:', stockResponse.data);

        // check if invoice stock-data matches stock response data
        const matchingStockIndex = invoiceResponse.data.stocks.findIndex(item => item.id === stockResponse.data.id);
        if (matchingStockIndex !== -1) {
          //update stock data changes to invoice
          invoiceResponse.data.stocks[matchingStockIndex] = stockResponse.data;
        }
      }),
    );

    //console.log('I-REDUCER: Updated Invoice data:', invoiceResponse.data);

    return invoiceResponse.data;
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'invoice/create_entity',
  async (entity: IInvoice, thunkAPI) => {
    const result = await axios.post<IInvoice>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'invoice/update_entity',
  async (entity: IInvoice, thunkAPI) => {
    const result = await axios.put<IInvoice>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'invoice/partial_update_entity',
  async (entity: IInvoice, thunkAPI) => {
    const result = await axios.patch<IInvoice>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'invoice/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    try {
      // Check if there are associated stocks
      const response = await axios.get<IInvoice>(requestUrl);
      const invoice = response.data;
      if (invoice.stocks && invoice.stocks.length > 0) {
        throw new Error(translate('sr2App.invoice.errors.associatedStocks'));
      }

      // Proceed with deletion if no associated stocks are found
      const result = await axios.delete<IInvoice>(requestUrl);
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
);

// slice

export const InvoiceSlice = createEntitySlice({
  name: 'invoice',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload;
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

export const { reset } = InvoiceSlice.actions;

// Reducer
export default InvoiceSlice.reducer;
