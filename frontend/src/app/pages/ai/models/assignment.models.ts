export interface AssignedTask {
  taskId: string;
  title: string;
  estimatedStartDate: string;
  estimatedEndDate: string;
  estimatedHours: number;
}

export interface PredictedTasksByUser {
  userName: string;
  assignedStories: AssignedStory[];
}

export interface AssignedStory {
  storyTitle: string;
  tasks: AssignedTask[];
}
