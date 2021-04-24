export interface Task {
  id?: number;
  name?: string;
  description?: string;
  sequence?: number;
  state?: string;
  createDateTime?: string;
  project?: any;
}
