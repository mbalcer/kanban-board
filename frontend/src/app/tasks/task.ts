export interface Task {
  taskId?: number;
  name?: string;
  description?: string;
  sequence?: number;
  state?: string;
  createDateTime?: string;
  project?: any;
}
