import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { GridColDef, GridRenderCellParams } from '@mui/x-data-grid';
import SimpleTable from 'src/components/table/simple-table';
import { Container, Chip, Button } from '@mui/material';
import { list } from 'src/api/inspectionVisitApi';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { Iconify } from 'src/components/iconify';
import { useSnackbar } from 'src/context/snackbar/SnackbarContext';

export default function InspectionVisitList() {
  const { clientId, branchId } = useParams();
  const { showMessage } = useSnackbar();
  const navigate = useNavigate();

  const columns: GridColDef[] = [
    {
      hideable: false,
      field: 'inspectionVisitId',
      headerName: 'Visit ID',
      width: 200,
      flex: 0.5,
      renderCell: (params: GridRenderCellParams) => (
        <Chip
          label={params.value}
          color='default'
          variant='outlined'
          size='small'
          onClick={() => navigate(`/home/client/${clientId}/branch/${branchId}/inspectionVisit/${params.row.inspectionVisitId}`)}
        />
      ),
    },
    { field: 'title', headerName: 'Title', width: 200, flex: 0.5 },
    { field: 'date', headerName: 'Date', width: 200, flex: 0.5 },

  ];

  const [dataList, setDataList] = useState([]);
  const [rowCount, setRowCount] = useState(0);

  useEffect(() => {
    refreshData(10, 0, '', '');
  }, []);

  const refreshData = (rowsPerPage:number, page:number, sort:string, filterName:string) => {
    if(branchId){
      list(branchId, rowsPerPage, page, sort, filterName).then((r) => {
        setDataList(r.data.content);
        setRowCount(r.data.totalElements);
        console.log(r);
      }).catch(error => {
        showMessage('Failed to load equipment data', 'error');
      });
    }
  };

  return (
    <Container maxWidth="xl">
      <Stack direction="row" alignItems="center" justifyContent="space-between" mb={5}>
        <Typography variant="h4">Inspection Visit</Typography>
        <Button
          variant="contained"
          color="inherit"
          startIcon={<Iconify icon="mingcute:add-line" />}
          onClick={() => navigate(`/home/client/${clientId}/branch/${branchId}/inspectionVisit/new`)}
        >
          New Inspection Visit
        </Button>
      </Stack>
      <SimpleTable 
        columns={columns}
        refreshData={refreshData}
        dataList={dataList}
        rowCount={rowCount}
        getRowId={(row) => row.inspectionVisitId} pageSizeDefault={0}      />
    </Container>
  );
}
