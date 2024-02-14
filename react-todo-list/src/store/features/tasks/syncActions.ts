import { createAction } from "@reduxjs/toolkit";
import { Task } from "types/task";
import { TasksFilter } from "types/ui/task-list";

export const setTasksFilter = createAction<TasksFilter>('tasks/set_tasks_filter')

export const setTasks = createAction<Task[]>('tasks/set_all_tasks')
