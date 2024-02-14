import { createReducer } from "@reduxjs/toolkit";
import { Task, TaskStatus } from "types/task";
import { TasksFilter, TasksStatusFilter } from "types/ui/task-list";
import { setTasks, setTasksFilter } from "./syncActions";
import { tasksApi } from "./rtk";

export type TaskListState = Readonly<{
    tasks: Task[],
    filter: TasksFilter,

    loading: boolean,
    currentRequestId: number,
    errors: string[]
}>;

const initialState: TaskListState = {
    tasks: [],
    filter: {
        status: TasksStatusFilter.All
    },

    loading: false,
    currentRequestId: -1,
    errors: []
};

const tasksReducer = createReducer(initialState, (builder) => {
    builder
        .addCase(setTasks, 
            (state, action) => {
                state.tasks.length = 0;
                state.tasks.push(...action.payload);
            }
        )
        .addCase(setTasksFilter,
            (state, action) => {
                state.filter = action.payload;
            }
        )

        .addMatcher(tasksApi.endpoints.createTask.matchFulfilled,
            (state, action) => {
                state.tasks.push(action.payload);
            }
        )
        .addMatcher(tasksApi.endpoints.setTaskName.matchFulfilled,
            (state, action) => {
                if (action.payload) {
                    const idx = state.tasks.findIndex(t => t.id == action.payload?.id);
                    state.tasks[idx] = action.payload;
                }
            }
        )
        .addMatcher(tasksApi.endpoints.setTaskStatus.matchFulfilled,
            (state, action) => {
                if (action.payload) {
                    const idx = state.tasks.findIndex(t => t.id == action.payload?.id);
                    state.tasks[idx] = action.payload;
                }
            }
        )
        .addMatcher(tasksApi.endpoints.setAllTaskStatus.matchFulfilled,
            (state, action) => {
                state.tasks.forEach(t => t.status = action.payload);
            }
        )
        .addMatcher(tasksApi.endpoints.deleteCompleted.matchFulfilled,
            (state, action) => {
                state.tasks = state.tasks.filter(t => t.status != TaskStatus.Completed);
            }
        )
        .addMatcher(tasksApi.endpoints.deleteTask.matchFulfilled,
            (state, action) => {
                state.tasks = state.tasks.filter(t => t.id != action.payload?.id);
            }
        )
});

export default tasksReducer;