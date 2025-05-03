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

export type Assignment = AssignmentWithIds | AssignmentWithTasks;

export interface PredictedTasksByUser {
  userId: number;
  userName: string;
  assignedTasks: { id: number; title: string }[];
}
