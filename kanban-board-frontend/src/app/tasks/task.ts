export interface Task {
  taskId?: string;
  name?: string;
  description?: string;
  sequence?: number;
  state?: string;
  createDateTime?: string;
  project?: string;
  student?: string;
}
