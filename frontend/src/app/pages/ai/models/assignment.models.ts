export interface TaskInfo {
  id: string | undefined;
  title: string;
}

export interface Task {
  id?: string;
  title: string;
  description?: string;
}

export interface AssignmentWithIds {
  userId: number;
  userName: string;
  assignedTaskIds: number[];
}

export interface AssignmentWithTasks {
  userId: number;
  userName: string;
  assignedTasks: TaskInfo[];
}

export interface AssignedTask {
  taskId: string;
  title: string;
  estimatedStartDate: string;
  estimatedEndDate: string;
  estimatedHours: number;
}

export interface PredictedTasksByUser {
  userId: number;
  userName: string;
  assignedTasks: AssignedTask[];
}
