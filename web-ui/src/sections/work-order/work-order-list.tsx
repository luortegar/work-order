import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { GridColDef, GridRenderCellParams } from '@mui/x-data-grid';
import SimpleTable from 'src/components/table/simple-table';
import { Container, Chip, Button } from '@mui/material';
import { list, viewPDF } from 'src/api/workOrderApi';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import {Iconify} from 'src/components/iconify';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';
import { format } from 'date-fns';
import { convertArrayToDate } from 'src/utils/format-time';


export default function WorkOrderList() {
  const { showMessage } = useSnackbar();
  const navigate = useNavigate();

  const columns: GridColDef[] = [
    {
      hideable: false,
      field: 'workOrderId',
      headerName: 'Work order id',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => (
        <Chip
          label={params.value}
          color='default'
          variant='outlined'
          size='small'
          onClick={() => navigate(`/home/work-order/${params.row.workOrderId}`)}
        />
      ),
    },

    {
      hideable: false,
      field: 'workOrderId_',
      headerName: 'WO PDF',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => (
        <Chip
          label={"pdf"}
          color='default'
          variant='outlined'
          size='small'
          onClick={() => {
            viewPDF(params?.row?.workOrderId)
          }
        }
        />
      ),
    },

    { field: 'workOrderNumber', headerName: 'Work order number', width: 200, flex: 0.5 },
    {
        field: 'startTime',
        headerName: 'Start time',
        width: 200,
        flex: 0.5,
        renderCell: (params: GridRenderCellParams) => {
          return params.value? params.value : 'N/A';
        },
      },
      {
        field: 'endTime',
        headerName: 'End time',
        width: 200,
        flex: 0.5,
        renderCell: (params: GridRenderCellParams) => {
          return params.value? params.value : 'N/A';
        },
      },
    {
      field: 'creationDate',
      headerName: 'Creation date',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => {
        return params.value? params.value : 'N/A';
      },
    },
    {
      field: 'updateDate',
      headerName: 'Update date',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => {
        return params.value? params.value : 'N/A';

      },
    },
  ];

  const [dataList, setDataList] = useState([]);
  const [rowCount, setRowCount] = useState(0);

  useEffect(() => {
    refreshData(5, 0, '', '');
  }, []);

  const refreshData = (rowsPerPage:number, page:number, sort:string, filterName:string) => {
    list(rowsPerPage, page, sort, filterName).then((r) => {
      setDataList(r.data.content);
      setRowCount(r.data.totalElements);
      console.log(r);
    });
  };

  return (
    <Container maxWidth="xl">
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
        <Typography variant="h4">Work order</Typography>
        <Button
          variant="contained"
          color="inherit"
          startIcon={<Iconify icon="eva:done-all-fill" />}
          onClick={() => navigate(`/home/work-order/new`)}
        >
          New work order
        </Button>
      </Stack>
      <SimpleTable columns={columns} refreshData={refreshData} dataList={dataList} rowCount={rowCount} getRowId={(row) => row.workOrderId} pageSizeDefault={0}/>
    </Container>
  );
}
