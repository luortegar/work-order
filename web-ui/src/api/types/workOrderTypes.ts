export interface DetailedWorkOrderResponse {
  workOrderId: string; // UUID
  workOrderNumber: string;
  branchId: string; // UUID
  recipientId: string; // UUID
  technicianId: string; // UUID
  equipmentId: string; // UUID
  serviceDetails: string;
  observations: string;
  photoIdList: string[]; // List<UUID>
  startTime: string; // formato: "yyyy-MM-dd HH:mm:ss"
  endTime: string;   // formato: "yyyy-MM-dd HH:mm:ss"
}
