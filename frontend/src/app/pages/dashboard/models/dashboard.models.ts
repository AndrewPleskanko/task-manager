export interface Task {
  id?: string;
  title?: string;
  description?: string;
  status?: string;
  createdAt?: Date;
  updatedAt?: Date;
  userId?: number;
  assignedTo?: number;
  dueDate?: Date;
  priority?: string;
  tags?: string;
  completedAt?: Date;
  taskType?: string;
  estimatedTime?: number;
  attachments?: string[];
}
