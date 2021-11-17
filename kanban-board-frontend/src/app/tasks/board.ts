import {Task} from './task';

export interface Board {
  name: string;
  value: string;
  tasks: Task[];
}
