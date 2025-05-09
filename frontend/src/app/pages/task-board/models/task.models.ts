export interface Task {
  id: number;
  title: string;
  description: string | null;
  status: string;
  createdAt: string;
  updatedAt: string | null;
  assignedTo: number | null;
  dueDate: string | null;
  priority: string | null;
  tags: string[];
  completedAt: string | null;
  userStoryId: number | null;
  blockedByTaskId: number | null;
}
