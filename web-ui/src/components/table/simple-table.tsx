import React, { useState, useCallback } from 'react';
import {
  Card,
  Box,
  TextField,
  InputAdornment,
  TableContainer,
  useTheme,
} from '@mui/material';
import {
  GridColDef,
  DataGrid,
  gridClasses,
} from '@mui/x-data-grid';
import Scrollbar from 'src/components/scrollbar-old';

interface ISimpleTableProps {
  columns: GridColDef[];
  dataList: any[];
  rowCount: number;
  pageSizeDefault: number;
  refreshData: (
    size: number,
    page: number,
    sort: string,
    searchTerm: string
  ) => any;
  getRowId: (row: any) => string | number;
}

const SimpleTable: React.FC<ISimpleTableProps> = ({
  columns,
  dataList,
  rowCount,
  refreshData,
  pageSizeDefault,
  getRowId,
}) => {
  const theme = useTheme();
  const [searchTerm, setSearchTerm] = useState('');
  const [sort, setSort] = useState('');
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(pageSizeDefault);

  const handleSearchChange = useCallback(
    (event: React.ChangeEvent<HTMLInputElement>) => {
      const newSearchTerm = event.target.value;
      setSearchTerm(newSearchTerm);

      if (newSearchTerm.length >= 2 || newSearchTerm.length === 0) {
        refreshData(pageSize, page, sort, newSearchTerm);
      }
    },
    [pageSize, page, sort, refreshData]
  );

  return (
    <Card
      sx={{
        p: 2,
        borderRadius: 3,
        boxShadow: '0px 4px 20px rgba(0,0,0,0.05)',
        backgroundColor: theme.palette.background.paper,
      }}
    >
      <Scrollbar>
        <TableContainer>
          <Box sx={{ mb: 2 }}>
            <TextField
              value={searchTerm}
              onChange={handleSearchChange}
              placeholder="Buscar cliente..."
              variant="outlined"
              fullWidth
              size="small"
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                  </InputAdornment>
                ),
              }}
              sx={{
                borderRadius: 2,
                backgroundColor: theme.palette.background.default,
                '& .MuiOutlinedInput-root': {
                  borderRadius: 2,
                },
              }}
            />
          </Box>

          <div style={{ height: 500, width: '100%' }}>
            <DataGrid
              disableRowSelectionOnClick
              disableColumnSelector
              rows={dataList}
              columns={columns}
              getRowId={getRowId}
              pagination
              sortingMode="server"
              filterMode="server"
              paginationMode="server"
              onPaginationModelChange={(newPaginationModel) => {
                refreshData(
                  newPaginationModel.pageSize,
                  newPaginationModel.page,
                  sort,
                  searchTerm
                );
                setPageSize(newPaginationModel.pageSize);
                setPage(newPaginationModel.page);
              }}
              onSortModelChange={(newSortModel) => {
                if (newSortModel?.[0]) {
                  const newSort = `${newSortModel?.[0].field},${newSortModel?.[0].sort}`;
                  refreshData(pageSize, page, newSort, searchTerm);
                  setSort(newSort);
                } else {
                  refreshData(pageSize, page, '', searchTerm);
                  setSort('');
                }
              }}
              rowCount={rowCount}
              pageSizeOptions={[5, 10, 20, 50, 100]}
              initialState={{
                pagination: {
                  paginationModel: { pageSize: 5, page: 0 },
                },
              }}
              disableColumnFilter
              sx={{
                border: 'none', // quita borde general
                borderRadius: 2,
                backgroundColor: theme.palette.background.paper,
                [`& .${gridClasses.columnHeaders}`]: {
                  backgroundColor: theme.palette.grey[100],
                  color: theme.palette.text.secondary,
                  fontWeight: 'bold',
                  fontSize: 14,
                  borderBottom: 'none', // quita línea inferior del header
                },
                [`& .${gridClasses.cell}`]: {
                  borderBottom: `1px solid ${theme.palette.divider}`,
                },
                [`& .${gridClasses.footerContainer}`]: {
                  borderTop: 'none', // quita línea superior del footer (paginación)
                },
                [`& .${gridClasses.virtualScroller}`]: {
                  borderLeft: 'none', // quita borde lateral izquierdo
                  borderRight: 'none', // quita borde lateral derecho
                },
                [`& .${gridClasses.row}:hover`]: {
                  backgroundColor: theme.palette.action.hover,
                },
                [`& .${gridClasses.columnSeparator}`]: {
                  display: 'none',
                },
              }}
            />

          </div>
        </TableContainer>
      </Scrollbar>
    </Card>
  );
};

export default SimpleTable;
