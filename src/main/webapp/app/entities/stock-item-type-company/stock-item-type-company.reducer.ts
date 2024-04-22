import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IStockItemTypeCompany, defaultValue } from 'app/shared/model/stock-item-type-company.model';
import { IStockItemType } from 'app/shared/model/stock-item-type.model';
import { ICompany } from 'app/shared/model/company.model';

const initialState: EntityState<IStockItemTypeCompany> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/stock-item-type-companies';

// Actions

export const getEntities = createAsyncThunk('stockItemTypeCompany/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
  return axios.get<IStockItemTypeCompany[]>(requestUrl);
});
/*export const getEntities = createAsyncThunk(
  'stockItemTypeCompany/fetch_entity_list',
  async ({ page, size, sort }: IQueryParams) => {
    const stockItemTypesRequest = axios.get<IStockItemType[]>(`${apiUrl}/stock-item-types`);
    const companiesRequest = axios.get<ICompany[]>(`${apiUrl}/companies`);

    const [stockItemTypesResponse, companiesResponse] = await Promise.all([stockItemTypesRequest, companiesRequest]);

    // Here you can combine data from stockItemTypesResponse.data and companiesResponse.data into IStockItemTypeCompany entities
    const combinedData: IStockItemTypeCompany[] = [];

    stockItemTypesResponse.data.forEach(stockItemType => {
      companiesResponse.data.forEach(company => {
        // Check if there is a match between stockItemType and company
        if (/* your condition for matching stockItemType and company * /) {
          // Create a new IStockItemTypeCompany object
          const stockItemTypeCompany: IStockItemTypeCompany = {
            // Assign properties from stockItemType and company as needed
          };
          combinedData.push(stockItemTypeCompany);
        }
      });
    });
    return combinedData;
  }
);*/

export const getEntity = createAsyncThunk(
  'stockItemTypeCompany/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IStockItemTypeCompany>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'stockItemTypeCompany/create_entity',
  async (entity: IStockItemTypeCompany, thunkAPI) => {
    const result = await axios.post<IStockItemTypeCompany>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'stockItemTypeCompany/update_entity',
  async (entity: IStockItemTypeCompany, thunkAPI) => {
    const result = await axios.put<IStockItemTypeCompany>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'stockItemTypeCompany/partial_update_entity',
  async (entity: IStockItemTypeCompany, thunkAPI) => {
    const result = await axios.patch<IStockItemTypeCompany>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'stockItemTypeCompany/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IStockItemTypeCompany>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const StockItemTypeCompanySlice = createEntitySlice({
  name: 'stockItemTypeCompany',
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
      /* .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { stockItemTypesResponse, companiesResponse } = action.payload;
      
        // Combine data from StockItemType and Company into IStockItemTypeCompany entities
        const combinedData: IStockItemTypeCompany[] = [];
      
        // Assuming data contains arrays of StockItemType and Company objects
        const combinedEntities = combineStockItemTypesAndCompanies(stockItemTypesResponse.data, companiesResponse.data);

  return {
    ...state,
    loading: false,
    entities: combinedEntities,
    totalItems: parseInt(stockItemTypesResponse.headers['x-total-count'], 10),
  };
})*/
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

export const { reset } = StockItemTypeCompanySlice.actions;

// Reducer
export default StockItemTypeCompanySlice.reducer;
